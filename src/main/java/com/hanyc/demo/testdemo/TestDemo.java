//package com.hanyc.demo.testdemo;
//
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.extra.template.TemplateException;
//import com.alibaba.fastjson.JSONObject;
//import com.hanyc.demo.util.FileUtil;
//import com.spire.doc.Document;
//import com.spire.doc.FileFormat;
//import com.spire.doc.ProtectionType;
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.util.IOUtils;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//
//import java.util.Map;
//
///**
// * @author ：hanyc
// * @date ：2023/7/19 13:49
// * @description：
// */
//@Slf4j
//public class TestDemo {
//    public static void main(String[] args) throws IOException, freemarker.template.TemplateException {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("userName", "韩云川用户");
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        data2html("a.ftl", jsonObject, outputStream);
//        ByteArrayInputStream in = new ByteArrayInputStream(outputStream.toByteArray());
//        Document document = new Document();
////        document.loadFromFile("D:\\del\\aaa\\国家科技管理信息系统公共服务平台-报告模板(1) - 副本.xml");
//        document.loadFromStream(in, FileFormat.Xml);
////        document.updateTableOfContents();
////        outputStream.reset();
//        String outPath = "D:\\del\\aaa\\x.docx";
//        OutputStream outputStreamWord = new FileOutputStream(outPath);
//        document.saveToStream(outputStreamWord, FileFormat.Docx);
//        document.close();
//
//        outputStream.close();
//        in.close();
//        outputStreamWord.close();
//        removeOneBodyElement(outPath);
//    }
//
//    /**
//     * 根据模板，利用提供的数据，生成文件
//     *
//     * @param sourceFile   模板文件名称
//     * @param data         数据
//     * @param outputStream 最终生成的文件，带路径
//     */
//    public static void data2html(String sourceFile, Map<String, Object> data, OutputStream outputStream) throws IOException, TemplateException, freemarker.template.TemplateException {
//        Writer out = null;
//        try {
////            out = new FileWriter(new File(sourceFile));
//            out = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
//            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
//            cfg.setDirectoryForTemplateLoading(new File("D:/del/aaa/"));
//            Template template = cfg.getTemplate(sourceFile);
//            template.process(data, out);
//        } catch (Exception e) {
//            log.error("模板生成报告html文件异常", e);
//            throw e;
//        } finally {
//            try {
//                if (out != null) {
//                    out.flush();
//                    out.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * 读取word文件并删除第一行内容，然后保存文件
//     *
//     * @param filePath 读取/保存文件路径
//     */
//    public static void removeOneBodyElement(String filePath) {
//        //重新读取生成的文档  删除警告
//        InputStream is = null;
//        XWPFDocument document = null;
//        OutputStream os = null;
//        try {
//            is = new FileInputStream(filePath);
//            document = new XWPFDocument(is);
//            //以上 Spire.Doc 生成的文件会自带警告信息，这里来删除Spire.Doc 的警告
//            document.removeBodyElement(0);
//            //输出word内容文件流，新输出路径位置
//            os = new FileOutputStream(filePath);
//            document.write(os);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            IOUtils.closeQuietly(document);
//            IOUtils.closeQuietly(is);
//            IOUtils.closeQuietly(os);
//        }
//    }
//}
