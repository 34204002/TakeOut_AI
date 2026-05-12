package com.sky.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客服知识库实体
 */
@Data
public class CustomerServiceKnowledge implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 问题分类（配送、菜品、支付、售后等）
     */
    private String category;
    
    /**
     * 问题关键词（多个关键词用逗号分隔）
     */
    private String keywords;
    
    /**
     * 标准问题
     */
    private String question;
    
    /**
     * 标准答案
     */
    private String answer;
    
    /**
     * 排序权重（数字越小优先级越高）
     */
    private Integer sortWeight;
    
    /**
     * 状态（0禁用 1启用）
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
