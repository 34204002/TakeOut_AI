package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;
     /**
     * 订单搜索
     *
     * @param ordersPageQueryDTO 订单搜索参数
     * @return 订单分页数据
     */
     @GetMapping("/conditionSearch")
     public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
         log.info("订单搜索：{}", ordersPageQueryDTO);
         PageResult pageResult = orderService.pageQuery(ordersPageQueryDTO);
         return Result.success(pageResult);
     }

}
