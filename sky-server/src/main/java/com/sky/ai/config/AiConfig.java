package com.sky.ai.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AiConfig {

    @Bean("statelessChatClient")
    public ChatClient statelessChatClient(ChatModel chatModel) {
        log.info("初始化无状态 ChatClient");
        
        return ChatClient.builder(chatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @Bean("memoryChatClient")
    public ChatClient memoryChatClient(ChatModel chatModel) {
        log.info("初始化带会话记忆的 ChatClient，使用文件系统持久化");
        
        ChatMemoryRepository chatMemoryRepository = new FileChatMemoryRepository();
        
        MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(50)
                .build();

        return ChatClient.builder(chatModel)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(messageWindowChatMemory).build(),
                        new SimpleLoggerAdvisor()
                )
                .build();
    }
}
