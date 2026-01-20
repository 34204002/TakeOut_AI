package com.sky.controller.user;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户订单控制器
 * 处理用户端的订单相关请求
 */
@RestController("userOrderController")
@RequestMapping ("/user/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 订单提醒
     * @param id 订单ID
     * @return Result<String> 操作结果
     */
    @GetMapping("/reminder/{id}")
    public Result<String> reminder(@PathVariable Long id) {
        log.info("订单提醒：{}", id);
        //orderService.reminder(id);
        return Result.success();
    }

    /**
     * 用户下单
     * @param ordersSubmitDTO 订单提交数据传输对象
     * @return Result<OrderSubmitVO> 订单提交结果，包含订单ID和金额
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }
    
    /**
     * 订单支付
     *
     * @param ordersPaymentDTO 订单支付数据传输对象，包含订单号等信息
     * @return Result<OrderPaymentVO> 订单支付结果，包含预支付交易单信息
     * @throws Exception 支付过程中可能抛出的异常
     */
    @PutMapping("/payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        return Result.success(orderPaymentVO);
    }
    /**
     * 查询订单详情
     * @param id 订单ID
     * @return Result<OrderVO> 订单详情
     */
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> detail(@PathVariable Long id) {
        log.info("查询订单详情：{}", id);
        OrderVO orderVO = orderService.getById(id);
        return Result.success(orderVO);
    }
    /**
     * 历史订单查询
     * @param ordersPageQueryDTO 历史订单查询参数
     * @return Result<PageResult> 历史订单分页查询结果
     */
    @GetMapping("/historyOrders")
    public Result<PageResult> list(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("历史订单查询：{}",ordersPageQueryDTO);
        PageResult pageResult = orderService.pageQuery(ordersPageQueryDTO);
        return Result.success(pageResult);
    }
    /**
     * 取消订单
     * @param id 订单ID
     * @return Result<String> 取消结果
     */
    @PutMapping("/cancel/{id}")
    public Result<String> cancel(@PathVariable Long id) {
        OrdersCancelDTO ordersCancelDTO = OrdersCancelDTO.builder().id(id).cancelReason("用户取消").build();
        log.info("取消订单：{}", ordersCancelDTO);
        orderService.userCancel(ordersCancelDTO);
        return Result.success();
    }
    /**
     * 再来一单
     * @param id 订单ID
     *
     */
    @PostMapping("/repetition/{id}")
    public Result<String> repetition(@PathVariable Long id) {
        log.info("再来一单：{}", id);
        orderService.repetition(id);
        return Result.success();
    }

}