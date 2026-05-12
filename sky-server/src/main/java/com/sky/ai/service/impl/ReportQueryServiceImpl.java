package com.sky.ai.service.impl;

import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.ReportAnswerVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import com.sky.ai.service.ReportQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 经营数据自然语言查询服务实现类
 */
@Service
@Slf4j
public class ReportQueryServiceImpl implements ReportQueryService {

    @Autowired
    @Qualifier("statelessChatClient")
    private ChatClient chatClient;

    @Autowired
    private ReportService reportService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // Skill 内容缓存，避免重复读取文件
    private final ConcurrentHashMap<String, String> skillCache = new ConcurrentHashMap<>();

    @Override
    public ReportAnswerVO answerDataQuestion(String question) {
        log.info("收到自然语言查询请求: {}", question);
        long startTime = System.currentTimeMillis();

        try {
            // 获取当前日期，用于 AI 解析相对时间
            String currentDate = LocalDate.now().format(DATE_FORMATTER);
            
            // 加载 Skill 文件内容作为 System Prompt
            String skillContent = loadSkillContent("report-query-assistant.md");
            String systemPrompt = skillContent + "\n\n当前日期：" + currentDate;

            log.info("开始调用 AI（Function Calling）...");
            long aiStartTime = System.currentTimeMillis();
            
            // 使用 Function Calling 自动识别意图并调用工具函数
            // 注意：Spring AI 会自动扫描当前类中的 @Tool 注解方法
            ChatClient.CallResponseSpec responseSpec = chatClient.prompt()
                    .system(systemPrompt)
                    .user(question)
                    .tools(this)  // 注册当前类的 @Tool 方法
                    .call();

            long aiEndTime = System.currentTimeMillis();
            log.info("AI 调用完成，耗时: {}ms", (aiEndTime - aiStartTime));

            // 获取 AI 生成的回答
            String aiAnswer = responseSpec.content();

            long endTime = System.currentTimeMillis();
            log.info("自然语言查询完成，总耗时: {}ms", (endTime - startTime));

            return ReportAnswerVO.builder()
                    .answer(aiAnswer)
                    .data(null)  // Spring AI Function Calling 的数据已融入 AI 回答中
                    .dataType("unknown")
                    .calledFunction(null)
                    .tokenUsage(0)
                    .build();

        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("自然语言查询失败，耗时: {}ms, 错误: {}", (endTime - startTime), e.getMessage(), e);

            // 降级策略：返回友好提示
            return ReportAnswerVO.builder()
                    .answer("抱歉，我暂时无法理解您的问题。您可以尝试问：'上个月营业额多少？'或'最热销的菜品有哪些？'")
                    .data(null)
                    .dataType("error")
                    .calledFunction(null)
                    .tokenUsage(0)
                    .build();
        }
    }

    /**
     * 工具函数1：查询营业额
     */
    @Tool(description = "查询指定日期范围内的营业额数据")
    public TurnoverReportVO getTurnover(
            @ToolParam(description = "开始日期，格式 yyyy-MM-dd") String startDate,
            @ToolParam(description = "结束日期，格式 yyyy-MM-dd") String endDate
    ) {
        log.info("【Tool调用】getTurnover, startDate: {}, endDate: {}", startDate, endDate);

        // 安全校验：限制查询范围最多90天
        validateDateRange(startDate, endDate);

        LocalDate begin = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        return reportService.turnoverStatistics(begin, end);
    }

    /**
     * 工具函数2：查询热销菜品
     */
    @Tool(description = "查询指定日期范围内销量最高的N个菜品")
    public SalesTop10ReportVO getTopSellingDishes(
            @ToolParam(description = "开始日期，格式 yyyy-MM-dd") String startDate,
            @ToolParam(description = "结束日期，格式 yyyy-MM-dd") String endDate,
            @ToolParam(description = "返回前N个菜品，默认10") Integer topN
    ) {
        log.info("【Tool调用】getTopSellingDishes, startDate: {}, endDate: {}, topN: {}", startDate, endDate, topN);

        validateDateRange(startDate, endDate);

        // 限制topN最大值防止过度查询
        if (topN == null || topN <= 0) {
            topN = 10;
        }
        if (topN > 50) {
            topN = 50;
        }

        LocalDate begin = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // 使用 ReportService 的 top10 方法
        return reportService.top10(begin, end);
    }

    /**
     * 工具函数3：查询用户统计数据
     */
    @Tool(description = "查询指定日期范围内的用户增长数据")
    public UserReportVO getUserStatistics(
            @ToolParam(description = "开始日期，格式 yyyy-MM-dd") String startDate,
            @ToolParam(description = "结束日期，格式 yyyy-MM-dd") String endDate
    ) {
        log.info("【Tool调用】getUserStatistics, startDate: {}, endDate: {}", startDate, endDate);

        validateDateRange(startDate, endDate);

        LocalDate begin = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        return reportService.userStatistics(begin, end);
    }

    /**
     * 工具函数4：查询订单统计数据
     */
    @Tool(description = "查询指定日期范围内的订单统计数据")
    public OrderReportVO getOrderStatistics(
            @ToolParam(description = "开始日期，格式 yyyy-MM-dd") String startDate,
            @ToolParam(description = "结束日期，格式 yyyy-MM-dd") String endDate
    ) {
        log.info("【Tool调用】getOrderStatistics, startDate: {}, endDate: {}", startDate, endDate);

        validateDateRange(startDate, endDate);

        LocalDate begin = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        return reportService.ordersStatistics(begin, end);
    }

    /**
     * 安全校验：限制查询时间范围
     */
    private void validateDateRange(String startDate, String endDate) {
        LocalDate begin = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        long days = ChronoUnit.DAYS.between(begin, end);
        if (days < 0) {
            throw new IllegalArgumentException("开始日期不能晚于结束日期");
        }
        // 移除 90 天限制，让用户自由查询
        // if (days > 90) {
        //     throw new IllegalArgumentException("查询时间范围不能超过90天");
        // }
    }

    /**
     * 加载 Skill 文件内容（带缓存）
     */
    private String loadSkillContent(String fileName) {
        // 先从缓存中获取
        return skillCache.computeIfAbsent(fileName, key -> {
            try {
                ClassPathResource resource = new ClassPathResource("ai/skills/" + key);
                String content = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
                log.info("Skill 文件加载成功: {}, 大小: {} bytes", key, content.length());
                return content;
            } catch (IOException e) {
                log.error("加载 Skill 文件失败: {}", key, e);
                // 降级：返回默认提示词
                return "你是一个经营数据分析助手，帮助用户查询业务数据。";
            }
        });
    }
}
