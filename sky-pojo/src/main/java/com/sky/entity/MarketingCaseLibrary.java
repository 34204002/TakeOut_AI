package com.sky.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 营销案例库实体
 */
@Data
public class MarketingCaseLibrary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 菜品分类（川菜/粤菜/快餐/套餐/饮品等）
     */
    private String dishCategory;

    /**
     * 活动类型（满减/折扣/新品上市/限时秒杀/组合套餐）
     */
    private String activityType;

    /**
     * 投放渠道（微信小程序/朋友圈/短信/抖音）
     */
    private String channel;

    /**
     * 文案风格（活泼/温馨/专业/紧迫感）
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
     * 合规注意事项
     */
    private String complianceNotes;

    /**
     * 标签（逗号分隔）
     */
    private String tags;

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
