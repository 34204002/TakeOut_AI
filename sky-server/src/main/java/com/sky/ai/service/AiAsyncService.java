package com.sky.ai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * AI 异步任务服务
 * 统一管理所有需要异步执行的 AI 相关任务
 */
@Service
@Slf4j
public class AiAsyncService {

    @Autowired
    private ReviewReplyService reviewReplyService;

    @Autowired
    private OrderRemarkService orderRemarkService;

    /**
     * 异步生成评价回复草稿
     *
     * @param reviewId 评价ID
     * @param reviewContent 评价内容
     * @param rating 评分
     */
    @Async
    public void generateReviewReplyAsync(Long reviewId, String reviewContent, Integer rating) {
        try {
            log.info("【异步任务开始】准备生成AI回复草稿，reviewId: {}", reviewId);
            long startTime = System.currentTimeMillis();
            
            reviewReplyService.generateReplyDraft(reviewId, reviewContent, rating);
            
            long endTime = System.currentTimeMillis();
            log.info("【异步任务完成】AI回复草稿生成成功，reviewId: {}, 耗时: {}ms", reviewId, (endTime - startTime));
        } catch (Exception e) {
            log.error("【异步任务失败】AI 生成评价回复草稿失败，reviewId: {}, 错误: {}", reviewId, e.getMessage(), e);
        }
    }

    /**
     * 异步解析订单备注
     *
     * @param orderId 订单ID
     * @param remark 订单备注内容
     */
    @Async
    public void parseOrderRemarkAsync(Long orderId, String remark) {
        try {
            log.info("【异步任务开始】准备解析订单备注，orderId: {}", orderId);
            long startTime = System.currentTimeMillis();
            
            orderRemarkService.parseAndSaveRemark(orderId, remark);
            
            long endTime = System.currentTimeMillis();
            log.info("【异步任务完成】订单备注解析成功，orderId: {}, 耗时: {}ms", orderId, (endTime - startTime));
        } catch (Exception e) {
            log.error("【异步任务失败】AI 备注解析失败，orderId: {}, 错误: {}", orderId, e.getMessage(), e);
        }
    }
}
