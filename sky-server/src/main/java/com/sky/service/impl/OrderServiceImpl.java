package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.mapper.*;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        //处理各种业务异常（地址簿不存在、购物车数据不存在）
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if(addressBook == null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(ShoppingCart.builder().userId(BaseContext.getCurrentId()).build());
        if(shoppingCartList == null || shoppingCartList.isEmpty()){
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //向订单表插入一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        BeanUtils.copyProperties(addressBook,orders);

        orders.setUserId(BaseContext.getCurrentId());
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setPayStatus(Orders.UN_PAID);
        orders.setOrderTime(LocalDateTime.now());

        BigDecimal sumAmount=new BigDecimal(0);
        for (ShoppingCart shoppingCart : shoppingCartList) {
            sumAmount = sumAmount.add(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber())));
        }
        orders.setAmount(sumAmount);
        orders.setNumber(generateOrderNumber());
        //TODO 设置用户名

        orderMapper.insert(orders);
        //向订单明细表插入n条数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart shoppingCart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orders.getId());
            BeanUtils.copyProperties(shoppingCart,orderDetail);
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);
        //清空购物车
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
        //封装VO返回
        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderTime(orders.getOrderTime())
                .orderAmount(orders.getAmount())
                .build();

    }
    /**
     * 生成订单号
     * 格式：yyyyMMddHHmmssSSS + userId
     */
    public String generateOrderNumber() {
        // 获取当前时间戳，精确到毫秒
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

        // 获取用户ID
        Long userId = BaseContext.getCurrentId();

        // 组合为订单号
        return timestamp + userId;
    }


}
