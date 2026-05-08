package com.sky.ai.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.entity.OrderRemarkTag;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.ai.mapper.OrderRemarkTagMapper;
import com.sky.ai.service.OrderRemarkService;
import com.sky.ai.util.AiCallUtil;
import com.sky.vo.OrderRemarkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Slf4j
public class OrderRemarkServiceImpl implements OrderRemarkService {

    @Autowired
    private AiCallUtil aiCallUtil;

    @Autowired
    private OrderRemarkTagMapper orderRemarkTagMapper;
    
    @Autowired
    private OrderMapper orderMapper; // 注入 OrderMapper

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 标准标签库
    private static final List<String> FLAVOR_TAGS = Arrays.asList("不辣", "微辣", "中辣", "重辣", "少盐", "多醋", "不要葱", "不要蒜", "不要香菜", "免辣");
    private static final List<String> DELIVERY_TAGS = Arrays.asList("放门卫", "放前台", "打电话", "挂门把手", "轻拿轻放");
    private static final List<String> URGENCY_TAGS = Arrays.asList("加急", "尽快", "马上", "特急");

    @Override
    public void parseAndSaveRemark(Long orderId, String originalRemark) {
        if (originalRemark == null || originalRemark.trim().isEmpty()) {
            return;
        }

        OrderRemarkVO remarkVO;
        try {
            // 1. 尝试 AI 解析
            remarkVO = parseByAi(originalRemark);
        } catch (Exception e) {
            log.warn("AI 解析备注失败，降级为规则匹配: {}", e.getMessage());
            // 2. 降级：规则匹配
            remarkVO = parseByRules(originalRemark);
        }

        // 3. 持久化
        saveRemarkTag(orderId, originalRemark, remarkVO);
    }

    @Override
    public OrderRemarkVO previewRemark(String originalRemark) {
        if (originalRemark == null || originalRemark.trim().isEmpty()) {
            return new OrderRemarkVO();
        }
        
        try {
            return parseByAi(originalRemark);
        } catch (Exception e) {
            return parseByRules(originalRemark);
        }
    }

    /**
     * 使用 AI 解析备注
     */
    private OrderRemarkVO parseByAi(String remark) {
        String prompt = String.format(
                "你是一个订单备注解析助手。请从下面的【标准标签库】中选择符合用户备注的标签。\n\n" +
                "【标准标签库】\n" +
                "口味：%s\n" +
                "配送：%s\n" +
                "时效：%s\n\n" +
                "【要求】\n" +
                "1. 只能从上述列表中选择，不要创造新词\n" +
                "2. 输出严格的 JSON 格式：{\"flavorTags\": [], \"deliveryTags\": [], \"urgencyLevel\": 0}\n" +
                "3. urgencyLevel: 0-普通, 1-加急\n\n" +
                "用户备注：%s\n" +
                "JSON输出：",
                String.join(", ", FLAVOR_TAGS),
                String.join(", ", DELIVERY_TAGS),
                String.join(", ", URGENCY_TAGS),
                remark
        );

        String response = aiCallUtil.callChatModel(prompt);
        log.info("AI 解析备注原始响应: {}", response);

        // 清理 Markdown 格式
        response = response.replaceAll("```json", "").replaceAll("```", "").trim();

        try {
            return objectMapper.readValue(response, OrderRemarkVO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("AI 返回格式错误", e);
        }
    }

    /**
     * 使用规则降级解析
     */
    private OrderRemarkVO parseByRules(String remark) {
        OrderRemarkVO vo = new OrderRemarkVO();
        vo.setFlavorTags(Arrays.asList());
        vo.setDeliveryTags(Arrays.asList());
        vo.setUrgencyLevel(0);

        if (Pattern.compile("不辣|免辣").matcher(remark).find()) {
            vo.setFlavorTags(Arrays.asList("不辣"));
        }
        if (Pattern.compile("加急|尽快|马上").matcher(remark).find()) {
            vo.setUrgencyLevel(1);
        }
        if (Pattern.compile("门卫|前台").matcher(remark).find()) {
            vo.setDeliveryTags(Arrays.asList("放门卫"));
        }

        return vo;
    }

    /**
     * 保存解析结果
     */
    private void saveRemarkTag(Long orderId, String originalRemark, OrderRemarkVO vo) {
        try {
            OrderRemarkTag tag = new OrderRemarkTag();
            tag.setOrderId(orderId);
            tag.setOriginalRemark(originalRemark);
            tag.setFlavorTags(objectMapper.writeValueAsString(vo.getFlavorTags()));
            tag.setDeliveryTags(objectMapper.writeValueAsString(vo.getDeliveryTags()));
            tag.setUrgencyLevel(vo.getUrgencyLevel());
            tag.setParseStatus(1); // 1-成功

            orderRemarkTagMapper.insert(tag);
            
            // 同步更新 Orders 表的冗余字段，方便后厨查看
            Orders updateOrder = new Orders();
            updateOrder.setId(orderId);
            updateOrder.setRemarkFlavor(tag.getFlavorTags());
            updateOrder.setRemarkDelivery(tag.getDeliveryTags());
            updateOrder.setRemarkUrgency(tag.getUrgencyLevel());
            updateOrder.setAiParseStatus(1);
            orderMapper.update(updateOrder);
            
        } catch (Exception e) {
            log.error("保存订单备注标签失败", e);
        }
    }
}
