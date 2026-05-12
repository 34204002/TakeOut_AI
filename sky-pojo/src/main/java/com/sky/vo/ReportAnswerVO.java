package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 经营数据自然语言查询响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportAnswerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * AI 生成的口语化回答
     * 例如："您上个月的营业额是 45,800 元，比上月增长了 10%。"
     */
    private String answer;

    /**
     * 结构化数据（用于前端图表展示）
     * 可能是 TurnoverReportVO、List<GoodsSalesDTO> 等
     */
    private Object data;

    /**
     * 数据类型标识
     * turnover / dish_sales / user_statistics / order_statistics
     */
    private String dataType;

    /**
     * 调用的工具函数名称
     */
    private String calledFunction;

    /**
     * Token 消耗量
     */
    private Integer tokenUsage;
}
