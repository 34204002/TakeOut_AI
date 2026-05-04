package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

/**
 * 订单备注解析结果VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRemarkVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 口味标签列表
     */
    private List<String> flavorTags;

    /**
     * 配送标签列表
     */
    private List<String> deliveryTags;

    /**
     * 紧急程度（0普通 1加急 2特急）
     */
    private Integer urgencyLevel;

    /**
     * 原始备注
     */
    private String originalRemark;
}
