package com.sky.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 经营数据自然语言查询请求 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户的自然语言问题
     * 例如："上个月营业额多少？"、"最近一周最热销的菜品有哪些？"
     */
    private String question;
}
