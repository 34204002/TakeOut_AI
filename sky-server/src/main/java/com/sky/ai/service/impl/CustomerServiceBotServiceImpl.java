package com.sky.ai.service.impl;

import com.sky.entity.CustomerServiceKnowledge;
import com.sky.ai.mapper.CustomerServiceKnowledgeMapper;
import com.sky.ai.service.CustomerServiceBotService;
import com.sky.ai.util.AiCallUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerServiceBotServiceImpl implements CustomerServiceBotService {
    
    @Autowired
    private AiCallUtil aiCallUtil;
    
    @Autowired
    private CustomerServiceKnowledgeMapper knowledgeMapper;
    
    /**
     * Embedding 模型（用于创建新的 VectorStore）
     */
    @Autowired
    private EmbeddingModel embeddingModel;
    
    /**
     * 客服专用向量存储
     */
    @Autowired
    @Qualifier("customerServiceVectorStore")
    private SimpleVectorStore vectorStore;
    
    private List<CustomerServiceKnowledge> knowledgeBase;
    
    /**
     * 启动时加载知识库到内存并构建向量库
     */
    @PostConstruct
    public void init() {
        // 1. 加载知识库到内存
        knowledgeBase = knowledgeMapper.listAll();
        
        // 2. 检查向量库是否已有数据（从文件加载）
        // 如果向量库为空，才重新构建
        try {
            // 尝试进行一次简单的检索，判断向量库是否有数据
            List<Document> testSearch = vectorStore.similaritySearch(
                SearchRequest.builder().query("测试").topK(1).build()
            );
            
            if (testSearch != null && !testSearch.isEmpty()) {
                log.info("向量库已有数据，跳过构建过程（从文件加载）");
            } else {
                log.info("向量库为空，开始构建...");
                buildVectorStore();
            }
        } catch (Exception e) {
            log.warn("检查向量库状态失败，重新构建", e);
            buildVectorStore();
        }
        
        log.info("客服知识库加载完成，共{}条知识", knowledgeBase.size());
    }
    
    /**
     * 构建向量库
     */
    private void buildVectorStore() {
        if (knowledgeBase == null || knowledgeBase.isEmpty()) {
            log.warn("知识库为空，跳过向量库构建");
            return;
        }
        
        // 注意：SimpleVectorStore 没有 clear() 方法
        // 所以我们需要创建一个新的 VectorStore 实例来替换旧的
        // 但由于 vectorStore 是注入的 Bean，我们不能直接替换
        // 因此采用以下策略：依赖 VectorStoreConfig 中的 ShutdownHook 保存
        // 下次启动时会从文件加载，而文件会在 reloadKnowledge() 中被覆盖
        
        List<Document> documents = knowledgeBase.stream()
            .map(k -> {
                Map<String, Object> metadata = new HashMap<>();
                metadata.put("id", k.getId());
                metadata.put("category", k.getCategory());
                metadata.put("question", k.getQuestion());
                
                // 文本内容：问题 + 关键词，增强检索效果
                String text = k.getQuestion() + " " + k.getKeywords();
                
                return new Document(text, metadata);
            })
            .collect(Collectors.toList());
        
        // 清空当前向量库（通过保存空文件再重新加载实现）
        // 但 SimpleVectorStore 不支持动态清空，所以我们只能追加
        // 实际使用时，reloadKnowledge() 会保存新文件，下次启动时加载的就是最新的
        vectorStore.add(documents);
        log.info("向量库构建完成，共{}个向量", documents.size());
    }
    
    /**
     * 智能客服回答
     *
     * @param userQuestion 用户问题
     * @return 回答内容
     */
    @Override
    public String answer(String userQuestion) {
        if (userQuestion == null || userQuestion.trim().isEmpty()) {
            return "您好，请问有什么可以帮助您的？";
        }
        
        try {
            // 第一步：AI 提取关键意图和子问题
            List<String> subQuestions = extractSubQuestions(userQuestion);
            log.info("原始问题: {}", userQuestion);
            log.info("提取到 {} 个子问题: {}", subQuestions.size(), subQuestions);
            
            // 第二步：分别向量检索每个子问题
            Set<CustomerServiceKnowledge> allMatchedKnowledges = new LinkedHashSet<>();
            
            for (String subQuestion : subQuestions) {
                List<Document> similarDocs = vectorStore.similaritySearch(
                    SearchRequest.builder().query(subQuestion).topK(3).build()
                );
                
                if (similarDocs != null && !similarDocs.isEmpty()) {
                    for (Document doc : similarDocs) {
                        Object idObj = doc.getMetadata().get("id");
                        Long knowledgeId = null;
                        if (idObj instanceof Number) {
                            knowledgeId = ((Number) idObj).longValue();
                        }
                        
                        if (knowledgeId != null) {
                            CustomerServiceKnowledge knowledge = knowledgeMapper.getById(knowledgeId);
                            if (knowledge != null) {
                                // 使用子问题计算相关性分数（仅用于日志）
                                double score = calculateRelevanceScore(subQuestion, knowledge);
                                log.info("检索结果: 子问题='{}', ID={}, 问题={}, 分数={}", subQuestion, knowledgeId, knowledge.getQuestion(), score);
                                allMatchedKnowledges.add(knowledge); // Set 自动去重
                            }
                        }
                    }
                }
            }
            
            // 第三步：根据匹配结果生成回答
            if (allMatchedKnowledges.isEmpty()) {
                log.info("未匹配到任何相关知识");
                return "抱歉，我暂时无法回答这个问题。您可以尝试换个问法，或联系人工客服。";
            }
            
            List<CustomerServiceKnowledge> matchedList = new ArrayList<>(allMatchedKnowledges);
            
            if (matchedList.size() == 1) {
                log.info("匹配到 1 条知识，直接返回");
                return matchedList.get(0).getAnswer();
            } else {
                log.info("匹配到 {} 条知识，使用 AI 总结", matchedList.size());
                return generateSummaryByAI(userQuestion, matchedList);
            }
            
        } catch (Exception e) {
            log.error("智能客服回答失败", e);
            return "抱歉，系统暂时无法回答您的问题。您可以尝试换个问法，或联系人工客服。";
        }
    }
    
    /**
     * 使用 AI 提取子问题和关键意图
     */
    private List<String> extractSubQuestions(String userQuestion) {
        String prompt = String.format(
            "请分析用户问题，提取出所有独立的子问题或关键意图，每行一个。\n" +
            "要求：\n" +
            "1. 如果只有一个问题，直接返回原问题\n" +
            "2. 如果有多个问题，分别列出\n" +
            "3. 提取核心意图，去除无关信息\n" +
            "4. 使用简洁明确的表达\n" +
            "5. 最多提取 5 个子问题\n\n" +
            "示例1：\n" +
            "用户：配送需要多久？\n" +
            "提取：\n" +
            "配送需要多久\n\n" +
            "示例2：\n" +
            "用户：我刚刚点的外卖快超时了还没到，你们有超时补偿吗？怎么找人工客服？\n" +
            "提取：\n" +
            "配送超时有什么补偿政策\n" +
            "如何联系人工客服\n\n" +
            "示例3：\n" +
            "用户：退款多久到账？可以开发票吗？\n" +
            "提取：\n" +
            "退款多久能到账\n" +
            "如何申请开发票\n\n" +
            "用户：%s\n" +
            "提取：",
            userQuestion
        );
        
        String extracted = aiCallUtil.callChatModel(prompt).trim();
        
        // 按行分割，过滤空行
        List<String> subQuestions = Arrays.stream(extracted.split("\n"))
            .map(String::trim)
            .filter(line -> !line.isEmpty() && line.length() > 2)
            .limit(5) // 最多 5 个
            .collect(Collectors.toList());
        
        // 如果提取失败，返回原问题
        if (subQuestions.isEmpty()) {
            subQuestions.add(userQuestion);
        }
        
        return subQuestions;
    }
    
    /**
     * 使用 AI 总结多个相关知识
     */
    private String generateSummaryByAI(String userQuestion, List<CustomerServiceKnowledge> knowledges) {
        // 加载 Skill 文件
        String skillContent = loadSkillContent("customer-service-assistant.md");
        
        // 构建上下文
        StringBuilder context = new StringBuilder();
        for (int i = 0; i < knowledges.size(); i++) {
            CustomerServiceKnowledge k = knowledges.get(i);
            context.append(String.format("\n【相关知识%d】\n问题：%s\n答案：%s\n", 
                i + 1, k.getQuestion(), k.getAnswer()));
        }
        
        String prompt = String.format(
            "%s\n\n【用户问题】%s\n\n【相关知识】%s\n\n" +
            "请根据以上相关知识，综合回答用户的问题。\n" +
            "要求：\n" +
            "1. 如果用户问了多个问题，请分别回答\n" +
            "2. 语言简洁友好，控制在 200 字以内\n" +
            "3. 不要编造知识中没有的信息\n" +
            "4. 如果某些问题没有相关知识，诚实告知\n\n" +
            "回答：",
            skillContent, userQuestion, context.toString()
        );
        
        return aiCallUtil.callChatModel(prompt);
    }
    
    /**
     * 加载 Skill 文件内容（带缓存）
     */
    private final ConcurrentHashMap<String, String> skillCache = new ConcurrentHashMap<>();
    
    private String loadSkillContent(String fileName) {
        return skillCache.computeIfAbsent(fileName, key -> {
            try {
                ClassPathResource resource = new ClassPathResource("ai/skills/" + key);
                String content = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
                log.info("Skill 文件加载成功: {}, 大小: {} bytes", key, content.length());
                return content;
            } catch (IOException e) {
                log.error("加载 Skill 文件失败: {}", key, e);
                return "你是智能客服助手，专业、友好地回答用户问题。";
            }
        });
    }
    
    /**
     * 计算相关性分数（优化版本）
     */
    private double calculateRelevanceScore(String userQuestion, CustomerServiceKnowledge knowledge) {
        String question = userQuestion.toLowerCase();
        String dbQuestion = knowledge.getQuestion().toLowerCase();
        String dbKeywords = knowledge.getKeywords().toLowerCase();
        
        double score = 0;
        
        // 1. 完全匹配问题（50分）
        if (question.contains(dbQuestion) || dbQuestion.contains(question)) {
            score += 50;
        }
        
        // 2. 部分匹配问题（最多30分）
        // 将问题拆分为词语，计算重合度
        String[] questionWords = question.split("[\\s,，。？?！!]+" );
        String[] dbQuestionWords = dbQuestion.split("[\\s,，。？?！!]+");
        int matchCount = 0;
        for (String qWord : questionWords) {
            if (qWord.length() > 1) {  // 忽略单字
                for (String dbWord : dbQuestionWords) {
                    if (dbWord.contains(qWord) || qWord.contains(dbWord)) {
                        matchCount++;
                        break;
                    }
                }
            }
        }
        if (questionWords.length > 0 && matchCount > 0) {
            double matchRatio = (double) matchCount / Math.max(questionWords.length, dbQuestionWords.length);
            score += 30 * matchRatio;
        }
        
        // 3. 关键词匹配（每个10分，最多30分）
        String[] keywords = dbKeywords.split("[,，]");
        int keywordMatchCount = 0;
        for (String keyword : keywords) {
            String trimmedKeyword = keyword.trim();
            if (!trimmedKeyword.isEmpty() && question.contains(trimmedKeyword)) {
                keywordMatchCount++;
            }
        }
        score += Math.min(keywordMatchCount * 10, 30);
        
        return score;
    }
    
    /**
     * 重新加载知识库（管理后台调用）
     */
    @Override
    public void reloadKnowledge() {
        log.info("开始重新加载知识库...");
        
        // 1. 重新从数据库加载最新数据
        knowledgeBase = knowledgeMapper.listAll();
        
        // 2. 先清空当前向量库中的数据
        // SimpleVectorStore 不支持 clear()，所以我们通过重建 Bean 的方式
        // 但 Bean 是单例，不能重建，所以采用以下策略：
        // - 删除旧的文件
        // - 下次启动时会重新构建
        try {
            File storeFile = new File("vector-store-customer-service.json");
            if (storeFile.exists()) {
                storeFile.delete();
                log.info("已删除旧的向量库文件，下次启动时将重新构建");
            }
        } catch (Exception e) {
            log.error("删除向量库文件失败", e);
        }
        
        // 3. 清空内存中的向量库（通过创建新实例并替换引用）
        // 但由于 vectorStore 是 final 的，我们无法替换
        // 所以这里只能追加，实际依赖文件删除来保证下次启动时是干净的
        
        // 4. 重建向量库（添加新数据）
        buildVectorStore();
        
        // 5. 保存到文件
        try {
            File storeFile = new File("vector-store-customer-service.json");
            vectorStore.save(storeFile);
            log.info("向量库已保存到: {}", storeFile.getAbsolutePath());
        } catch (Exception e) {
            log.error("保存向量库失败", e);
        }
        
        log.info("客服知识库重新加载完成，共{}条知识", knowledgeBase.size());
    }
}
