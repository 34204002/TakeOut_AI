package com.sky.ai.service;

import com.sky.dto.MarketingGenerateDTO;
import com.sky.result.PageResult;
import com.sky.vo.MarketingCaseVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 营销文案生成服务接口
 */
public interface MarketingCopyService {

    /**
     * 生成营销文案（3条不同角度）
     *
     * @param dto 生成请求参数
     * @return 3条营销文案列表
     */
    List<MarketingCaseVO> generateCopies(MarketingGenerateDTO dto);

    /**
     * 查询历史案例库（分页）
     *
     * @param page 页码
     * @param pageSize 每页数量
     * @param dishCategory 菜品分类（可选）
     * @param activityType 活动类型（可选）
     * @param channel 投放渠道（可选）
     * @return 分页结果
     */
    PageResult queryCases(Integer page, Integer pageSize, String dishCategory, 
                         String activityType, String channel);

    /**
     * 反馈效果评分
     *
     * @param id 案例ID
     * @param score 评分（0-5）
     */
    void feedbackScore(Long id, BigDecimal score);

    /**
     * 发布营销文案（保存到案例库并写入向量库）
     *
     * @param dto 生成请求参数
     * @param selectedCopy 选中的文案内容
     * @param caseTitle 案例标题
     * @return 保存后的案例ID
     */
    Long publishCopy(MarketingGenerateDTO dto, String selectedCopy, String caseTitle);
}
