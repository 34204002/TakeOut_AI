
package com.sky.controller.user;

import com.sky.dto.ReviewSubmitDTO;
import com.sky.result.Result;
import com.sky.service.ReviewService;
import com.sky.vo.ReviewVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户端评价控制器
 */
@RestController("userReviewController")
@RequestMapping("/user/review")
@Slf4j
@Tag(name = "用户评价接口")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * 提交评价
     *
     * @param reviewSubmitDTO 评价数据
     * @return 操作结果
     */
    @PostMapping("/submit")
    @Operation(summary = "提交评价")
    public Result<String> submitReview(@RequestBody ReviewSubmitDTO reviewSubmitDTO) {
        log.info("用户提交评价: {}", reviewSubmitDTO);
        reviewService.submitReview(reviewSubmitDTO);
        return Result.success("评价提交成功");
    }

    /**
     * 查询店铺最新评价
     *
     * @param limit 数量限制（默认10条）
     * @return 评价列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询店铺最新评价")
    public Result<List<ReviewVO>> listLatestReviews(
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        log.info("查询店铺最新评价，数量: {}", limit);
        List<ReviewVO> reviews = reviewService.listLatestReviews(limit);
        return Result.success(reviews);
    }
}
