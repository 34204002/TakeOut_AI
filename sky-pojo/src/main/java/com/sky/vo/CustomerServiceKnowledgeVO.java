package com.sky.vo;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客服知识库VO
 */
@Data
public class CustomerServiceKnowledgeVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 问题分类
     */
    private String category;
    
    /**
     * 问题关键词
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
     * 排序权重
     */
    private Integer sortWeight;
    
    /**
     * 状态（0禁用 1启用）
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
