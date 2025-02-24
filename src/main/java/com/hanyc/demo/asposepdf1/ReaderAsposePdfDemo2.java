package com.hanyc.demo.asposepdf1;


import com.aspose.pdf.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 作废
 */
public class ReaderAsposePdfDemo2 {

    public static void main(String[] args) {
        String filePath = "D:\\del\\nraq2\\a.pdf";
        String targetPath = "D:\\del\\nraq2\\a - " + System.currentTimeMillis() + ".pdf";
        // Open document
        Document pdfDocument = new Document(filePath);
        Page page = pdfDocument.getPages().get_Item(1);
        // 实例化 ParagraphAbsorber
        ParagraphAbsorber absorber = new ParagraphAbsorber();
        absorber.visit(pdfDocument);
        List<Rectangle> list = new LinkedList();
        for (PageMarkup markup : absorber.getPageMarkups()) {
            int i = 1;
            for (MarkupSection section : markup.getSections()) {
                int j = 1;

                for (MarkupParagraph paragraph : section.getParagraphs()) {
                    StringBuilder paragraphText = new StringBuilder();

                    for (java.util.List<TextFragment> line : paragraph.getLines()) {
                        for (TextFragment fragment : line) {
                            Rectangle rectangle = fragment.getRectangle();
                            if ("认知和学习".contains(fragment.getText())) {
                                list.add(rectangle);
                            }
                        }
//                        paragraphText.append("\r\n");
                    }

//                    System.out.println("第 " + j + " 段，位于第 " + i + " 节，第 " + markup.getNumber() + " 页：");
                    System.out.println(paragraphText.toString());
                    j++;
                }
                i++;
            }
        }
        for (Rectangle rectangle : list) {
            TextAnnotation highlight = new TextAnnotation(page, rectangle);
            highlight.setColor(Color.getYellow());
            highlight.setContents("This is a highlighted comment.\naslfasf\naksjhiauhf");
            highlight.setOpacity(0.5);
            highlight.setTitle("aaaaaaaaaa");
            highlight.setKeptWithNext(true);
            page.getAnnotations().add(highlight);
        }

        // 7. 保存修改后的文档
        pdfDocument.save(targetPath);
    }

    /**
     * 提取页面中的所有文本行
     */
    private static List<ReaderAsposePdfDemo0.TextLine> extractTextLines(Page page) {
        // 使用 TextFragmentAbsorber 提取所有文本片段
        TextFragmentAbsorber absorber = new TextFragmentAbsorber();
        page.accept(absorber);
        TextFragmentCollection textFragments = absorber.getTextFragments();
//        List<TextFragment> textFragments = (List<TextFragment>) textFragments1;

        // 按 Y 坐标分组（同一行文本的 Y 坐标相近）
        List<ReaderAsposePdfDemo0.TextLine> textLines = new ArrayList<>();
        for (TextFragment fragment : textFragments) {
            double y = fragment.getPosition().getYIndent();
            boolean isNewLine = true;

            // 查找是否已有相同行的 TextLine
            for (ReaderAsposePdfDemo0.TextLine line : textLines) {
                if (Math.abs(line.getY() - y) < 5) { // 允许小范围误差
                    line.addFragment(fragment);
                    isNewLine = false;
                    break;
                }
            }

            // 创建新行
            if (isNewLine) {
                ReaderAsposePdfDemo0.TextLine newLine = new ReaderAsposePdfDemo0.TextLine(y);
                newLine.addFragment(fragment);
                textLines.add(newLine);
            }
        }

        // 对每行内的 TextFragment 按 X 坐标排序（从左到右）
        for (ReaderAsposePdfDemo0.TextLine line : textLines) {
            line.sortFragments();
        }

        return textLines;
    }
}
