package com.hanyc.demo.itextpdf3;

import cn.hutool.core.date.DateUtil;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfTextAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfTextMarkupAnnotation;
import com.itextpdf.kernel.pdf.colorspace.PdfColorSpace;
import com.itextpdf.kernel.pdf.colorspace.PdfDeviceCs;
import com.itextpdf.styledxmlparser.jsoup.helper.DataUtil;
import com.itextpdf.svg.utils.TextRectangle;
import javafx.scene.paint.Color;

import java.io.IOException;

import static com.itextpdf.io.font.PdfEncodings.UNICODE_BIG;

public class AddHighlightAnnotation {

    public static void main(String[] args) throws IOException {
        String filePath = "D:\\del\\nraq2\\a.pdf";
        String targetPath = "D:\\del\\nraq2\\a2 - " + System.currentTimeMillis() + ".pdf";

        // 1. 读取PDF文档
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(filePath), new PdfWriter(targetPath));

        // 2. 获取目标页面（此处以第一页为例）
        PdfPage page = pdfDoc.getFirstPage();
        // 参数说明：x-左下角X坐标, y-左下角Y坐标, width-宽度, height-高度
        Rectangle highlightArea = new Rectangle(50, 500, 200, 20); // 替换为你的实际坐标
        addHighlightAnnotation(page, highlightArea);

        // 9. 保存并关闭文档
        pdfDoc.close();
    }

    public static void addHighlightAnnotation(PdfPage page, Rectangle highlightArea) {
        // 3. 定义高亮区域的坐标（直接指定）

        // 4. 创建高亮批注（需要定义四个顶点坐标，即 QuadPoints）
        // QuadPoints顺序：左下 -> 右下 -> 左上 -> 右上
//        float[] quadPoints = {
//                highlightArea.getLeft(), highlightArea.getBottom(),   // 左下
//                highlightArea.getRight(), highlightArea.getBottom(),   // 右下
//                highlightArea.getLeft(), highlightArea.getTop(),      // 左上
//                highlightArea.getRight(), highlightArea.getTop()       // 右上
//        };
//
//        // 5. 创建高亮批注
//        PdfTextMarkupAnnotation highlight = PdfTextMarkupAnnotation.createHighLight(
//                // 批注的可见区域
//                highlightArea,
//                // 实际高亮覆盖的四个顶点坐标
//                quadPoints
//        );
//        // 6. 设置高亮颜色（RGB黄色）
//        // 设置颜色为黄色 [R=1, G=1, B=0]
//        highlight.setColor(new float[]{1, 1, 0});
//
//        // 7. 可选：设置批注文本内容
//        highlight.setContents("这是高亮批注的说明文本");
//        // 替换为实际作者名称
//        highlight.setTitle(new PdfString("asfagweq作者名称", UNICODE_BIG));
//        highlight.setDate(new PdfString(DateUtil.today(), UNICODE_BIG));
////         8. 将批注添加到页面
//        page.addAnnotation(highlight);

        Rectangle rectangle = new Rectangle(0,0,0,0);
        float[] points = {36, 765, 109, 765, 36, 746, 109, 746,200, 765, 309, 765, 200, 746, 309, 746};
        PdfTextMarkupAnnotation markup = PdfTextMarkupAnnotation.createHighLight(PageSize.A4, points);
        markup.setContents(new PdfString("asfagweq作者名称", UNICODE_BIG));
        markup.setTitle(new PdfString("asfagweq作者名称", UNICODE_BIG));
        float[] rgb = {1, 0, 0};
        PdfArray colors = new PdfArray(rgb);
        markup.setColor(colors);
        page.addAnnotation(markup);
        page.flush();

    }
}