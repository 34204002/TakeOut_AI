package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
    /**
     * 查询订单详情
     * @param id 订单ID
     * @return Result<OrderVO> 订单详情
     */
    @GetMapping("/details/{id}")
    public Result<OrderVO> getById(@PathVariable Long id) {
        log.info("查询订单详情：{}", id);
        OrderVO orderVO = orderService.getById(id);
        return Result.success(orderVO);
    }
    /**
     * 各个状态的订单数量统计
     * @return Result<OrderStatisticsVO> 订单数量统计结果
     *
     */
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics() {
        log.info("订单数量统计");
        OrderStatisticsVO orderStatisticsVO = orderService.caculateStatistics();
        return Result.success(orderStatisticsVO);
    }
    

    /**
     * 派送订单
     * @param id 订单ID
     * @return Result<String> 处理结果
     */
    @PutMapping("/delivery/{id}")
    public Result<String> delivery(@PathVariable Long id) {
        log.info("派送订单：{}", id);
        orderService.update(Orders.builder().id(id).status(Orders.DELIVERY_IN_PROGRESS).build());
        return Result.success();
    }


    /**
     * 接单
     * @param
     * @return Result<String> 处理结果
     */
    @PutMapping("/confirm")
    public Result<String> confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("接单：{}", ordersConfirmDTO);
        orderService.update(Orders.builder().id(ordersConfirmDTO.getId()).status(Orders.CONFIRMED).build());
        return Result.success();
    }


    /**
     * 完成订单
     * @param id 订单ID
     * @return Result<String> 处理结果
     */
    @PutMapping("/complete/{id}")
    public Result<String> complete(@PathVariable Long id) {
        log.info("完成订单：{}", id);
        orderService.update(Orders.builder().id(id).status(Orders.COMPLETED).deliveryTime(LocalDateTime.now()).build());
        return Result.success();
    }

    /**
     * 拒单
     * @param ordersRejectionDTO 拒单参数
     * @return Result<String> 处理结果
     */
    @PutMapping("/rejection")
    public Result<String> reject(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        log.info("拒单：{}", ordersRejectionDTO);
        orderService.reject(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 取消订单
     * @param
     * @return Result<String> 处理结果
     */
    @PutMapping("/cancel")
    public Result<String> cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("取消订单：{}", ordersCancelDTO);
        orderService.adminCancel(ordersCancelDTO);
        return Result.success();
    }



}
