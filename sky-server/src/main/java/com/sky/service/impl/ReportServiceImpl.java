package com.sky.service.impl;

import com.sky.entity.Turnover;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    /**
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();

        List<Turnover> turnoversList = orderMapper.getTurnoverStatistics(begin, end);
        List<LocalDate> dateList = new ArrayList<>();
        List<Double> turnoverList = new ArrayList<>();
        LocalDate date = begin;
        int p=0;
        while (!date.equals(end.plusDays(1))){
            dateList.add(date);
            if(date.equals(turnoversList.get(p).getDate()) ) {
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
}
