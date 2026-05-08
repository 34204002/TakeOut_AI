
package com.sky.ai.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.entity.Review;
import com.sky.entity.ReviewReplyDraft;
import com.sky.entity.ReviewReplyKnowledge;
import com.sky.mapper.ReviewMapper;
import com.sky.ai.mapper.ReviewReplyDraftMapper;
import com.sky.ai.mapper.ReviewReplyKnowledgeMapper;
import com.sky.ai.service.ReviewReplyService;
import com.sky.ai.util.AiCallUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评价智能回复服务实现类
 */
@Service
@Slf4j
public class ReviewReplyServiceImpl implements ReviewReplyService {

    @Autowired
    private AiCallUtil aiCallUtil;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private ReviewReplyDraftMapper draftMapper;

    @Autowired
    private ReviewReplyKnowledgeMapper knowledgeMapper;
    
    /**
     * 差评回复专用的向量存储
     */
    @Autowired
    @Qualifier("reviewVectorStore")
    private SimpleVectorStore vectorStore;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void generateReplyDraft(Long reviewId, String reviewContent, Integer rating) {
        log.info("=== 开始生成评价回复草稿，reviewId: {}, rating: {} ===", reviewId, rating);

        try {
            // 第一步：AI 情感与意图分析
            Map<String, String> analysisResult = analyzeReview(reviewContent, rating);
            
            String emotion = analysisResult.get("emotion");
            String problemType = analysisResult.get("type");
            String demand = analysisResult.get("demand");

            log.info("AI 分析结果 - 情绪: {}, 类型: {}, 诉求: {}", emotion, problemType, demand);

            // 第二步：RAG 检索相似模板
            List<ReviewReplyKnowledge> templates = retrieveTemplates(problemType);
            
            // 第三步：AI 生成回复
            String generatedReply = generateReply(reviewContent, emotion, problemType, demand, templates);

            // 【修复】查询订单ID
            Review review = reviewMapper.getById(reviewId);
            if (review == null) {
                log.error("评价不存在，无法生成草稿，reviewId: {}", reviewId);
                return;
            }
            Long orderId = review.getOrderId();

            // 第四步：持久化草稿
            ReviewReplyDraft draft = new ReviewReplyDraft();
            draft.setOrderId(orderId);  // ← 关键修复：设置 orderId
            draft.setReviewId(reviewId);
            draft.setReviewContent(reviewContent);
            draft.setAiAnalysis(objectMapper.writeValueAsString(analysisResult));
            draft.setRetrievedTemplates(objectMapper.writeValueAsString(templates));
            draft.setGeneratedReply(generatedReply);
            draft.setStatus("pending_review");
            draft.setAiModel("deepseek-ai/DeepSeek-V4-Flash");
            draft.setTokenUsage(0);
            draft.setCreateTime(LocalDateTime.now());

            draftMapper.insert(draft);
            
            log.info("=== 评价回复草稿生成成功，draftId: {}, reviewId: {} ===", draft.getId(), reviewId);

        } catch (Exception e) {
            log.error("=== 生成评价回复草稿失败，reviewId: {} ===", reviewId, e);
            
            // 降级策略：使用规则生成简单回复
            try {
                generateFallbackDraft(reviewId, reviewContent, rating);
            } catch (Exception ex) {
                log.error("=== 降级策略也失败了，reviewId: {} ===", reviewId, ex);
            }
        }
    }

    @Override
    public void approveAndPublish(Long draftId) {
        ReviewReplyDraft draft = draftMapper.getById(draftId);
        if (draft == null) {
            throw new RuntimeException("草稿不存在");
        }

        // 更新评价表的回复内容
        Long reviewId = getReviewIdByDraftId(draftId);
        reviewMapper.replyReview(reviewId, draft.getGeneratedReply(), LocalDateTime.now());

        // 更新草稿状态
        draft.setStatus("approved");
        draft.setPublishTime(LocalDateTime.now());
        draftMapper.update(draft);

        // 【新增】将高质量回复写入 RAG 知识库
        addToRagLibrary(reviewId, draft.getReviewContent(), draft.getGeneratedReply());

        log.info("评价回复已发布并同步至 RAG 库，draftId: {}", draftId);
    }

    @Override
    public void rejectAndRegenerate(Long draftId, String reason) {
        ReviewReplyDraft draft = draftMapper.getById(draftId);
        if (draft == null) {
            throw new RuntimeException("草稿不存在");
        }

        // 更新草稿状态
        draft.setStatus("rejected");
        draftMapper.update(draft);

        // 从 review 表查询真实的 rating
        Review review = reviewMapper.getById(draft.getReviewId());
        if (review == null) {
            log.error("评价不存在，无法重新生成，reviewId: {}", draft.getReviewId());
            return;
        }

        // 重新生成
        generateReplyDraft(draft.getReviewId(), draft.getReviewContent(), review.getRating());
        
        log.info("草稿已拒绝并重新生成，draftId: {}, 原因: {}", draftId, reason);
    }

    @Override
    public List<ReviewReplyDraft> listPendingDrafts() {
        return draftMapper.listPendingReview();
    }

