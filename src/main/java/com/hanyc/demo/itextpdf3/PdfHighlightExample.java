//package com.hanyc.demo.pdf3;
//
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.pdf.*;
//
//import java.io.FileOutputStream;
//
//public class PdfHighlightExample {
//    public static void main(String[] args) {
//        String filePath = "D:\\del\\nraq2\\a.pdf";
//        String targetPath = "D:\\del\\nraq2\\a - " + System.currentTimeMillis() + ".pdf";
//
//        PdfReader pdfReader = null;
//        PdfStamper pdfStamper = null;
//
//        try {
//            // 1. 读取PDF文件
//            pdfReader = new PdfReader(filePath);
//            pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(targetPath));
//
//            // 2. 获取第一页的实际尺寸
//            Rectangle pageSize = pdfReader.getPageSize(1);
//            System.out.println("页面尺寸: " + pageSize);
//
//            // 3. 定义高亮区域（示例：居中区域，宽200，高50）
//            float marginLeft = 100;
//            float marginBottom = 100;
//            float highlightWidth = 300;
//            float highlightHeight = 100;
//            Rectangle highlightArea = new Rectangle(
//                    marginLeft,
//                    marginBottom,
//                    marginLeft + highlightWidth,
//                    marginBottom + highlightHeight
//            );
//
//            // 4. 生成四边形顶点坐标（基于高亮区域）
//            float[] quadPoints = {
//                    highlightArea.getLeft(), highlightArea.getBottom(), // 左下
//                    highlightArea.getRight(), highlightArea.getBottom(), // 右下
//                    highlightArea.getRight(), highlightArea.getTop(),    // 右上
//                    highlightArea.getLeft(), highlightArea.getTop()     // 左上
//            };
//
//            // 5. 创建高亮注释
//            PdfAnnotation highlight = PdfAnnotation.createMarkup(
//                    pdfStamper.getWriter(),
//                    highlightArea,
//                    "高亮注释",
//                    PdfAnnotation.MARKUP_HIGHLIGHT,
//                    quadPoints
//            );
//
//            // 设置高亮颜色（填充色为红色，透明度50%）
//            highlight.setColor(BaseColor.RED);
//            highlight.setTitle("重要内容");
////            highlight.setOpacity(0.5f);
//            // 6. 将注释添加到第一页
//            pdfStamper.addAnnotation(highlight, 1);
//
//            System.out.println("PDF高亮添加成功！");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            // 7. 确保资源关闭
//            try {
//                if (pdfStamper != null) pdfStamper.close();
//                if (pdfReader != null) pdfReader.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}