package com.sky.ai.service;

import com.sky.vo.OrderRemarkVO;

/**
 * 订单备注解析服务接口
 */
public interface OrderRemarkService {

    /**
     * 解析订单备注并保存到数据库
     *
     * @param orderId        订单ID
     * @param originalRemark 原始备注文本
     */
    void parseAndSaveRemark(Long orderId, String originalRemark);

    /**
     * 预览备注解析结果（不保存，仅用于前端展示）
     *
     * @param originalRemark 原始备注文本
     * @return 解析后的标签对象
     */
    OrderRemarkVO previewRemark(String originalRemark);
}
