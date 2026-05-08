package com.sky.ai.mapper;

import com.sky.entity.OrderRemarkTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 订单备注标签Mapper
 */
@Mapper
public interface OrderRemarkTagMapper {

    /**
     * 根据订单ID查询备注标签
     *
     * @param orderId 订单ID
     * @return 订单备注标签
     */
    @Select("SELECT * FROM sky_take_out.order_remark_tag WHERE order_id = #{orderId}")
    OrderRemarkTag getByOrderId(Long orderId);

    /**
     * 插入订单备注标签
     *
     * @param tag 订单备注标签
     */
    void insert(OrderRemarkTag tag);

    /**
     * 更新订单备注标签
     *
     * @param tag 订单备注标签
     */
    void update(OrderRemarkTag tag);
}
