package com.hanyc.demo.itextpdf3;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.FilteredTextEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.filter.TextRegionEventFilter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;

import java.io.IOException;
import java.util.List;

/**
 * 通过矩形获取句型中的文本内容
 */
public class ReadPdfByRectangle {

    public static void main(String[] args) throws IOException {
        String filePath = "D:\\del\\nraq2\\a.pdf";
        String targetPath = "D:\\del\\nraq2\\a2 - " + System.currentTimeMillis() + ".pdf";
        // 示例：提取页面中 (50, 50) 到 (200, 200) 区域的文本
        Rectangle area = new Rectangle(50, 600, 200, 100); // x, y, width, height
        String text = extractTextFromArea(filePath, area);
        System.out.println("提取的文本内容:\n" + text);

        // 示例：提取多个区域的文本
//        List<Rectangle> areas = Arrays.asList(
//                  new Rectangle(50, 600, 200, 100)
//        );
//        String multiText = extractTextFromAreas(filePath, areas);
//        System.out.println("多区域提取结果:\n" + multiText);
    }

    /**
     * 提取指定矩形区域内的文本
     *
     * @param pdfPath    PDF文件路径
     * @param targetArea 目标区域（坐标基于PDF页面左下角）
     * @return 区域内的文本内容
     */
    public static String extractTextFromArea(String pdfPath, Rectangle targetArea) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(pdfPath));
        StringBuilder extractedText = new StringBuilder();

        // 遍历每一页
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            // 定义区域过滤器
            TextRegionEventFilter filter = new TextRegionEventFilter(targetArea);

            // 绑定过滤策略
            LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();
            FilteredTextEventListener listener = new FilteredTextEventListener(strategy, filter);

            // 处理页面内容
            new PdfCanvasProcessor(listener).processPageContent(pdfDoc.getPage(i));

            // 获取过滤后的文本
            extractedText.append(strategy.getResultantText());
        }

        pdfDoc.close();
        return extractedText.toString();
    }

    /**
     * 提取多个矩形区域的文本
     *
     * @param pdfPath     PDF文件路径
     * @param targetAreas 多个目标区域
     * @return 所有区域内的文本内容（按区域顺序合并）
     */
    public static String extractTextFromAreas(String pdfPath, List<Rectangle> targetAreas) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(pdfPath));
        StringBuilder extractedText = new StringBuilder();

        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            for (Rectangle area : targetAreas) {
                TextRegionEventFilter filter = new TextRegionEventFilter(area);
                LocationTextExtractionStrategy strategy = new LocationTextExtractionStrategy();
                FilteredTextEventListener listener = new FilteredTextEventListener(strategy, filter);
                new PdfCanvasProcessor(listener).processPageContent(pdfDoc.getPage(i));
                extractedText.append(strategy.getResultantText());
            }
        }

        pdfDoc.close();
        return extractedText.toString();
    }

}