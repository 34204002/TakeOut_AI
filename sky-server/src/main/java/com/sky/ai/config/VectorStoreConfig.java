package com.sky.ai.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * SimpleVectorStore 配置类
 * 提供基于文件的持久化向量存储
 * 为不同业务场景创建独立的向量存储实例
 */
@Configuration
@Slf4j
public class VectorStoreConfig {

    // 差评回复知识库持久化文件路径
    private static final String REVIEW_VECTOR_STORE_FILE = "vector-store-review.json";
    
    // 营销文案知识库持久化文件路径
    private static final String MARKETING_VECTOR_STORE_FILE = "vector-store-marketing.json";
    
    // 客服知识库持久化文件路径
    private static final String CUSTOMER_SERVICE_VECTOR_STORE_FILE = "vector-store-customer-service.json";


    @Autowired
    private EmbeddingModel embeddingModel;

    /**
     * 差评回复知识库向量存储
     * 用于存储客服话术、合规规则等差评回复相关知识
     *
     * @return 差评回复专用的 SimpleVectorStore 实例
     */
    @Bean("reviewVectorStore")
    public SimpleVectorStore reviewVectorStore() {
        log.info("初始化差评回复向量存储: {}", REVIEW_VECTOR_STORE_FILE);
        
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File storeFile = new File(REVIEW_VECTOR_STORE_FILE);
        
        // 启动时加载已有数据
        if (storeFile.exists()) {
            try {
                vectorStore.load(storeFile);
                log.info("差评回复向量数据加载成功: {}", storeFile.getAbsolutePath());
            } catch (Exception e) {
                log.error("加载差评回复向量数据失败", e);
            }
        } else {
            log.info("差评回复向量存储文件不存在，将创建新的向量库");
        }
        
        // 退出时自动保存数据
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                vectorStore.save(storeFile);
                log.info("差评回复向量数据已保存到: {}", storeFile.getAbsolutePath());
            } catch (Exception e) {
                log.error("保存差评回复向量数据失败", e);
            }
        }));
        
        return vectorStore;
    }

    /**
     * 营销文案知识库向量存储
     * 用于存储营销案例、文案模板等营销相关知识
     *
     * @return 营销文案专用的 SimpleVectorStore 实例
     */
    @Bean("marketingVectorStore")
    public SimpleVectorStore marketingVectorStore() {
        log.info("初始化营销文案向量存储: {}", MARKETING_VECTOR_STORE_FILE);
        
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File storeFile = new File(MARKETING_VECTOR_STORE_FILE);
        
        // 启动时加载已有数据
        if (storeFile.exists()) {
            try {
                vectorStore.load(storeFile);
                log.info("营销文案向量数据加载成功: {}", storeFile.getAbsolutePath());
            } catch (Exception e) {
                log.error("加载营销文案向量数据失败", e);
            }
        } else {
            log.info("营销文案向量存储文件不存在，将创建新的向量库");
        }
        
        // 退出时自动保存数据
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                vectorStore.save(storeFile);
                log.info("营销文案向量数据已保存到: {}", storeFile.getAbsolutePath());
            } catch (Exception e) {
                log.error("保存营销文案向量数据失败", e);
            }
        }));
        
        return vectorStore;
    }

    /**
     * 客服知识库向量存储
     * 用于存储客服问答知识库，支持智能客服自动回答
     *
     * @return 客服专用的 SimpleVectorStore 实例
     */
    @Bean("customerServiceVectorStore")
    public SimpleVectorStore customerServiceVectorStore() {
        log.info("初始化客服知识库向量存储: {}", CUSTOMER_SERVICE_VECTOR_STORE_FILE);
        
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File storeFile = new File(CUSTOMER_SERVICE_VECTOR_STORE_FILE);
        
        // 启动时加载已有数据
        if (storeFile.exists()) {
            try {
                vectorStore.load(storeFile);
                log.info("客服知识库向量数据加载成功: {}", storeFile.getAbsolutePath());
            } catch (Exception e) {
                log.error("加载客服知识库向量数据失败", e);
            }
        } else {
            log.info("客服知识库向量存储文件不存在，将创建新的向量库");
        }
        
        // 退出时自动保存数据
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                vectorStore.save(storeFile);
                log.info("客服知识库向量数据已保存到: {}", storeFile.getAbsolutePath());
            } catch (Exception e) {
                log.error("保存客服知识库向量数据失败", e);
            }
        }));
        
        return vectorStore;
    }
}
