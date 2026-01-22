package com.sky.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersReport implements Serializable {
    //日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    //订单数量
    private Integer orderCount;
    //有效订单数量
    private Integer validOrderCount;
}
