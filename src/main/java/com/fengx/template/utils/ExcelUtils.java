package com.fengx.template.utils;

import com.fengx.template.exception.WarnException;
import com.fengx.template.pojo.excel.ExcelTypeEnum;
import com.fengx.template.pojo.excel.ExcelTableHead;
import com.fengx.template.response.Response;
import com.fengx.template.utils.common.DateUtils;
import com.fengx.template.utils.common.FileUtils;
import com.fengx.template.utils.common.ObjectUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * Excel操作工具类
 *
 * p:支持大量数据自动分sheet
 */
public class ExcelUtils {

    // Sheet最大行数量
    public static final int EXCEL_MAX_ROWS = 65535;
    // 单元格宽度
    public static final int EXCEL_COLUMN_WIDTH = 6 * 256 * 2;
    // 单元格高度
    public static final short EXCEL_COLUMN_HEIGHT = 24 * 20;

    /**
     * 导出Excel提供网页下载
     * @param fileName 文件名称 （不包括后缀）
     * @param tableHeads 表头
     * @param tableData 表数据
     * @return Response
     */
    public static Response<?> exportExcelDownload(String fileName,
                                                  List<ExcelTableHead> tableHeads,
                                                  List<List<String>> tableData) {
        // 创建Excel
        Workbook workbook = createWorkbook(tableData.size());
        // 填充表头
        setTableHead(workbook, tableHeads);
        // 填充数据
        setTableData(workbook, tableData);
        // 返回下载
        return exportExcelDownload(workbook, incSuffix(fileName));
    }

