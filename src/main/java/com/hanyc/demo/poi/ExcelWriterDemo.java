package com.hanyc.demo.poi;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.hanyc.demo.util.EasyPoiUtil;
import com.hanyc.demo.word.PoiTlDemo.TableDemo1;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.util.*;

/**
 * @author ：hanyc
 * @date ：2024/4/12 11:00
 * @description：
 */
@Slf4j
public class ExcelWriterDemo {
    public static void main(String[] args) throws Exception {
        List<List<String>> mapList = new ArrayList<>();
        List<String> linkedList = new LinkedList<String>();
        for (int i = 0; i < 100000; i++) {
            List<String> linkedListtemp = new LinkedList<String>();
            linkedListtemp.add("111修改后的方法中，我们首先创建了一个新的 XSSFWorkbook 和一个 Sheet。然后，我们循环创建了前7行的空白行。接下来，我们遍历数据列表，并从第8行开始使用 ExcelExportUtil.exportRow() 方法逐行将数据填充到 Workbook 中。最" + i);
            linkedListtemp.add("222修改后的方法中，我们首先创建了一个新的 XSSFWorkbook 和一个 Sheet。然后，我们循环创建了前7行的空白行。接下来，我们遍历数据列表，并从第8行开始使用 ExcelExportUtil.exportRow() 方法逐行将数据填充到 Workbook 中。最" + i);
            linkedListtemp.add("333，我们首先创建了一个新的 XSSFWorkbook 和一个 Sheet。然后，我们循环创建了前7行的空白行。接下来，我们遍历数据列表，并从第8行开始使用 ExcelExportUtil.exportRow() 方法逐行将数据填充到 Workbook 中。最" + i);
            linkedListtemp.add("444修改后的方法中，我们首先创建了一个新的 XSSFWorkbook 和一个 Sheet。然后，我们循环创建了前7行的空白行。接下来，我们遍历数据列表，并从第8行开始使用 ExcelExportUtil.exportRow() 方法逐行将数据填充到 Workbook 中。最" + i);
            linkedListtemp.add("555修改后的方法中，我们首先创建了一个新的 XSSFWorkbook 和一个 Sheet。然后，我们循环创建了前7行的空白行。接下来，我们遍历数据列表，并从第8行开始使用 ExcelExportUtil.exportRow() 方法逐行将数据填充到 Workbook 中。最" + i);
            linkedListtemp.add("666修改后的方法中，我们首先创建了一个新的 XSSFWorkbook 和一个 Sheet。然后，我们循环创建了前7行的空白行。接下来，我们遍历数据列表，并从第8行开始使用 ExcelExportUtil.exportRow() 方法逐行将数据填充到 Workbook 中。最" + i);
            mapList.add(linkedListtemp);
        }

        linkedList.add("表头1");
        linkedList.add("表头2");
        linkedList.add("表头3");
        linkedList.add("表头4");
        linkedList.add("表头5");
        linkedList.add("表头6");

        String tempPath = "D:\\del\\kg2\\测试导出poi" + System.currentTimeMillis() + "-temp.xlsx";
        OutputStream outputStreamTemp = new FileOutputStream(tempPath);

        OutputStream outputStream = new FileOutputStream("D:\\del\\kg2\\测试导出poi" + System.currentTimeMillis() + ".xlsx");
        //封装数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskName", "任务名称");
        map.put("paperNum", 100);
        map.put("riskNum", 102);
        map.put("endTime", "20293923232");
        long l = System.currentTimeMillis();
        ExcelWriter excelWriter = EasyExcel.write(outputStreamTemp).withTemplate("D:\\del\\kg2\\template_content.xlsx").build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        excelWriter.fill(map, writeSheet);
        //关闭文件流
        excelWriter.finish();
        outputStreamTemp.flush();
        outputStreamTemp.close();
        log.info("模版导出耗时:  {}  ", System.currentTimeMillis() - l);
        // 加载现有的 Excel 文件
        XSSFWorkbook existingWorkbook = new XSSFWorkbook(new FileInputStream(tempPath));
        // 创建 SXSSFWorkbook
        SXSSFWorkbook wb = new SXSSFWorkbook(existingWorkbook);
        EasyPoiUtil.excelExport(wb, mapList, linkedList, 10, outputStream);
        outputStream.close();
        FileUtil.del(tempPath);
        log.info("success useTime: {} result :{}", System.currentTimeMillis() - l, "");

    }
}
