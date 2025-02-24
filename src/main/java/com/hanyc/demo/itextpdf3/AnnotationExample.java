//package com.hanyc.demo.pdf3;
//
//import com.aspose.pdf.Color;
//import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.*;
//
//import java.io.FileOutputStream;
//
//public class AnnotationExample {
//    public static void main(String[] args) {
//        try {
//            String filePath = "D:\\del\\nraq2\\a.pdf";
//            String targetPath = "D:\\del\\nraq2\\a - " + System.currentTimeMillis() + ".pdf";
//            // 创建一个新的文档
//
//            PdfReader pdfReader = new PdfReader(filePath);
//            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(targetPath));
//
//
//            // 定义高亮区域
//            Rectangle highlightArea = new Rectangle(100, 600, 200, 700);
//// 创建高亮注释
//            PdfAnnotation highlight = PdfAnnotation.createMarkup(pdfStamper.getWriter(),
//                    highlightArea,
//                    "Highlighted text",
//                    PdfAnnotation.MARKUP_HIGHLIGHT,
//                    new float[]{200, 600, 300, 600, 300, 700, 100, 700});
//            highlight.setColor(BaseColor.RED);
//            pdfStamper.addAnnotation(highlight, 1);
//
////            document.close();
//            pdfStamper.close();
//
//
//            pdfReader.close();
//
//
//            System.out.println("PDF document created successfully!");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}