package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 营销文案案例 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingCaseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 菜品分类
     */
    private String dishCategory;

    /**
     * 活动类型
     */
    private String activityType;

    /**
     * 投放渠道
     */
    private String channel;

    /**
     * 文案风格
     */
    private String style;

    /**
     * 案例标题
     */
    private String caseTitle;

    /**
     * 文案内容
     */
    private String caseContent;

    /**
     * 字数
     */
    private Integer characterCount;

    /**
     * 是否包含emoji（0否 1是）
     */
    private Integer hasEmoji;

    /**
     * 效果评分（0-5）
     */
    private BigDecimal performanceScore;

    /**
     * 使用次数
     */
    private Integer usageCount;

    /**
     * 转化率
     */
    private BigDecimal conversionRate;

    /**
     * 标签（逗号分隔）
     */
    private String tags;
}
