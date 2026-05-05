
package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ReviewSubmitDTO;
import com.sky.entity.Orders;
import com.sky.entity.Review;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReviewMapper;
import com.sky.service.ReviewService;
import com.sky.vo.ReviewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 店铺评价服务实现类
 */
@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void submitReview(ReviewSubmitDTO reviewSubmitDTO) {
        Long userId = BaseContext.getCurrentId();
        
        // 验证订单是否存在且属于当前用户
        Orders order = orderMapper.getById(reviewSubmitDTO.getOrderId());
        if (order == null || !order.getUserId().equals(userId)) {
            throw new RuntimeException("订单不存在");
        }

        // 验证订单是否已完成
        if (!Orders.COMPLETED.equals(order.getStatus())) {
            throw new RuntimeException("只能对已完成的订单进行评价");
        }

        // 检查是否已经评价过
        Review existingReview = reviewMapper.getByOrderId(reviewSubmitDTO.getOrderId());
        if (existingReview != null) {
            throw new RuntimeException("该订单已评价");
        }

        // 创建评价
        Review review = Review.builder()
                .orderId(reviewSubmitDTO.getOrderId())
                .userId(userId)
                .rating(reviewSubmitDTO.getRating())
                .content(reviewSubmitDTO.getContent())
                .status(1) // 直接发布
                .createTime(LocalDateTime.now())
                .build();

        reviewMapper.insert(review);
        log.info("用户提交评价成功，订单ID: {}, 用户ID: {}", reviewSubmitDTO.getOrderId(), userId);
    }

    @Override
    public List<ReviewVO> listLatestReviews(Integer limit) {
        List<Review> reviews = reviewMapper.listLatestReviews(limit);
        return convertToVOList(reviews);
    }

    @Override
    public List<ReviewVO> listAllReviews() {
        List<Review> reviews = reviewMapper.listAll();
        return convertToVOList(reviews);
    }

    @Override
    public void replyReview(Long id, String replyContent) {
        Review review = reviewMapper.getById(id);
        if (review == null) {
            throw new RuntimeException("评价不存在");
        }

        reviewMapper.replyReview(id, replyContent, LocalDateTime.now());
        log.info("商家回复评价成功，评价ID: {}", id);
    }

    @Override
    public void hideReview(Long id) {
        Review review = reviewMapper.getById(id);
        if (review == null) {
            throw new RuntimeException("评价不存在");
        }

        reviewMapper.updateStatus(id, 2); // 2表示已隐藏
        log.info("商家隐藏评价成功，评价ID: {}", id);
    }

    /**
     * 转换为 VO 列表
     */
    private List<ReviewVO> convertToVOList(List<Review> reviews) {
        List<ReviewVO> voList = new ArrayList<>();
        for (Review review : reviews) {
            ReviewVO vo = ReviewVO.builder()
                    .id(review.getId())
                    .orderId(review.getOrderId())
                    .userId(review.getUserId())
                    .userName(maskUserName(review.getUserName()))
                    .rating(review.getRating())
                    .content(review.getContent())
                    .replyContent(review.getReplyContent())
                    .replyTime(review.getReplyTime())
                    .createTime(review.getCreateTime())
                    .build();
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 用户名脱敏处理
     */
    private String maskUserName(String userName) {
        if (userName == null || userName.length() <= 1) {
            return userName;
        }
        return userName.charAt(0) + "**";
    }
}
