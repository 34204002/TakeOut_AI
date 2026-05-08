package com.sky.ai.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * AI 调用工具类
 * 封装所有直接调用 AI API 的操作，实现业务逻辑与 AI 调用的隔离
 * 
 * 注意：VectorStore 操作由各 Service 层直接注入对应的 Bean，不在此工具类中统一管理
 */
@Component
@Slf4j
public class AiCallUtil {

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private EmbeddingModel embeddingModel;

    /**
     * 调用大模型生成文本
     *
     * @param prompt 提示词
     * @return AI 生成的文本
     */
    public String callChatModel(String prompt) {
        log.info("【AI调用】开始请求 AI 服务..."); // <--- 加这行
        log.info("调用 ChatModel，prompt 长度: {}", prompt.length());
        try {
            String response = chatModel.call(prompt);
            log.info("【AI调用】成功收到响应！"); // <--- 加这行
            log.info("ChatModel 调用成功，响应长度: {}", response.length());
            return response;
        } catch (Exception e) {
            log.error("【AI调用】失败了", e);
            throw new RuntimeException("AI 服务调用失败: " + e.getMessage(), e);
        }
    }

    /**
     * 生成文本向量
     *
     * @param text 输入文本
     * @return 向量数组
     */
    public float[] generateEmbedding(String text) {
        log.debug("生成文本向量，文本长度: {}", text.length());
        try {
            float[] embedding = embeddingModel.embed(text);
            log.debug("向量生成成功，维度: {}", embedding.length);
            return embedding;
        } catch (Exception e) {
            log.error("向量生成失败", e);
            throw new RuntimeException("向量生成失败: " + e.getMessage(), e);
        }
    }



    /**
     * 解析 AI 返回的 JSON 格式响应
     *
     * @param response AI 原始响应
     * @return 解析后的 Map
     */
    public Map<String, Object> parseJsonResponse(String response) {
        try {
            // 清理 Markdown 代码块标记
            String cleaned = response.replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();
            
            // 这里可以使用 Jackson 或其他 JSON 库解析
            // 为了简化，暂时返回空 Map，实际使用时需要完善
            log.warn("JSON 解析功能待完善，原始响应: {}", cleaned);
            return Map.of();
        } catch (Exception e) {
            log.error("JSON 解析失败", e);
            return Map.of();
        }
    }
}
