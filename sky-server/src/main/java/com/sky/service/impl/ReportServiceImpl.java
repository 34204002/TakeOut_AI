package com.sky.service.impl;

import com.sky.entity.*;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    /**
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();

        List<TurnoverReport> turnoversList = orderMapper.getTurnoverStatistics(begin, end);
        //日期
        List<LocalDate> dateList = new ArrayList<>();
        //营业额
        List<Double> turnoverList = new ArrayList<>();
        LocalDate date = begin;
        int p=0;
        while (!date.equals(end.plusDays(1))){
            dateList.add(date);
            if(p < turnoversList.size() && date.equals(turnoversList.get(p).getDate()) ) {
                turnoverList.add(turnoversList.get(p).getTurnover());
                p++;
            }
            else
                turnoverList.add(0.0);
            date=date.plusDays(1);
        }
        turnoverReportVO.setTurnoverList(StringUtils.join(turnoverList, ","));
        turnoverReportVO.setDateList(StringUtils.join(dateList, ","));
        return turnoverReportVO;
    }

    /**
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        UserReportVO userReportVO = new UserReportVO();
        List<UserReport> userReportList = userMapper.getUserStatistics(begin, end);
        List<LocalDate> dateList = new ArrayList<>();
        List<Long> newUserList = new ArrayList<>();
        List<Long> totalUserList = new ArrayList<>();
        Long totalUserCount = userMapper.countByCreateTimeBefore(begin.plusDays(-1));
        LocalDate date = begin;
        int p=0;
        while (!date.equals(end.plusDays(1))){
            dateList.add(date);
            if(p < userReportList.size() && date.equals(userReportList.get(p).getDate()) ) {
                totalUserCount += userReportList.get(p).getUserCount();
                newUserList.add(userReportList.get(p).getUserCount());
                totalUserList.add(totalUserCount);
                p++;
            }else {
                newUserList.add(0L);
                totalUserList.add(totalUserCount);
            }
            date=date.plusDays(1);
        }
        userReportVO.setDateList(StringUtils.join(dateList, ","));
        userReportVO.setNewUserList(StringUtils.join(newUserList, ","));
        userReportVO.setTotalUserList(StringUtils.join(totalUserList, ","));
        return userReportVO;
    }

    /**
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        OrderReportVO orderReportVO = new OrderReportVO();
        List<OrdersReport> ordersList = orderMapper.getOrdersStatistics(begin, end);
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        LocalDate date = begin;
        int p=0;
        while (!date.equals(end.plusDays(1))){
            dateList.add(date);
            if(p < ordersList.size() && date.equals(ordersList.get(p).getDate()) ) {
                orderCountList.add(ordersList.get(p).getOrderCount());
                validOrderCountList.add(ordersList.get(p).getValidOrderCount());
                p++;
            }else {
                orderCountList.add(0);
                validOrderCountList.add(0);
            }
            date=date.plusDays(1);
        }
        orderReportVO.setDateList(StringUtils.join(dateList, ","));
        orderReportVO.setOrderCountList(StringUtils.join(orderCountList, ","));
        orderReportVO.setValidOrderCountList(StringUtils.join(validOrderCountList, ","));
        orderReportVO.setTotalOrderCount(orderCountList.stream().mapToInt(x -> x).sum());
        orderReportVO.setValidOrderCount(validOrderCountList.stream().mapToInt(x -> x).sum());
        orderReportVO.setOrderCompletionRate(orderReportVO.getValidOrderCount() * 1.0 / orderReportVO.getTotalOrderCount());
        return orderReportVO;
    }

    /**
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
        List<Integer> validOrderIdList=orderMapper.getIdByStatusAndOrderTimeBetween(Orders.COMPLETED, begin, end);
        List<OrderDetailReport> productSalesList = orderDetailMapper.getProductSalesByOrderId(validOrderIdList);
        
        List<String> nameList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        
        // 填充列表
        for (OrderDetailReport report : productSalesList) {
            nameList.add(report.getName());
            numberList.add(report.getTotalNumber());
        }

        SalesTop10ReportVO salesTop10ReportVO = new SalesTop10ReportVO();
        salesTop10ReportVO.setNameList(StringUtils.join(nameList, ","));
        salesTop10ReportVO.setNumberList(StringUtils.join(numberList, ","));
        return salesTop10ReportVO;
    }
}
