
package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 评价提交 DTO
 */
@Data
public class ReviewSubmitDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 评分（1-5星）
     */
    private Integer rating;

    /**
     * 评价内容
     */
    private String content;
}
