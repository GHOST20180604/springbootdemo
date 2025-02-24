package com.hanyc.demo.asposepdf1;


import com.aspose.pdf.*;
import lombok.Data;
//import com.aspose.pdf.annotations.TextHighlightAnnotation;
//import com.aspose.pdf.text.TextFragment;
//import com.aspose.pdf.text.TextFragmentAbsorber;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ReaderAsposePdfDemo0 {

    public static void main(String[] args) {
        String filePath = "D:\\del\\nraq2\\a.pdf";
        String targetPath = "D:\\del\\nraq2\\a2 - " + System.currentTimeMillis() + ".pdf";
        // 1. 加载 PDF 文档
        Document pdfDocument = new Document(filePath);

        // 2. 定位到指定页面（例如第1页，索引从1开始）
        Page targetPage = pdfDocument.getPages().get_Item(1);

        // 3. 提取页面的文本结构（按行分割）
        List<TextLine> textLines = extractTextLines(targetPage);
        for (TextLine textLine : textLines) {
            StringBuilder sb = new StringBuilder();
            for (TextFragment textFragment : textLine.getFragments()) {
                sb.append(textFragment.getText());
            }
            System.out.println(sb.toString());
        }
        // 4. 假设用户指定：第2行，第5到第10个字符
        int targetLineIndex = 1; // 行号从0开始
        int startCharIndex = 3;  // 字符索引从0开始
        int endCharIndex = 5;

        // 5. 获取目标行的字符区域
        TextLine targetLine = textLines.get(targetLineIndex);
        Rectangle highlightRect = calculateHighlightRect(targetLine, startCharIndex, endCharIndex);

        // 6. 添加高亮批注
        if (highlightRect != null) {
            addHighlightAnnotation(targetPage, highlightRect);
        }


        // 4. 假设用户指定：第2行，第5到第10个字符
        int targetLineIndex1 = 2; // 行号从0开始
        int startCharIndex1 = 0;  // 字符索引从0开始
        int endCharIndex1 = 10;

        // 5. 获取目标行的字符区域
        TextLine targetLine1 = textLines.get(targetLineIndex1);
        Rectangle highlightRect1 = calculateHighlightRect(targetLine1, startCharIndex1, endCharIndex1);
        Rectangle newR = highlightRect.join(highlightRect1);
        // 6. 添加高亮批注
        if (highlightRect1 != null) {
            addHighlightAnnotation(targetPage, newR);
        }


        // 7. 保存修改后的文档
        pdfDocument.save(targetPath);
    }

    /**
     * 提取页面中的所有文本行
     */
    private static List<TextLine> extractTextLines(Page page) {
        // 使用 TextFragmentAbsorber 提取所有文本片段
        TextFragmentAbsorber absorber = new TextFragmentAbsorber();
        page.accept(absorber);
        TextFragmentCollection textFragments = absorber.getTextFragments();
//        List<TextFragment> textFragments = (List<TextFragment>) textFragments1;

        // 按 Y 坐标分组（同一行文本的 Y 坐标相近）
        List<TextLine> textLines = new ArrayList<>();
        for (TextFragment fragment : textFragments) {
            double y = fragment.getPosition().getYIndent();
            boolean isNewLine = true;

            // 查找是否已有相同行的 TextLine
            for (TextLine line : textLines) {
                if (Math.abs(line.getY() - y) < 5) { // 允许小范围误差
                    line.addFragment(fragment);
                    isNewLine = false;
                    break;
                }
            }

            // 创建新行
            if (isNewLine) {
                TextLine newLine = new TextLine(y);
                newLine.addFragment(fragment);
                textLines.add(newLine);
            }
        }

        // 对每行内的 TextFragment 按 X 坐标排序（从左到右）
        for (TextLine line : textLines) {
            line.sortFragments();
        }

        return textLines;
    }

    /**
     * 计算高亮区域
     */
    private static Rectangle calculateHighlightRect(TextLine line, int startCharIndex, int endCharIndex) {
        List<TextCharInfo> chars = line.getCharacters();
        if (chars.isEmpty() || startCharIndex < 0 || endCharIndex >= chars.size()) {
            return null;
        }

        // 获取起始和结束字符的坐标
        TextCharInfo startChar = chars.get(startCharIndex);
        TextCharInfo endChar = chars.get(endCharIndex);

        // 计算高亮区域的矩形坐标
        double x1 = startChar.getX();
        // 上边界
        double y1 = line.getY();
        double x2 = endChar.getX() + endChar.getWidth();
        // 下边界
        double y2 = line.getY() + line.getLineHeight();

        return new Rectangle(x1, y1, x2, y2);
    }

    /**
     * 添加高亮批注
     */
    private static void addHighlightAnnotation(Page page, Rectangle rect) {
        HighlightAnnotation highlight = new HighlightAnnotation(page, rect);
        highlight.setColor(Color.getYellow());
        highlight.setContents("This is a highlighted comment.\naslfasf\naksjhiauhf");
        highlight.setOpacity(0.5);
        highlight.setTitle("aaaaaaaaaa");
        highlight.setKeptWithNext(true);
        page.getAnnotations().add(highlight);
    }

    /**
     * 辅助类：表示一行文本
     */
    @Data
    static class TextLine {
        private final double y;
        private final List<TextFragment> fragments = new ArrayList<>();
        private final List<TextCharInfo> characters = new ArrayList<>();

        public TextLine(double y) {
            this.y = y;
        }

        public void addFragment(TextFragment fragment) {
            fragments.add(fragment);
        }

        public void sortFragments() {
            // 按 X 坐标从左到右排序
            fragments.sort(Comparator.comparingDouble(f -> f.getPosition().getXIndent()));
            // 提取所有字符的位置信息
            extractCharacters();
        }

        private void extractCharacters() {
            for (TextFragment fragment : fragments) {
                String text = fragment.getText();
                double x = fragment.getPosition().getXIndent();
                double fontSize = fragment.getTextState().getFontSize();
                double charWidth = fragment.getRectangle().getWidth() / text.length();

                // 记录每个字符的位置
                for (int i = 0; i < text.length(); i++) {
                    double charX = x + i * charWidth;
                    characters.add(new TextCharInfo(charX, y, charWidth, fontSize));
                }
            }
        }

        public List<TextCharInfo> getCharacters() {
            return characters;
        }

        public double getY() {
            return y;
        }

        public double getLineHeight() {
            return !fragments.isEmpty() ? fragments.get(0).getTextState().getFontSize() : 12;
        }
    }

    /**
     * 辅助类：表示单个字符的位置信息
     */
    static class TextCharInfo {
        private final double x;
        private final double y;
        private final double width;
        private final double height;

        public TextCharInfo(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public double getX() {
            return x;
        }

        public double getWidth() {
            return width;
        }
    }

}
