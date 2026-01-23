package com.sky.service.impl;

import com.sky.entity.*;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    // ===================== 严格按坐标定义常量 =====================
    // 概览数据（对应Excel C4/E4/G4/C5/E5）
    private static final int OVERVIEW_ROW1 = 3;    // 概览第1行：Excel行4 → 4-1=3
    private static final int OVERVIEW_ROW2 = 4;    // 概览第2行：Excel行5 →5-1=4
    private static final int COL_TURNOVER = 2;     // 营业额列：Excel C列 → C=2
    private static final int COL_COMPLETION = 4;   // 完成率列：Excel E列 → E=4
    private static final int COL_NEW_USER = 6;     // 新增用户列：Excel G列 → G=6
    private static final int COL_VALID_ORDER = 2;  // 有效订单列：Excel C列 → C=2
    private static final int COL_AVG_PRICE = 4;    // 客单价列：Excel E列 → E=4

    // 明细数据（对应Excel B8-G37）
    private static final int DETAIL_START_ROW = 7; // 明细起始行：Excel行8 →8-1=7
    private static final int DETAIL_END_ROW = 36;  // 明细结束行：Excel行37 →37-1=36
    private static final int COL_DETAIL_DATE = 1;  // 明细日期列：Excel B列 → B=1
    private static final int COL_DETAIL_TURNOVER = 2; // 明细营业额：Excel C列 →C=2
    private static final int COL_DETAIL_VALID = 3; // 明细有效订单：Excel D列 →D=3
    private static final int COL_DETAIL_RATE = 4;  // 明细完成率：Excel E列 →E=4
    private static final int COL_DETAIL_AVG = 5;   // 明细客单价：Excel F列 →F=5
    private static final int COL_DETAIL_USER = 6;  // 明细新增用户：Excel G列 →G=6

    // ===================== 原有业务方法（不变） =====================
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();
        List<TurnoverReport> turnoversList = orderMapper.getTurnoverStatistics(begin, end);
        List<LocalDate> dateList = new ArrayList<>();
        List<Double> turnoverList = new ArrayList<>();
        LocalDate date = begin;
        int p = 0;
        while (!date.equals(end.plusDays(1))) {
            dateList.add(date);
            if (p < turnoversList.size() && date.equals(turnoversList.get(p).getDate())) {
                turnoverList.add(turnoversList.get(p).getTurnover());
                p++;
            } else
                turnoverList.add(0.0);
            date = date.plusDays(1);
        }
        turnoverReportVO.setTurnoverList(StringUtils.join(turnoverList, ","));
        turnoverReportVO.setDateList(StringUtils.join(dateList, ","));
        return turnoverReportVO;
    }

    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        UserReportVO userReportVO = new UserReportVO();
        List<UserReport> userReportList = userMapper.getUserStatistics(begin, end);
        List<LocalDate> dateList = new ArrayList<>();
        List<Long> newUserList = new ArrayList<>();
        List<Long> totalUserList = new ArrayList<>();
        Long totalUserCount = userMapper.countByCreateTimeBefore(begin.plusDays(-1));
        LocalDate date = begin;
        int p = 0;
        while (!date.equals(end.plusDays(1))) {
            dateList.add(date);
            if (p < userReportList.size() && date.equals(userReportList.get(p).getDate())) {
                totalUserCount += userReportList.get(p).getUserCount();
                newUserList.add(userReportList.get(p).getUserCount());
                totalUserList.add(totalUserCount);
                p++;
            } else {
                newUserList.add(0L);
                totalUserList.add(totalUserCount);
            }
            date = date.plusDays(1);
        }
        userReportVO.setDateList(StringUtils.join(dateList, ","));
        userReportVO.setNewUserList(StringUtils.join(newUserList, ","));
        userReportVO.setTotalUserList(StringUtils.join(totalUserList, ","));
        return userReportVO;
    }

    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        OrderReportVO orderReportVO = new OrderReportVO();
        List<OrdersReport> ordersList = orderMapper.getOrdersStatistics(begin, end);
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        LocalDate date = begin;
        int p = 0;
        while (!date.equals(end.plusDays(1))) {
            dateList.add(date);
            if (p < ordersList.size() && date.equals(ordersList.get(p).getDate())) {
                orderCountList.add(ordersList.get(p).getOrderCount());
                validOrderCountList.add(ordersList.get(p).getValidOrderCount());
                p++;
            } else {
                orderCountList.add(0);
                validOrderCountList.add(0);
            }
            date = date.plusDays(1);
        }
        orderReportVO.setDateList(StringUtils.join(dateList, ","));
        orderReportVO.setOrderCountList(StringUtils.join(orderCountList, ","));
        orderReportVO.setValidOrderCountList(StringUtils.join(validOrderCountList, ","));
        orderReportVO.setTotalOrderCount(orderCountList.stream().mapToInt(x -> x).sum());
        orderReportVO.setValidOrderCount(validOrderCountList.stream().mapToInt(x -> x).sum());
        // 除零异常防护
        double completionRate = orderReportVO.getTotalOrderCount() > 0
                ? orderReportVO.getValidOrderCount() * 1.0 / orderReportVO.getTotalOrderCount()
                : 0.0;
        orderReportVO.setOrderCompletionRate(completionRate);
        return orderReportVO;
    }

    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
        List<Integer> validOrderIdList = orderMapper.getIdByStatusAndOrderTimeBetween(Orders.COMPLETED, begin, end);
        List<OrderDetailReport> productSalesList = orderDetailMapper.getProductSalesByOrderId(validOrderIdList);
        List<String> nameList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        for (OrderDetailReport report : productSalesList) {
            nameList.add(report.getName());
            numberList.add(report.getTotalNumber());
        }
        SalesTop10ReportVO salesTop10ReportVO = new SalesTop10ReportVO();
        salesTop10ReportVO.setNameList(StringUtils.join(nameList, ","));
        salesTop10ReportVO.setNumberList(StringUtils.join(numberList, ","));
        return salesTop10ReportVO;
    }

    // ===================== 导出核心方法（修复格式覆盖问题） =====================
    @Override
    public void export(HttpServletResponse response) {
        Workbook workbook = null;
        InputStream inputStream = null;
        try {
            // 1. 时间范围
            LocalDate now = LocalDate.now();
            LocalDate begin = now.minusDays(29);

            // 2. 获取数据
            TurnoverReportVO turnoverVO = turnoverStatistics(begin, now);
            UserReportVO userVO = userStatistics(begin, now);
            OrderReportVO orderVO = ordersStatistics(begin, now);

            // 3. 读取模板
            inputStream = getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
            if (inputStream == null) {
                throw new RuntimeException("模板文件不存在：template/运营数据报表模板.xlsx");
            }
            workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            // 4. 填充概览（严格对应C4/E4/G4/C5/E5）
            fillOverviewByCoord(sheet, turnoverVO, orderVO, userVO, workbook);

            // 5. 填充明细（严格对应B8-G37）
            fillDetailByCoord(sheet, turnoverVO, orderVO, userVO, workbook);

            // 6. 响应设置
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String("运营数据报表.xlsx".getBytes("UTF-8"), "ISO-8859-1"));

            // 7. 写入响应
            try (OutputStream os = response.getOutputStream()) {
                workbook.write(os);
                os.flush();
            }

        } catch (Exception e) {
            throw new RuntimeException("报表导出失败：" + e.getMessage(), e);
        } finally {
            closeResource(workbook);
            closeResource(inputStream);
        }
    }

    // ===================== 数据格式编码定义（替代原有CellStyle创建） =====================
    private short getPercentFormat(Workbook workbook) {
        return workbook.createDataFormat().getFormat("0.00%");
    }

    private short getCurrencyFormat(Workbook workbook) {
        return workbook.createDataFormat().getFormat("¥#,##0.00");
    }

    private short getIntegerFormat(Workbook workbook) {
        return workbook.createDataFormat().getFormat("#,##0");
    }

    // ===================== 按坐标填充概览（C4/E4/G4/C5/E5） =====================
    private void fillOverviewByCoord(Sheet sheet, TurnoverReportVO turnoverVO, OrderReportVO orderVO,
                                     UserReportVO userVO, Workbook workbook) {
        // 1. 概览第1行（Excel行4 → 代码索引3）：C4(营业额)、E4(完成率)、G4(新增用户)
        Row row4 = getOrCreateRow(sheet, OVERVIEW_ROW1);
        // 营业额 → Excel C4（仅设置货币格式，保留模板样式）
        setCellValue(row4, COL_TURNOVER, getLatestTurnover(turnoverVO), workbook, getCurrencyFormat(workbook));
        // 订单完成率 → Excel E4（仅设置百分比格式）
        setCellValue(row4, COL_COMPLETION, orderVO.getOrderCompletionRate(), workbook, getPercentFormat(workbook));
        // 新增用户数 → Excel G4（仅设置整数格式）
        setCellValue(row4, COL_NEW_USER, getLatestNewUser(userVO), workbook, getIntegerFormat(workbook));

        // 2. 概览第2行（Excel行5 → 代码索引4）：C5(有效订单)、E5(客单价)
        Row row5 = getOrCreateRow(sheet, OVERVIEW_ROW2);
        // 有效订单数 → Excel C5（仅设置整数格式）
        setCellValue(row5, COL_VALID_ORDER, orderVO.getValidOrderCount(), workbook, getIntegerFormat(workbook));
        // 平均客单价 → Excel E5
        double avgPrice = orderVO.getValidOrderCount() > 0
                ? getLatestTurnover(turnoverVO) / orderVO.getValidOrderCount()
                : 0.0;
        setCellValue(row5, COL_AVG_PRICE, avgPrice, workbook, getCurrencyFormat(workbook));

        // 强制锁死数据格式（防止模板覆盖，仅改数据格式）
        forceCellStyle(row4, COL_TURNOVER, workbook, getCurrencyFormat(workbook));
        forceCellStyle(row4, COL_COMPLETION, workbook, getPercentFormat(workbook));
        forceCellStyle(row4, COL_NEW_USER, workbook, getIntegerFormat(workbook));
        forceCellStyle(row5, COL_VALID_ORDER, workbook, getIntegerFormat(workbook));
        forceCellStyle(row5, COL_AVG_PRICE, workbook, getCurrencyFormat(workbook));
    }

    // ===================== 按坐标填充明细（B8-G37） =====================
    private void fillDetailByCoord(Sheet sheet, TurnoverReportVO turnoverVO, OrderReportVO orderVO,
                                   UserReportVO userVO, Workbook workbook) {
        // 解析30天数据
        List<String> dates = splitCsv(turnoverVO.getDateList());
        List<String> turnovers = splitCsv(turnoverVO.getTurnoverList());
        List<String> validOrders = splitCsv(orderVO.getValidOrderCountList());
        List<String> totalOrders = splitCsv(orderVO.getOrderCountList());
        List<String> newUsers = splitCsv(userVO.getNewUserList());

        // 循环填充B8-G37（代码索引7-36，共30行）
        for (int i = 0; i <= DETAIL_END_ROW - DETAIL_START_ROW; i++) {
            int currentRowIdx = DETAIL_START_ROW + i; // 当前行索引（7→36）
            Row detailRow = getOrCreateRow(sheet, currentRowIdx);

            // 有数据则填充（i<30天）
            if (i < dates.size()) {
                // 日期 → Excel B列（B8-B37）：不修改格式，保留模板样式
                setCellValue(detailRow, COL_DETAIL_DATE, dates.get(i), workbook, null);
                // 营业额 → Excel C列：仅货币格式
                setCellValue(detailRow, COL_DETAIL_TURNOVER, parseDouble(turnovers.get(i)), workbook, getCurrencyFormat(workbook));
                // 有效订单 → Excel D列：仅整数格式
                setCellValue(detailRow, COL_DETAIL_VALID, parseInt(validOrders.get(i)), workbook, getIntegerFormat(workbook));
                // 完成率 → Excel E列：仅百分比格式
                double rate = calculateRate(validOrders.get(i), totalOrders.get(i));
                setCellValue(detailRow, COL_DETAIL_RATE, rate, workbook, getPercentFormat(workbook));
                // 客单价 → Excel F列：仅货币格式
                double avg = calculateAvgPrice(turnovers.get(i), validOrders.get(i));
                setCellValue(detailRow, COL_DETAIL_AVG, avg, workbook, getCurrencyFormat(workbook));
                // 新增用户 → Excel G列：仅整数格式
                setCellValue(detailRow, COL_DETAIL_USER, parseLong(newUsers.get(i)), workbook, getIntegerFormat(workbook));
            }

            // 强制锁死明细数据格式（仅改数据格式，保留模板样式）
            forceCellStyle(detailRow, COL_DETAIL_TURNOVER, workbook, getCurrencyFormat(workbook));
            forceCellStyle(detailRow, COL_DETAIL_VALID, workbook, getIntegerFormat(workbook));
            forceCellStyle(detailRow, COL_DETAIL_RATE, workbook, getPercentFormat(workbook));
            forceCellStyle(detailRow, COL_DETAIL_AVG, workbook, getCurrencyFormat(workbook));
            forceCellStyle(detailRow, COL_DETAIL_USER, workbook, getIntegerFormat(workbook));
        }
    }

    // ===================== 工具方法（修复样式覆盖问题） =====================
    private double getLatestTurnover(TurnoverReportVO vo) {
        return getLatestDoubleValue(vo.getTurnoverList());
    }

    private long getLatestNewUser(UserReportVO vo) {
        return getLatestLongValue(vo.getNewUserList());
    }

    private double calculateAvgPrice(String turnoverStr, String validOrderStr) {
        double turnover = parseDouble(turnoverStr);
        int valid = parseInt(validOrderStr);
        return valid > 0 ? turnover / valid : 0.0;
    }

    private double calculateRate(String validStr, String totalStr) {
        int valid = parseInt(validStr);
        int total = parseInt(totalStr);
        return total > 0 ? (double) valid / total : 0.0;
    }

    private Row getOrCreateRow(Sheet sheet, int rowIdx) {
        Row row = sheet.getRow(rowIdx);
        return row == null ? sheet.createRow(rowIdx) : row;
    }

    /**
     * 重构后的赋值方法：继承原有样式，仅修改数据格式
     */
    private void setCellValue(Row row, int colIdx, Object value, Workbook workbook, Short dataFormat) {
        Cell cell = row.getCell(colIdx);
        // 优先复用模板单元格，避免创建新单元格（新单元格无原有样式）
        if (cell == null) {
            cell = row.createCell(colIdx);
            // 若为新单元格，默认继承该行首单元格的样式（尽量贴近模板）
            Cell firstCell = row.getCell(0);
            if (firstCell != null) {
                cell.setCellStyle(firstCell.getCellStyle());
            }
        }

        // 1. 赋值
        if (value instanceof String) cell.setCellValue((String) value);
        else if (value instanceof Double) cell.setCellValue((Double) value);
        else if (value instanceof Integer) cell.setCellValue((Integer) value);
        else if (value instanceof Long) cell.setCellValue((Long) value);

        // 2. 仅修改数据格式，保留原有样式（边框/字体/背景等）
        if (dataFormat != null && workbook != null) {
            CellStyle originStyle = cell.getCellStyle();
            // 克隆原有样式（避免修改共享样式导致其他单元格受影响）
            CellStyle newStyle = workbook.createCellStyle();
            newStyle.cloneStyleFrom(originStyle);
            newStyle.setDataFormat(dataFormat);
            cell.setCellStyle(newStyle);
        }
    }

    /**
     * 重构后的样式强制方法：仅修改数据格式，保留原有样式
     */
    private void forceCellStyle(Cell cell, Workbook workbook, Short dataFormat) {
        if (cell == null || workbook == null) return;
        // 克隆原有样式，仅更新数据格式
        CellStyle originStyle = cell.getCellStyle();
        CellStyle newStyle = workbook.createCellStyle();
        newStyle.cloneStyleFrom(originStyle);
        if (dataFormat != null) {
            newStyle.setDataFormat(dataFormat);
        }
        cell.setCellStyle(newStyle);
    }

    /**
     * 重载：按行+列索引强制设置数据格式
     */
    private void forceCellStyle(Row row, int colIdx, Workbook workbook, Short dataFormat) {
        Cell cell = row.getCell(colIdx);
        if (cell == null) {
            cell = row.createCell(colIdx);
            // 新单元格继承行内首个单元格样式
            Cell firstCell = row.getCell(0);
            if (firstCell != null) {
                cell.setCellStyle(firstCell.getCellStyle());
            }
        }
        forceCellStyle(cell, workbook, dataFormat);
    }

    private List<String> splitCsv(String csv) {
        return csv == null || csv.isEmpty() ? List.of() : Arrays.asList(csv.split(","));
    }

    private double getLatestDoubleValue(String csv) {
        List<String> values = splitCsv(csv);
        return values.isEmpty() ? 0.0 : parseDouble(values.get(values.size() - 1));
    }

    private long getLatestLongValue(String csv) {
        List<String> values = splitCsv(csv);
        return values.isEmpty() ? 0L : parseLong(values.get(values.size() - 1));
    }

    private double parseDouble(String str) {
        try { return Double.parseDouble(str); } catch (Exception e) { return 0.0; }
    }

    private int parseInt(String str) {
        try { return Integer.parseInt(str); } catch (Exception e) { return 0; }
    }

    private long parseLong(String str) {
        try { return Long.parseLong(str); } catch (Exception e) { return 0L; }
    }

    private void closeResource(AutoCloseable resource) {
        if (resource != null) {
            try { resource.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }


}