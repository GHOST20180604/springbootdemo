package com.hanyc.demo.util;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSON;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：hanyc
 * @date ：2024/1/25 11:15
 * @description：
 */
@Slf4j
public class FreemarkerTool {

    public static void main(String[] args) throws IOException, InterruptedException, TemplateException {
        // 文件名
        String sourceFile = "echart-test.ftl";
        // 渲染存储数据
        Map<String, Object> datas = new HashMap<String, Object>();
        List<String> xAxisData = ListUtil.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
        datas.put("xAxisData", JSON.toJSONString(xAxisData));
        List<Integer> yAxisData = ListUtil.of(10, 52, 200, 334, 390, 330, 220);
        datas.put("yAxisData", JSON.toJSONString(yAxisData));
        //最终生成的文件路径
        String destFile = "D:\\code\\springbootdemo2\\src\\main\\resources\\template\\echart-test-" + System.currentTimeMillis() + ".html";

        data2html(sourceFile, datas, destFile);

    }

    /**
     * 根据模板，利用提供的数据，生成文件
     *
     * @param sourceFile 模板文件名
     * @param data       模版数据
     * @param destFile   最终生成的文件，需要携带路径
     */
    public static void data2html(String sourceFile, Map<String, Object> data, String destFile) throws IOException, TemplateException {

        // 如果文件夹不存在 则创建
        FileUtil.createFile(new File(destFile));
        Writer out = null;
        try {
            out = new FileWriter(new File(destFile));
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            // 文件所在位置目录
            cfg.setDirectoryForTemplateLoading(new File("D:/code/springbootdemo2/src/main/resources/template/"));
            Template template = cfg.getTemplate(sourceFile);
            template.process(data, out);
        } catch (Exception e) {
            log.error("模板生成报告html文件异常", e);
            throw e;
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
