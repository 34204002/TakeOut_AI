package com.sky.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 差评回复草稿实体
 */
@Data
public class ReviewReplyDraft implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 订单ID
     */
    private Long orderId;
    
    /**
     * 评价ID
     */
    private Long reviewId;
    
    /**
     * 用户差评原文
     */
    private String reviewContent;
    
    /**
     * AI分析结果（JSON）
     */
    private String aiAnalysis;
    
    /**
     * RAG召回的模板列表（JSON）
     */
    private String retrievedTemplates;
    
    /**
     * AI生成的回复内容
     */
    private String generatedReply;
    
    /**
     * 状态（pending_review/approved/rejected/modified）
     */
    private String status;
    
    /**
     * 商家修改后的回复
     */
    private String merchantModifiedReply;
    
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    
    /**
     * 使用的AI模型
     */
    private String aiModel;
    
    /**
     * Token消耗量
     */
    private Integer tokenUsage;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 创建人
     */
    private Long createUser;
    
    /**
     * 更新人
     */
    private Long updateUser;
}