    /**
     * 解析导入的Excel数据
     * @param file 导入文件
     * @param validStartRow 有效数据在的起始行 (从0开始计算行数, 合并单元格的按未合并时计算行数)
     * @return List<Map<String, String>>
     */
    public static List<Map<String, String>> importExcelAnalysis(MultipartFile file, int validStartRow) {
        List<Map<String, String>> vms = Lists.newArrayList();
        // 解析成excel
        Workbook wb = ExcelUtils.getWorkbook(file);
        // 遍历sheet
        for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
            // sheet
            Sheet sheet = wb.getSheetAt(sheetIndex);
            // 读取表头
            Map<Integer, String> headMap = Maps.newHashMap();
            for (int i = 0; i < validStartRow; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        cell.setCellType(CellType.STRING);
                        String cellValue = cell.getStringCellValue();
                        if (!cellValue.equals("")) {
                            headMap.put(j, cellValue);
                        }
                    }
                }
            }
            // 读取数据
            for (int i = validStartRow; i <= sheet.getLastRowNum(); i++) {
                // 行
                Row row = sheet.getRow(i);
                // 行数据
                Map<String, String> rowDataMap = Maps.newHashMap();
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    // 单元格
                    Cell cell = row.getCell(j);
                    String stringCellValue;
                    // 设置值格式
                    if (cell == null) {
                        stringCellValue = "";
                    } else  {
                        //设置单元格类型
                        cell.setCellType(CellType.STRING);
                        stringCellValue = cell.getStringCellValue();
                    }
                    // 获取单元格数据
                    rowDataMap.put(headMap.get(j), stringCellValue);
                }
                // 装数据
                vms.add(rowDataMap);
            }
        }
        // 返回
        return vms;
    }

    /**
     * 给名字增加时间和文件后缀
     * @param excelName 文件名（无后缀）
     * @return 新文件名
     */
    public static String incSuffix(String excelName) {
        return excelName + DateUtils.nowShortStr() + "." + ExcelTypeEnum.EXCEL_TYPE_XLSX.getCode();
    }

    /**
     * 导出Excel提供网页下载
     * @param wb wb
     * @param fileName 文件名称
     * @return Response
     */
    public static Response<?> exportExcelDownload(Workbook wb, String fileName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(fileName, "UTF-8"));
            headers.set("fileName", URLEncoder.encode(fileName, "UTF-8"));
            headers.setAccessControlExposeHeaders(Lists.newArrayList("fileName", "Contetnt-Disposition"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
            wb.write(outByteStream);
            outByteStream.flush();
            outByteStream.close();
            return new Response<>(outByteStream.toByteArray(), headers);
        } catch (IOException e) {
            throw new WarnException(e.getMessage());
        }
    }

    /**
     * 导出Excel文件
     * @param wb wb
     * @param filePath 文件路径（包括名字和后缀）
     */
    public static void exportExcelFile(Workbook wb, String filePath) {
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(new File(filePath));
            wb.write(stream);
        } catch (IOException e) {
            throw new WarnException(e.getMessage());
        } finally{
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置表格数据
     * @param wb wb
     * @param tableData 表格数据 二维数组
     */
    public static void setTableData(Workbook wb, List<List<String>> tableData) {
        // 获取内容样式
        CellStyle tableBodyStyle = getTableBodyStyle(wb);
        // 页数
        int pageSize = tableData.size() / EXCEL_MAX_ROWS + 1;
        // 划分数据
        List<List<List<String>>> averageAssign = ObjectUtils.averageAssign(tableData, pageSize);
        // 遍历sheet
        for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
            // 此sheet数据
            List<List<String>> data = averageAssign.get(sheetIndex);
            Sheet sheet = wb.getSheetAt(sheetIndex);
            int lastRowNum = sheet.getLastRowNum();
            // 数据写入
            for (int i = 0; i < data.size(); i++) {
                // 创建一行
                Row row = sheet.createRow(i + lastRowNum + 1);
                // 行高
                row.setHeight(EXCEL_COLUMN_HEIGHT);
                List<String> rowData = data.get(i);
                // 列
                for (int j = 0; j < rowData.size(); j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(tableBodyStyle);
                    cell.setCellValue(rowData.get(j));
                }
            }
        }
    }

    /**
     * 设置excel表头
     * @param wb Workbook
     * @param tableHeads 表头数据
     */
    public static void setTableHead(Workbook wb, List<ExcelTableHead> tableHeads) {
        // 获取表头样式
        CellStyle tableHeadStyle = getTableHeadStyle(wb);
        // 遍历sheet
        for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = wb.getSheetAt(sheetIndex);
            // 一级标题的坐标
            int firstIndex = sheet.getLastRowNum();
            // 二级标题的坐标
            int secondIndex = firstIndex + 1;
            // 创建row对象
            Row first = sheet.createRow(firstIndex);
            Row second = sheet.createRow(secondIndex);
            // 行高
            first.setHeight(EXCEL_COLUMN_HEIGHT);
            second.setHeight(EXCEL_COLUMN_HEIGHT);
            // col坐标 头尾指针
            int head = 0, rear = -1;
            for (ExcelTableHead tableHead : tableHeads) {
                // 尾指针移动到这块标题数据末尾
                rear = rear + tableHead.getColNumber();
                // 设置行宽
                sheet.setColumnWidth(head, EXCEL_COLUMN_WIDTH);
                Cell title = first.createCell(head);
                title.setCellStyle(tableHeadStyle);
                title.setCellValue(tableHead.getFirstHead());
                // 处理一级标题
                for (int i = 1; i < tableHead.getColNumber(); i++) {
                    // 设置行宽
                    sheet.setColumnWidth(head + i, EXCEL_COLUMN_WIDTH);
                    Cell col = first.createCell(head + i);
                    col.setCellStyle(tableHeadStyle);
                    col.setCellValue("");
                }
                // 处理二级标题
                for (int i = 0; i < tableHead.getColNumber(); i++) {
                    Cell col = second.createCell(head + i);
                    col.setCellStyle(tableHeadStyle);
                    if (tableHead.getSecondHead().size() != 0) {
                        col.setCellValue(tableHead.getSecondHead().get(i));
                    } else {
                        // 没有二级标题 合并二级标题的单元格
                        sheet.addMergedRegion(new CellRangeAddress(firstIndex, secondIndex, head + i, head + i));
                    }
                }
                // 合并一级标题单元格 (合并区域必须包含2个或更多单元格)
                if (tableHead.getColNumber() > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(firstIndex, firstIndex, head, rear));
                }
                // 头指针移动到新标题数据开头
                head = head + tableHead.getColNumber();
            }
        }
    }

    /**
     * 获取MultipartFile中的文件路径及文件Input流
     * @param file multipartFile文件
     * @return org.apache.poi.ss.usermodel.Workbook
     */
    public static Workbook getWorkbook(MultipartFile file) {
        try {
            String ext = FileUtils.getFileSuffix(file);
            // 转为Input流
            InputStream is = file.getInputStream();
            return createWorkbook(is, ext);
        } catch (IOException e) {
            throw new WarnException(e.getMessage());
        }
    }

    /**
     * 获取poi工作簿
     * @param is  文件流
     * @param ext 后缀
     * @return org.apache.poi.ss.usermodel.Workbook
     */
    public static Workbook createWorkbook(InputStream is, String ext) {
        Workbook workbook;
        try {
            if (ExcelTypeEnum.EXCEL_TYPE_XLSX.getCode().equals(ext)) {   //xlsx
                workbook = new HSSFWorkbook(is);
            } else if (ExcelTypeEnum.EXCEL_TYPE_XLS.getCode().equals(ext)) {  //xls
                workbook = new XSSFWorkbook(is);
            } else {
                throw new WarnException("文件类型错误");
            }
        } catch (IOException e) {
            throw new WarnException(e.getMessage());
        }
        return workbook;
    }

    /**
     * 获取poi工作簿
     * @param dataSize 内容数据数量
     * @return org.apache.poi.ss.usermodel.Workbook
     */
    public static Workbook createWorkbook(int dataSize) {
        Workbook workbook = new HSSFWorkbook();
        int sheetSize = 1;
        if (dataSize > 0) {
            // 大致减一个表头的数量
            int page = dataSize / (EXCEL_MAX_ROWS - 20);
            sheetSize = sheetSize + page;
        }
        // createSheet
        for (int i = 0; i < sheetSize; i++) {
            workbook.createSheet();
        }
        return workbook;
    }

    /**
     * 获取表格表头样式
     * @param wb Workbook
     * @return CellStyle
     */
    private static CellStyle getTableHeadStyle(Workbook wb) {
        CellStyle style = baseStyle(wb);
        // 标题字体
        Font font = wb.createFont();
        font.setColor(HSSFFont.COLOR_NORMAL);
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    /**
     * 获取表格主体内容样式
     * @param wb Workbook
     * @return CellStyle
     */
    private static CellStyle getTableBodyStyle(Workbook wb) {
        return baseStyle(wb);
    }

    /**
     * 基础样式
     * p: 居中和边框
     * @param wb Workbook
     * @return CellStyle
     */
    private static CellStyle baseStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //边框
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}
