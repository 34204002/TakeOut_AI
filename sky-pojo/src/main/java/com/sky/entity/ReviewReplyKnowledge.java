package com.sky.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 差评回复知识库实体
 */
@Data
public class ReviewReplyKnowledge implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 问题分类（配送超时/菜品质量/服务态度/包装问题/其他）
     */
    private String category;
    
    /**
     * 子分类
     */
    private String subCategory;
    
    /**
     * 关键词（逗号分隔）
     */
    private String keywords;
    
    /**
     * 回复模板
     */
    private String replyTemplate;
    
    /**
     * 合规规则
     */
    private String complianceRules;
    
    /**
     * 适用情绪等级（轻微不满/愤怒/非常愤怒）
     */
    private String emotionLevel;
    
    /**
     * 补偿类型（退款/优惠券/积分/赠品）
     */
    private String compensationType;
    
    /**
     * 使用次数
     */
    private Integer useCount;
    
    /**
     * 效果评分（0-5）
     */
    private BigDecimal effectivenessScore;
    
    /**
     * 状态（0禁用 1启用）
     */
    private Integer status;
    
    /**
     * 创建人
     */
    private Long createUser;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新人
     */
    private Long updateUser;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
