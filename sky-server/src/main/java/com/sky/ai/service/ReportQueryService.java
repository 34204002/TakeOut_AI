package com.sky.ai.service;

import com.sky.vo.ReportAnswerVO;

/**
 * 经营数据自然语言查询服务接口
 */
public interface ReportQueryService {

    /**
     * 使用自然语言查询经营数据
     *
     * @param question 用户的自然语言问题
     * @return AI 回答 + 结构化数据
     */
    ReportAnswerVO answerDataQuestion(String question);
}
