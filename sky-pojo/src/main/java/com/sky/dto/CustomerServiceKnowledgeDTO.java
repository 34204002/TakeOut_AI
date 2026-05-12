package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 客服知识库DTO
 */
@Data
public class CustomerServiceKnowledgeDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID（更新时必填）
     */
    private Long id;
    
    /**
     * 问题分类
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
}
