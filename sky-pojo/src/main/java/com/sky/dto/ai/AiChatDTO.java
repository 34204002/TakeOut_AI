package com.sky.dto.ai;

import lombok.Data;

/**
 * AI对话请求DTO
 */
@Data
public class AiChatDTO {
    
    /**
     * 用户消息
     */
    private String message;
    
    /**
     * 系统提示词（可选）
     */
    private String system;
}