    /**
     * AI 分析评价情感和意图
     */
    private Map<String, String> analyzeReview(String content, Integer rating) {
        String prompt = String.format(
            "你是一个评价分析助手。请分析以下用户评价的情绪等级、问题类型、核心诉求。\n\n" +
            "【评价内容】%s\n" +
            "【评分】%d星\n\n" +
            "【要求】\n" +
            "1. emotion: 愤怒/满意/中性\n" +
            "2. type: 配送超时/口味问题/服务态度/菜品质量/其他\n" +
            "3. demand: 退款/改进/道歉/其他\n" +
            "4. 输出严格的 JSON 格式：{\"emotion\": \"\", \"type\": \"\", \"demand\": \"\"}\n\n" +
            "JSON输出：",
            content, rating
        );

        String response = aiCallUtil.callChatModel(prompt);
        response = response.replaceAll("```json", "").replaceAll("```", "").trim();

        try {
            return objectMapper.readValue(response, Map.class);
        } catch (Exception e) {
            log.warn("AI 分析格式错误，使用默认值", e);
            Map<String, String> defaultResult = new HashMap<>();
            defaultResult.put("emotion", rating <= 2 ? "愤怒" : "中性");
            defaultResult.put("type", "其他");
            defaultResult.put("demand", "其他");
            return defaultResult;
        }
    }

    /**
     * RAG 检索相似回复（基于 Simple Vector Store）
     */
    private List<ReviewReplyKnowledge> retrieveTemplates(String problemType) {
        try {
            // 1. 执行相似度搜索（Spring AI 会自动将字符串转为向量）
            List<Document> results = vectorStore.similaritySearch(problemType);
            
            // 限制返回数量
            if (results.size() > 3) {
                results = results.subList(0, 3);
            }

            // 2. 将 Document 转换为 ReviewReplyKnowledge 对象
            return results.stream().map(doc -> {
                ReviewReplyKnowledge k = new ReviewReplyKnowledge();
                k.setReplyTemplate(doc.getText());
                k.setCategory("historical_reply");
                return k;
            }).collect(Collectors.toList());

        } catch (Exception e) {
            log.warn("RAG 向量检索失败，降级为 SQL 检索", e);
            return fallbackSqlRetrieve(problemType);
        }
    }

    /**
     * 降级策略：传统 SQL 检索
     */
    private List<ReviewReplyKnowledge> fallbackSqlRetrieve(String problemType) {
        List<ReviewReplyKnowledge> templates = knowledgeMapper.listByCategory(problemType);
        if (templates == null || templates.isEmpty()) {
            templates = knowledgeMapper.listAll();
        }
        return templates.stream().limit(3).toList();
    }

    /**
     * AI 生成回复
     */
    private String generateReply(String reviewContent, String emotion, String problemType,
                                 String demand, List<ReviewReplyKnowledge> templates) {
        String templateStr = templates.isEmpty() ? "无参考模板" :
                templates.stream().map(ReviewReplyKnowledge::getReplyTemplate).reduce((a, b) -> a + "\n" + b).get();

        String prompt = String.format(
                "你是一个专业的客服助手。请根据以下信息生成一条礼貌、专业的回复。\n\n" +
                        "【用户评价】%s\n" +
                        "【情绪分析】%s\n" +
                        "【问题类型】%s\n" +
                        "【用户诉求】%s\n\n" +
                        "【参考模板】\n%s\n\n" +
                        "【要求】\n" +
                        "1. 语气诚恳、专业\n" +
                        "2. 针对用户提到的具体问题回应\n" +
                        "3. 如果有合理诉求，给出解决方案\n" +
                        "4. 控制在 100 字以内\n\n" +
                        "回复内容：",
                reviewContent, emotion, problemType, demand, templateStr
        );

        return aiCallUtil.callChatModel(prompt);
    }

    /**
     * 降级策略：规则生成简单回复
     */
    private void generateFallbackDraft(Long reviewId, String content, Integer rating) {
        String fallbackReply;

        if (rating <= 2) {
            fallbackReply = "非常抱歉给您带来不好的体验，我们会认真反思并改进。如有任何问题，欢迎联系客服处理。";
        } else if (rating == 3) {
            fallbackReply = "感谢您的反馈，我们会继续努力提升服务质量，期待您的再次光临！";
        } else {
            fallbackReply = "感谢您的好评！我们会继续保持，期待再次为您服务～";
        }

        // 【修复】降级策略也需要设置 orderId
        Review review = reviewMapper.getById(reviewId);
        if (review == null) {
            log.error("评价不存在，无法生成降级草稿，reviewId: {}", reviewId);
            return;
        }

        ReviewReplyDraft draft = new ReviewReplyDraft();
        draft.setOrderId(review.getOrderId());  // ← 关键修复
        draft.setReviewId(reviewId);
        draft.setReviewContent(content);
        draft.setGeneratedReply(fallbackReply);
        draft.setStatus("pending_review");
        draft.setAiModel("rule-based");
        draft.setCreateTime(LocalDateTime.now());

        draftMapper.insert(draft);
        log.info("使用降级策略生成草稿成功，reviewId: {}", reviewId);
    }

    /**
     * 根据草稿ID查询评价ID
     */
    private Long getReviewIdByDraftId(Long draftId) {
        ReviewReplyDraft draft = draftMapper.getById(draftId);
        return draft != null ? draft.getReviewId() : null;
    }

    /**
     * 将高质量回复写入 RAG 知识库
     */
    private void addToRagLibrary(Long reviewId, String reviewContent, String replyContent) {
        try {
            // 1. 构造元数据
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("reviewId", reviewId);
            metadata.put("type", "merchant_reply");
            metadata.put("timestamp", LocalDateTime.now().toString());

            // 2. 生成向量（将评价和回复拼接）
            String textToEmbed = reviewContent + " [回复] " + replyContent;

            // 3. 创建 Document 并存入向量库
            Document document = new Document(
                    "reply_" + reviewId,
                    textToEmbed,
                    metadata
            );

            vectorStore.add(List.of(document));
            log.info("回复已存入 RAG 库，reviewId: {}", reviewId);
        } catch (Exception e) {
            log.error("存入 RAG 库失败，reviewId: {}", reviewId, e);
        }
    }
}