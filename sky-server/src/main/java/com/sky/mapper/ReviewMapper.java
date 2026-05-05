package com.sky.mapper;

import com.sky.entity.Review;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 店铺评价 Mapper
 */
@Mapper
public interface ReviewMapper {

    /**
     * 插入评价
     *
     * @param review 评价对象
     */
    @Insert("INSERT INTO sky_take_out.review (order_id, user_id, rating, content, status, create_time) " +
            "VALUES (#{orderId}, #{userId}, #{rating}, #{content}, #{status}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Review review);

    /**
     * 根据订单ID查询评价
     *
     * @param orderId 订单ID
     * @return 评价对象
     */
    @Select("SELECT * FROM sky_take_out.review WHERE order_id = #{orderId}")
    Review getByOrderId(Long orderId);

    /**
     * 查询店铺最新的评价列表（已发布的）
     *
     * @param limit 数量限制
     * @return 评价列表
     */
    @Select("SELECT r.*, u.name as user_name FROM sky_take_out.review r " +
            "LEFT JOIN sky_take_out.user u ON r.user_id = u.id " +
            "WHERE r.status = 1 ORDER BY r.create_time DESC LIMIT #{limit}")
    List<Review> listLatestReviews(@Param("limit") Integer limit);

    /**
     * 查询所有评价（管理端，支持分页）
     *
     * @return 评价列表
     */
    @Select("SELECT r.*, u.name as user_name FROM sky_take_out.review r " +
            "LEFT JOIN sky_take_out.user u ON r.user_id = u.id " +
            "ORDER BY r.create_time DESC")
    List<Review> listAll();

    /**
     * 更新评价状态
     *
     * @param id 评价ID
     * @param status 状态
     */
    @Update("UPDATE sky_take_out.review SET status = #{status} WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 商家回复评价
     *
     * @param id 评价ID
     * @param replyContent 回复内容
     * @param replyTime 回复时间
     */
    @Update("UPDATE sky_take_out.review SET reply_content = #{replyContent}, reply_time = #{replyTime} WHERE id = #{id}")
    void replyReview(@Param("id") Long id, @Param("replyContent") String replyContent, @Param("replyTime") java.time.LocalDateTime replyTime);

    /**
     * 根据ID查询评价
     *
     * @param id 评价ID
     * @return 评价对象
     */
    @Select("SELECT r.*, u.name as user_name FROM sky_take_out.review r " +
            "LEFT JOIN sky_take_out.user u ON r.user_id = u.id " +
            "WHERE r.id = #{id}")
    Review getById(Long id);
}
