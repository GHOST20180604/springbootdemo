//package com.hanyc.demo.pdf3;
//
//import com.itextpdf.kernel.colors.Color;
//import com.itextpdf.kernel.geom.Rectangle;
//import com.itextpdf.kernel.pdf.*;
//import com.itextpdf.kernel.pdf.annot.PdfTextMarkupAnnotation;
//
//import java.io.IOException;
//
//public class AddHighlightAnnotation {
//    public static void main(String[] args) throws IOException {
//        // 输入和输出文件路径
//        String filePath = "D:\\del\\nraq2\\a.pdf";
//        String targetPath = "D:\\del\\nraq2\\a - " + System.currentTimeMillis() + ".pdf";
//
//        // 创建 PdfDocument 对象
//        PdfDocument pdfDoc = new PdfDocument(new PdfReader(filePath), new PdfWriter(targetPath));
//
//        // 获取第一页
//        PdfPage pdfPage = pdfDoc.getPage(1);
//
//        // 定义高亮区域
//        Rectangle rect = new Rectangle(105, 500, 64, 20);  // 高亮区域的矩形
//        float[] quadPoints = new float[]{169, 500, 105, 500, 169, 520, 105, 520};  // 四边形点
//
//        // 创建高亮注释
//        PdfTextMarkupAnnotation highlightAnnotation = PdfTextMarkupAnnotation.createHighLight(rect, quadPoints);
////        highlightAnnotation.setColor(Color.makeColor());  // 设置高亮颜色为红色
//        highlightAnnotation.setContents("这是一个高亮注释");  // 设置注释内容
//        highlightAnnotation.setTitle(new PdfString("作者"));  // 设置注释的标题
//
//        // 将注释添加到页面
//        pdfPage.addAnnotation(highlightAnnotation);
//
//        // 关闭文档
//        pdfDoc.close();
//
//        System.out.println("高亮注释已成功添加到 PDF 文件中。");
//    }
//}