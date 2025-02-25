package com.hanyc.demo.itextpdf3;

import com.hanyc.demo.util.RectangleUtils;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.annot.PdfTextMarkupAnnotation;

import java.io.IOException;

import static com.itextpdf.io.font.PdfEncodings.UNICODE_BIG;

/**
 * 自定义添加高亮测试类
 */
public class HanycAddHighlightAnnotationTest {

    public static void main(String[] args) throws IOException {
        String filePath = "D:\\del\\nraq2\\a.pdf";
        String targetPath = "D:\\del\\nraq2\\a2 - " + System.currentTimeMillis() + ".pdf";

        // 1. 读取PDF文档
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(filePath), new PdfWriter(targetPath));

        // 2. 获取目标页面（此处以第一页为例）
        PdfPage page = pdfDoc.getFirstPage();
        // 参数说明：x-左下角X坐标, y-左下角Y坐标, width-宽度, height-高度
        Rectangle rect1 = new Rectangle(50, 600, 200, 100);// 替换为你的实际坐标
        Rectangle rect2 = new Rectangle(50, 450, 200, 20);// 替换为你的实际坐标

        Rectangle rectangle = RectangleUtils.mergeRectangles(rect1, rect2);
        float[] points = RectangleUtils.mergeRectanglePoints(rect1, rect2);

        PdfTextMarkupAnnotation markup = PdfTextMarkupAnnotation.createHighLight(rectangle, points);
        markup.setContents(new PdfString("asfagweq作者名称", UNICODE_BIG));
        markup.setTitle(new PdfString("asfagweq作者名称", UNICODE_BIG));
        float[] rgb = {1, 0, 0};
        PdfArray colors = new PdfArray(rgb);
        markup.setColor(colors);
        page.addAnnotation(markup);
        page.flush();
        // 9. 保存并关闭文档
        pdfDoc.close();
    }

}