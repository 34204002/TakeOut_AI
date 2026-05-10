package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 营销文案生成请求 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingGenerateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜品ID
     */
    private Long dishId;

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
}
