package com.sky.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单备注标签实体
 */
@Data
public class OrderRemarkTag implements Serializable {
    
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
     * 口味标签（JSON数组）["不辣","多醋","不要葱"]
     */
    private String flavorTags;
    
    /**
     * 配送标签（JSON数组）["放门卫","打电话"]
     */
    private String deliveryTags;
    
    /**
     * 紧急程度（0普通 1加急 2特急）
     */
    private Integer urgencyLevel;
    
    /**
     * 原始备注文本
     */
    private String originalRemark;
    
    /**
     * 解析状态（1成功 2失败 3人工审核）
     */
    private Integer parseStatus;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
