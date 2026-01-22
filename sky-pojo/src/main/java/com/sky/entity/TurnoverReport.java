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
public class TurnoverReport implements Serializable {
    // 日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    // 营业额
    private Double turnover;
}
