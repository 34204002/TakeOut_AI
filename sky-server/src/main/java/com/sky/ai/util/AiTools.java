package com.sky.ai.util;

import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * AI 工具函数集合类
 * 集中管理所有通过 @Tool 注解暴露给大模型的函数
 */
@Component
@Slf4j
public class AiTools {

    @Autowired
    private ReportService reportService;

    /**
     * 工具函数1：查询营业额
     */
    @Tool(description = "查询指定日期范围内的营业额数据")
    public TurnoverReportVO getTurnover(
            @ToolParam(description = "开始日期，格式 yyyy-MM-dd") LocalDate startDate,
            @ToolParam(description = "结束日期，格式 yyyy-MM-dd") LocalDate endDate
    ) {
        log.info("【Tool调用】getTurnover, startDate: {}, endDate: {}", startDate, endDate);
        validateDateRange(startDate, endDate);
        return reportService.turnoverStatistics(startDate, endDate);
    }

    /**
     * 工具函数2：查询热销菜品
     */
    @Tool(description = "查询指定日期范围内销量最高的N个菜品")
    public SalesTop10ReportVO getTopSellingDishes(
            @ToolParam(description = "开始日期，格式 yyyy-MM-dd") LocalDate startDate,
            @ToolParam(description = "结束日期，格式 yyyy-MM-dd") LocalDate endDate,
            @ToolParam(description = "返回前N个菜品，默认10") Integer topN
    ) {
        log.info("【Tool调用】getTopSellingDishes, startDate: {}, endDate: {}, topN: {}", startDate, endDate, topN);
        validateDateRange(startDate, endDate);
        
        if (topN == null || topN <= 0) topN = 10;
        if (topN > 50) topN = 50;
        
        return reportService.top10(startDate, endDate);
    }

    /**
     * 工具函数3：查询用户统计数据
     */
    @Tool(description = "查询指定日期范围内的用户增长数据")
    public UserReportVO getUserStatistics(
            @ToolParam(description = "开始日期，格式 yyyy-MM-dd") LocalDate startDate,
            @ToolParam(description = "结束日期，格式 yyyy-MM-dd") LocalDate endDate
    ) {
        log.info("【Tool调用】getUserStatistics, startDate: {}, endDate: {}", startDate, endDate);
        validateDateRange(startDate, endDate);
        return reportService.userStatistics(startDate, endDate);
    }

    /**
     * 工具函数4：查询订单统计数据
     */
    @Tool(description = "查询指定日期范围内的订单统计数据")
    public OrderReportVO getOrderStatistics(
            @ToolParam(description = "开始日期，格式 yyyy-MM-dd") LocalDate startDate,
            @ToolParam(description = "结束日期，格式 yyyy-MM-dd") LocalDate endDate
    ) {
        log.info("【Tool调用】getOrderStatistics, startDate: {}, endDate: {}", startDate, endDate);
        validateDateRange(startDate, endDate);
        return reportService.ordersStatistics(startDate, endDate);
    }

    /**
     * 安全校验：限制查询时间范围
     */
    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("开始日期不能晚于结束日期");
        }
        // 防止大模型查询过久的数据导致性能问题
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days > 365) {
            throw new IllegalArgumentException("查询时间范围不能超过1年");
        }
    }
}
