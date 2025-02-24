package com.hanyc.demo.asposepdf1;

import com.aspose.pdf.*;

/**
 * 按照段落解析内容.
 */
public class ExtractParagraphs {
    public static void main(String[] args) {
        String filePath = "D:\\del\\nraq2\\2021全球人工智能创新指数报告.pdf";
        String targetPath = "D:\\del\\nraq2\\a2 - " + System.currentTimeMillis() + ".pdf";
        // 加载 PDF
        extractParagraph02(filePath);

        // 打印提取的段落内容
        System.out.println("Extracted Paragraphs:");
    }


    public static void extractParagraph02(String filePath) {
        // 打开一个现有的 PDF 文件
        Document doc = new Document(filePath);
        // 实例化 ParagraphAbsorber
        ParagraphAbsorber absorber = new ParagraphAbsorber();
        absorber.visit(doc);

        for (PageMarkup markup : absorber.getPageMarkups()) {
            int i = 1;
            for (MarkupSection section : markup.getSections()) {
                int j = 1;

                for (MarkupParagraph paragraph : section.getParagraphs()) {
                    StringBuilder paragraphText = new StringBuilder();

                    for (java.util.List<TextFragment> line : paragraph.getLines()) {
                        for (TextFragment fragment : line) {
                            Rectangle rectangle = fragment.getRectangle();
                            paragraphText.append(fragment.getText());
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
    }

}