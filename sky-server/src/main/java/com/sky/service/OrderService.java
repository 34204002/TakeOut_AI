package com.sky.service;

import com.sky.dto.*;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);
    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);


    OrderVO getById(Long id);

    PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    void userCancel(OrdersCancelDTO ordersCancelDTO);

    void repetition(Long id);

    OrderStatisticsVO caculateStatistics();

    void update(Orders orders);

    void reject(OrdersRejectionDTO ordersRejectionDTO);

    void adminCancel(OrdersCancelDTO ordersCancelDTO);
}
