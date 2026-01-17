package com.sky.controller.user;

import com.sky.annotation.AutoFill;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/user/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/reminder/{id}")
    public Result<String> reminder(@PathVariable Long id) {
        log.info("订单提醒：{}", id);
        return Result.success();
    }

    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }
}
