package com.sky.mapper;

import com.sky.entity.OrderDetail;
import com.sky.entity.OrderDetailReport;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface OrderDetailMapper {
    void insertBatch(List<OrderDetail> orderDetailList);

    List<OrderDetail> getByOrderId(Long orderId);


    List<OrderDetailReport> getProductSalesByOrderId(List<Integer> validOrderIdList);
}
