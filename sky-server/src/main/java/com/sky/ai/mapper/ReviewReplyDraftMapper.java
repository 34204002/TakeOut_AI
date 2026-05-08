package com.sky.ai.mapper;

import com.sky.entity.ReviewReplyDraft;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 差评回复草稿Mapper
 */
@Mapper
public interface ReviewReplyDraftMapper {
    
    /**
     * 根据订单ID查询草稿
     *
     * @param orderId 订单ID
     * @return 回复草稿
     */
    @Select("SELECT * FROM sky_take_out.review_reply_draft WHERE order_id = #{orderId}")
    ReviewReplyDraft getByOrderId(Long orderId);
    
    /**
     * 查询待审核的草稿列表
     *
     * @return 草稿列表
     */
    @Select("SELECT * FROM sky_take_out.review_reply_draft WHERE status = 'pending_review' ORDER BY create_time DESC")
    List<ReviewReplyDraft> listPendingReview();
    
    /**
     * 插入草稿
     *
     * @param draft 回复草稿
     */
    void insert(ReviewReplyDraft draft);
    
    /**
     * 更新草稿
     *
     * @param draft 回复草稿
     */
    void update(ReviewReplyDraft draft);
    
    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 回复草稿
     */
    @Select("SELECT * FROM sky_take_out.review_reply_draft WHERE id = #{id}")
    ReviewReplyDraft getById(Long id);
}
