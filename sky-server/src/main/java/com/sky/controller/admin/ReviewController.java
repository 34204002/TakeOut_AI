
package com.sky.controller.admin;

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
 * 管理端评价控制器
 */
@RestController("adminReviewController")
@RequestMapping("/admin/review")
@Slf4j
@Tag(name = "管理端评价接口")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * 查询所有评价
     *
     * @return 评价列表
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有评价")
    public Result<List<ReviewVO>> listAllReviews() {
        log.info("管理端查询所有评价");
        List<ReviewVO> reviews = reviewService.listAllReviews();
        return Result.success(reviews);
    }

    /**
     * 回复评价
     *
     * @param id 评价ID
     * @param replyContent 回复内容
     * @return 操作结果
     */
    @PostMapping("/reply/{id}")
    @Operation(summary = "回复评价")
    public Result<String> replyReview(@PathVariable Long id, @RequestParam String replyContent) {
        log.info("商家回复评价，评价ID: {}", id);
        reviewService.replyReview(id, replyContent);
        return Result.success("回复成功");
    }

    /**
     * 隐藏评价
     *
     * @param id 评价ID
     * @return 操作结果
     */
    @PutMapping("/hide/{id}")
    @Operation(summary = "隐藏评价")
    public Result<String> hideReview(@PathVariable Long id) {
        log.info("商家隐藏评价，评价ID: {}", id);
        reviewService.hideReview(id);
        return Result.success("评价已隐藏");
    }
}
