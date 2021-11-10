package com.andon.springbootutil.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andon
 * 2021/11/10
 * <p>
 * Excel导入导出
 */
@Slf4j
public class ExcelUtil {

    /**
     * excel解析
     */
    public static List<Object> excelToList(MultipartFile file) {
        // 获取文件名称
        String originalFilename = file.getOriginalFilename();
        List<Object> list = new ArrayList<>();
        try {
            // 获取输入流
            InputStream inputStream = file.getInputStream();
            // 判断excel版本
            Workbook workbook;
            assert originalFilename != null;
            if (judgeExcelVersion(originalFilename)) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                workbook = new HSSFWorkbook(inputStream);
            }
            // 工作表对象
            Sheet sheet = workbook.getSheetAt(0);
            // 总行数
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            // 工作表的列
            Row row = sheet.getRow(0);
            // 总列数
            int physicalNumberOfCells = row.getPhysicalNumberOfCells();
            // 得到指定的单元格
            List<Object> fieldList = new ArrayList<>();
            for (int i = 0; i < physicalNumberOfRows; i++) {
                Map<Object, Object> map = new HashMap<>();
                Row rowI = sheet.getRow(i);
                for (int j = 0; j < physicalNumberOfCells; j++) {
                    Cell cell = rowI.getCell(j);
                    if (!ObjectUtils.isEmpty(cell)) {
                        Object data = null;
                        CellType cellType = cell.getCellType();
                        if (cellType == CellType.STRING) {
                            data = cell.getStringCellValue();
                        } else if (cellType == CellType.NUMERIC) {
                            data = cell.getNumericCellValue();
                        }
                        if (i == 0) {
                            fieldList.add(data);
                        } else {
                            map.put(fieldList.get(j), data);
                        }
                    }
                }
                if (!ObjectUtils.isEmpty(map)) {
                    list.add(map);
                }
            }
            // 关闭流资源
            inputStream.close();
            workbook.close();
            return list;
        } catch (IOException e) {
            log.error("excelToList failure!! error={}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断上传的excel文件版本（xls为2003，xlsx为2007）
     *
     * @param fileName 文件路径
     * @return excel2007及以上版本返回true，excel2007以下版本返回false
     */
    private static boolean judgeExcelVersion(String fileName) {
        return !fileName.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * excel导出
     */
    public static void exportExcel(HttpServletResponse httpServletResponse, List<List<String>> excelData, String sheetName, String fileName, int columnWidth) {
        try {
            // 声明一个工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 生成一个表格,设置表格名称
            HSSFSheet sheet = workbook.createSheet(sheetName);
            // 设置表格列宽度
            sheet.setDefaultColumnWidth(columnWidth);
            // 写入List<List<String>>中的数据
            int rowIndex = 0;
            for (List<String> data : excelData) {
                // 创建一个row行,然后自增i
                HSSFRow row = sheet.createRow(rowIndex++);
                // 遍历添加本行数据
                for (int i = 0; i < data.size(); i++) {
                    // 创建一个单元格
                    HSSFCell cell = row.createCell(i);
                    // 创建一个内容对象
                    HSSFRichTextString text = new HSSFRichTextString(data.get(i));
                    // 将内容对象的文字内容写入单元格中
                    cell.setCellValue(text);
                }
            }
            // Excel输入流通过response输出到页面下载
            httpServletResponse.setContentType("application/octet-stream");
            // 设置导出Excel的名称
            httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 刷新缓冲
            httpServletResponse.flushBuffer();
            // workbook讲Excel写入到response的输出流中,供页面下载Excel文件
            workbook.write(httpServletResponse.getOutputStream());
            // 关闭workbook
            workbook.close();
        } catch (IOException e) {
            log.error("exportExcel failure!! error={}", e.getMessage());
            e.printStackTrace();
        }
    }
}
