package com.hanyc.demo.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * @author ：hanyc
 * @date ：2023/3/7 16:18
 * @description： EasyPoi 工具类
 */
@Slf4j
public class EasyPoiUtil {
    /**
     * 文件导入
     *
     * @param inputStream 文件流
     * @param sheetIndex  第几个sheet页
     * @param titleRows   表格标题行数
     * @param headerRows  表头行数
     * @param pojoClass   示例class
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(InputStream inputStream, int sheetIndex, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        // 根据file得到Workbook,主要是要根据这个对象获取,传过来的excel有几个sheet页
        ImportParams params = new ImportParams();
        // 第几个sheet表页
        params.setStartSheetIndex(sheetIndex);
        params.setTitleRows(titleRows);
        //设置表头行数
        params.setHeadRows(headerRows);
        return importExcel(inputStream, pojoClass, params);
    }

    /**
     * 文件导入
     *
     * @param inputStream 文件流
     * @param pojoClass   示例class
     * @param params      导入文件参数
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(InputStream inputStream, Class<T> pojoClass, ImportParams params) {
        if (params == null) {
            params = new ImportParams();
        }
        List<T> list = null;
        try {
            //读取的excel数据集合
            list = ExcelImportUtil.importExcel(inputStream, pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new RuntimeException("模板不能为空");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 常规默认导出方式
     *
     * @param list
     * @param pojoClass
     * @param outputStream
     */
    public static void defaultExport(List<?> list, Class<?> pojoClass, OutputStream outputStream) throws Exception {
        //设置导出参数
        ExportParams params = new ExportParams();
        //设置excel类型，XSSF代表xlsx，HSSF代表xls
        params.setType(ExcelType.XSSF);

        Workbook workbook = ExcelExportUtil.exportExcel(params, pojoClass, list);
        OutputStream os = null;
        //将文件存入response对象中，返回给前端
        try {
            os = outputStream;
        } catch (Exception e) {
            log.error("导出excel失败，msg: {}", e.getMessage(), e);
            throw new Exception("导出excel失败");
        } finally {
            try {
                if (os != null) {
                    workbook.write(os);
                    workbook.close();
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 导出excel
     *
     * @param rowNum 从第几行开始
     * @param list   需要导出的数据
     * @return minio文件路径
     */
    public static void excelExport(List<List<String>> list, List<String> titles, int rowNum, OutputStream outputStream) throws IOException {
        //开始制作excel
        /* 第一步，创建一个Workbook，对应一个Excel文件  */
        SXSSFWorkbook wb = new SXSSFWorkbook();
        excelExport(wb, list, titles, rowNum, outputStream);
        SXSSFSheet sheet = wb.createSheet("sheet1");
    }

    /**
     * 导出excel
     *
     * @param rowNum 从第几行开始
     * @param list   需要导出的数据
     * @return minio文件路径
     */
    public static void excelExport(SXSSFWorkbook wb, List<List<String>> list, List<String> titles, int rowNum, OutputStream outputStream) throws IOException {
        long l1 = System.currentTimeMillis();
        /* 第二步，在Workbook中添加一个sheet,对应Excel文件中的sheet  */
        SXSSFSheet sheet = wb.getSheetAt(0);
        /* 第四步，创建表头 */
        // 创建第一页的第一行，索引从rownum 开始
        SXSSFRow row0 = sheet.createRow(rowNum++);
//        XSSFCellStyle headCellStyle = createHeadCellStyle(wb);
        // 设置行高
        long l2 = System.currentTimeMillis();
        log.info("导出excel sheet样式设置耗时:{}", l2 - l1);
        for (int i = 0; i < titles.size(); i++) {
            SXSSFCell tempCell = row0.createCell(i);
            if (BeanUtil.isEmpty(titles.get(i))) {
                continue;
            }
            tempCell.setCellValue(titles.get(i));
//            tempCell.setCellStyle(headCellStyle);
        }
        long l3 = System.currentTimeMillis();
        log.info("导出excel表头设置耗时:{}", l3 - l2);
        //封装数据导出
        for (List<String> record : list) {
            int index = 0;
            SXSSFRow tempRow = sheet.createRow(rowNum++);
            for (String entry : record) {
                SXSSFCell tempCell = tempRow.createCell(index++);
                tempCell.setCellValue(entry);
            }
        }
        long l4 = System.currentTimeMillis();
        log.info("导出excel内容耗时:{}", l4 - l3);
        wb.write(outputStream);
        wb.close();
    }

    /**
     * 创建表头样式
     *
     * @param wb
     * @return
     */
    public static XSSFCellStyle createHeadCellStyle(SXSSFWorkbook wb) {
        XSSFCellStyle cellStyle = (XSSFCellStyle) wb.createCellStyle();
        // 设置自动换行
        cellStyle.setWrapText(true);
        //背景颜色
//        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        //水平居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //垂直对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
        //下边框
//        cellStyle.setBorderBottom(BorderStyle.THIN);
        //左边框
//        cellStyle.setBorderLeft(BorderStyle.THIN);
        //右边框
//        cellStyle.setBorderRight(BorderStyle.THIN);
        //上边框
//        cellStyle.setBorderTop(BorderStyle.THIN);
        // 创建字体样式
//        Font headerFont = wb.createFont();
        //字体加粗
//        headerFont.setBold(true);
        // 设置字体类型
//        headerFont.setFontName("黑体");
        // 设置字体大小
//        headerFont.setFontHeightInPoints((short) 12);
        // 为标题样式设置字体样式
//        cellStyle.setFont(headerFont);

        return cellStyle;
    }
}
