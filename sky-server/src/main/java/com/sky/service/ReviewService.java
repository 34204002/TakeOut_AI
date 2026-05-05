
package com.sky.service;

import com.sky.dto.ReviewSubmitDTO;
import com.sky.vo.ReviewVO;

import java.util.List;

/**
 * 店铺评价服务接口
 */
public interface ReviewService {

    /**
     * 用户提交评价
     *
     * @param reviewSubmitDTO 评价提交数据
     */
    void submitReview(ReviewSubmitDTO reviewSubmitDTO);

    /**
     * 查询店铺最新评价列表
     *
     * @param limit 数量限制
     * @return 评价列表
     */
    List<ReviewVO> listLatestReviews(Integer limit);

    /**
     * 查询所有评价（管理端）
     *
     * @return 评价列表
     */
    List<ReviewVO> listAllReviews();

    /**
     * 商家回复评价
     *
     * @param id 评价ID
     * @param replyContent 回复内容
     */
    void replyReview(Long id, String replyContent);

    /**
     * 隐藏评价（管理端）
     *
     * @param id 评价ID
     */
    void hideReview(Long id);
}
