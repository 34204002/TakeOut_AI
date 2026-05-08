
package com.sky.ai.controller.admin;

import com.sky.entity.ReviewReplyDraft;
import com.sky.result.Result;
import com.sky.ai.service.ReviewReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端评价回复控制器
 */
@RestController("adminReviewReplyController")
@RequestMapping("/admin/review/reply")
@Slf4j
@Tag(name = "评价智能回复接口")
public class ReviewReplyController {

    @Autowired
    private ReviewReplyService reviewReplyService;

    /**
     * 获取待审核的回复草稿列表
     *
     * @return 草稿列表
     */
    @GetMapping("/pending")
    @Operation(summary = "获取待审核回复草稿")
    public Result<List<ReviewReplyDraft>> listPendingDrafts() {
        log.info("查询待审核回复草稿");
        List<ReviewReplyDraft> drafts = reviewReplyService.listPendingDrafts();
        return Result.success(drafts);
    }

    /**
     * 审核通过并发布回复
     *
     * @param draftId 草稿ID
     * @return 操作结果
     */
    @PostMapping("/approve/{draftId}")
    @Operation(summary = "审核通过并发布")
    public Result<String> approveDraft(@PathVariable Long draftId) {
        log.info("审核通过回复草稿，draftId: {}", draftId);
        reviewReplyService.approveAndPublish(draftId);
        return Result.success("回复已发布");
    }

    /**
     * 拒绝草稿并重新生成
     *
     * @param draftId 草稿ID
     * @param reason 拒绝原因
     * @return 操作结果
     */
    @PostMapping("/reject/{draftId}")
    @Operation(summary = "拒绝并重新生成")
    public Result<String> rejectDraft(@PathVariable Long draftId, 
                                       @RequestParam String reason) {
        log.info("拒绝回复草稿，draftId: {}, 原因: {}", draftId, reason);
        reviewReplyService.rejectAndRegenerate(draftId, reason);
        return Result.success("已重新生成回复");
    }
}
