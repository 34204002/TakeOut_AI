package com.sky.service.impl;

import com.sky.entity.Dish;
import com.sky.entity.Orders;
import com.sky.entity.OrdersReport;
import com.sky.entity.Setmeal;
import com.sky.entity.TurnoverReport;
import com.sky.entity.UserReport;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private DishMapper dishMapper;
    
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public BusinessDataVO getBusinessData() {
        // 获取今天的开始时间和结束时间
        LocalDate today = LocalDate.now();
        LocalDate begin = today;
        LocalDate end = today;

        // 营业额：今天已完成订单的总金额
        List<TurnoverReport> turnoverReports = orderMapper.getTurnoverStatistics(begin, end);
        Double turnover = turnoverReports.isEmpty() ? 0.0 : turnoverReports.get(0).getTurnover();

        // 订单统计：获取今天的订单统计数据
        List<OrdersReport> ordersReports = orderMapper.getOrdersStatistics(begin, end);
        Integer validOrderCount = ordersReports.isEmpty() ? 0 : ordersReports.get(0).getValidOrderCount();
        Integer totalOrderCount = ordersReports.isEmpty() ? 0 : ordersReports.get(0).getOrderCount();

        // 订单完成率：有效订单数 / 总订单数
        Double orderCompletionRate = totalOrderCount > 0 ? (double) validOrderCount / totalOrderCount : 0.0;

        // 平均客单价：营业额 / 有效订单数
        Double unitPrice = validOrderCount > 0 ? turnover / validOrderCount : 0.0;

        // 新增用户数：今天注册的用户数量
        List<UserReport> userReports = userMapper.getUserStatistics(begin, end);
        Integer newUsers = userReports.isEmpty() ? 0 : Math.toIntExact(userReports.get(0).getUserCount());

        // 构造并返回BusinessDataVO
        BusinessDataVO businessDataVO = BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();

        return businessDataVO;
    }

    @Override
    public OrderOverViewVO getOverviewOrders() {
        // 获取今天的开始时间和结束时间
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime todayEnd = today.atTime(23, 59, 59);

        // 查询今天的所有订单
        List<Orders> orders = orderMapper.list().stream()
                .filter(order -> order.getOrderTime() != null &&
                        order.getOrderTime().isAfter(todayStart) &&
                        order.getOrderTime().isBefore(todayEnd))
                .collect(Collectors.toList());

        // 待接单数量 (状态为TO_BE_CONFIRMED = 2)
        Integer waitingOrders = (int) orders.stream()
                .filter(order -> order.getStatus().equals(Orders.TO_BE_CONFIRMED))
                .count();

        // 待派送数量 (状态为CONFIRMED = 3)
        Integer deliveredOrders = (int) orders.stream()
                .filter(order -> order.getStatus().equals(Orders.CONFIRMED))
                .count();

        // 已完成数量 (状态为COMPLETED = 5)
        Integer completedOrders = (int) orders.stream()
                .filter(order -> order.getStatus().equals(Orders.COMPLETED))
                .count();

        // 已取消数量 (状态为CANCELLED = 6)
        Integer cancelledOrders = (int) orders.stream()
                .filter(order -> order.getStatus().equals(Orders.CANCELLED))
                .count();

        // 全部订单
        Integer allOrders = orders.size();

        // 构造并返回OrderOverViewVO
        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    @Override
    public DishOverViewVO getOverviewDishes() {
        // 查询所有菜品
        List<Dish> dishes = dishMapper.list();

        // 已启售数量 (状态为1)
        Integer sold = (int) dishes.stream()
                .filter(dish -> dish.getStatus() != null && dish.getStatus() == 1)
                .count();

        // 已停售数量 (状态为0)
        Integer discontinued = (int) dishes.stream()
                .filter(dish -> dish.getStatus() != null && dish.getStatus() == 0)
                .count();

        // 构造并返回DishOverViewVO
        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    @Override
    public SetmealOverViewVO getOverviewSetmeals() {
        // 查询所有套餐
        List<Setmeal> setmeals = setmealMapper.list();

        // 已启售数量 (状态为1)
        Integer sold = (int) setmeals.stream()
                .filter(setmeal -> setmeal.getStatus() != null && setmeal.getStatus() == 1)
                .count();

        // 已停售数量 (状态为0)
        Integer discontinued = (int) setmeals.stream()
                .filter(setmeal -> setmeal.getStatus() != null && setmeal.getStatus() == 0)
                .count();

        // 构造并返回SetmealOverViewVO
        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
