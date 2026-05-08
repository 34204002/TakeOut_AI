
package com.sky.ai.service;

/**
 * 评价智能回复服务接口
 */
public interface ReviewReplyService {

    /**
     * 生成评价回复草稿
     *
     * @param reviewId 评价ID
     * @param reviewContent 评价内容
     * @param rating 评分
     */
    void generateReplyDraft(Long reviewId, String reviewContent, Integer rating);

    /**
     * 审核通过并发布回复
     *
     * @param draftId 草稿ID
     */
    void approveAndPublish(Long draftId);

    /**
     * 拒绝草稿并重新生成
     *
     * @param draftId 草稿ID
     * @param reason 拒绝原因
     */
    void rejectAndRegenerate(Long draftId, String reason);

    /**
     * 获取待审核的回复草稿列表
     *
     * @return 草稿列表
     */
    java.util.List<com.sky.entity.ReviewReplyDraft> listPendingDrafts();
}
