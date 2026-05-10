package com.sky.ai.controller.admin;

import com.sky.dto.MarketingGenerateDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.MarketingCaseVO;
import com.sky.ai.service.MarketingCopyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 管理端营销文案控制器
 */
@RestController("adminMarketingController")
@RequestMapping("/admin/marketing")
@Slf4j
@Tag(name = "营销文案生成接口")
public class MarketingController {

    @Autowired
    private MarketingCopyService marketingCopyService;

    /**
     * 生成营销文案
     *
     * @param dto 生成请求参数（菜品ID、活动类型、渠道、风格）
     * @return 3条不同角度的营销文案
     */
    @PostMapping("/generate")
    @Operation(summary = "生成营销文案")
    public Result<List<MarketingCaseVO>> generateCopies(@RequestBody MarketingGenerateDTO dto) {
        log.info("生成营销文案，参数: {}", dto);
        List<MarketingCaseVO> copies = marketingCopyService.generateCopies(dto);
        return Result.success(copies);
    }

    /**
     * 查询历史案例库
     *
     * @param page 页码
     * @param pageSize 每页数量
     * @param dishCategory 菜品分类（可选）
     * @param activityType 活动类型（可选）
     * @param channel 投放渠道（可选）
     * @return 分页的案例列表
     */
    @GetMapping("/cases")
    @Operation(summary = "查询历史案例库")
    public Result<PageResult> queryCases(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String dishCategory,
            @RequestParam(required = false) String activityType,
            @RequestParam(required = false) String channel) {
        
        // 将空字符串转为 null
        dishCategory = (dishCategory != null && dishCategory.trim().isEmpty()) ? null : dishCategory;
        activityType = (activityType != null && activityType.trim().isEmpty()) ? null : activityType;
        channel = (channel != null && channel.trim().isEmpty()) ? null : channel;
        
        log.info("查询营销案例库，page: {}, pageSize: {}, dishCategory: {}, activityType: {}, channel: {}",
                page, pageSize, dishCategory, activityType, channel);
        PageResult result = marketingCopyService.queryCases(page, pageSize, dishCategory, activityType, channel);
        return Result.success(result);
    }

    /**
     * 反馈效果评分
     *
     * @param id 案例ID
     * @param score 评分（0-5）
     * @return 操作结果
     */
    @PutMapping("/cases/{id}/feedback")
    @Operation(summary = "反馈效果评分")
    public Result<String> feedbackScore(@PathVariable Long id, @RequestParam BigDecimal score) {
        log.info("反馈效果评分，id: {}, score: {}", id, score);
        
        // 校验评分范围
        if (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(new BigDecimal("5")) > 0) {
            return Result.error("评分必须在0-5之间");
        }
        
        marketingCopyService.feedbackScore(id, score);
        return Result.success("评分提交成功");
    }

    /**
     * 发布营销文案
     *
     * @param dto 生成请求参数
     * @param selectedCopy 选中的文案内容
     * @param caseTitle 案例标题（可选）
     * @return 保存后的案例ID
     */
    @PostMapping("/publish")
    @Operation(summary = "发布营销文案")
    public Result<Long> publishCopy(
            @RequestBody MarketingGenerateDTO dto,
            @RequestParam String selectedCopy,
            @RequestParam(required = false) String caseTitle) {
        log.info("发布营销文案，dishId: {}, title: {}", dto.getDishId(), caseTitle);
        
        Long caseId = marketingCopyService.publishCopy(dto, selectedCopy, caseTitle);
        return Result.success(caseId);
    }
}
