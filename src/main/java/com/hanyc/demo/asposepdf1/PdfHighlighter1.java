package com.hanyc.demo.asposepdf1;

import com.aspose.pdf.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
/**
 * 作废
 */
public class PdfHighlighter1 {

    public static void main(String[] args) {
        String filePath = "D:\\del\\nraq2\\a.pdf";
        String targetPath = "D:\\del\\nraq2\\a2 - " + System.currentTimeMillis() + ".pdf";
        // 1. 加载 PDF 文档
        Document pdfDocument = new Document(filePath);

        // 2. 定位到指定页面（例如第1页，索引从1开始）
        Page targetPage = pdfDocument.getPages().get_Item(1);

        // 3. 提取页面的文本结构（按行分割）
        List<TextLine> textLines = extractTextLines(targetPage);

        // 4. 假设用户指定：第1行末尾3个字符和第2行开头3个字符需要高亮
        int firstLineIndex = 0; // 第1行
        int firstLineStartCharIndex = textLines.get(firstLineIndex).getCharacters().size() - 3; // 末尾3个字符
        int firstLineEndCharIndex = textLines.get(firstLineIndex).getCharacters().size() - 1;

        int secondLineIndex = 1; // 第2行
        int secondLineStartCharIndex = 0; // 开头3个字符
        int secondLineEndCharIndex = 2;

        // 5. 计算高亮区域
        Rectangle firstLineHighlightRect = calculateHighlightRect(textLines.get(firstLineIndex), firstLineStartCharIndex, firstLineEndCharIndex);
        Rectangle secondLineHighlightRect = calculateHighlightRect(textLines.get(secondLineIndex), secondLineStartCharIndex, secondLineEndCharIndex);

        // 6. 合并高亮区域
        Rectangle combinedHighlightRect = combineRectangles(firstLineHighlightRect, secondLineHighlightRect);

        // 7. 添加高亮批注
        if (combinedHighlightRect != null) {
            addHighlightAnnotation(targetPage, combinedHighlightRect);
        }

        // 9. 保存修改后的文档
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
//        List<TextFragment> textFragments = (List<TextFragment>) absorberTextFragments;

        // 按 Y 坐标分组（同一行文本的 Y 坐标相近）
        List<TextLine> textLines = new ArrayList<>();
        for (TextFragment fragment : textFragments) {
            double y = fragment.getRectangle().getLLY();
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
        double y1 = line.getY() - line.getLineHeight(); // 上边界
        double x2 = endChar.getX() + endChar.getWidth();
        double y2 = line.getY(); // 下边界

        return new Rectangle(x1, y1, x2, y2);
    }

    /**
     * 合并两个矩形区域
     */
    private static Rectangle combineRectangles(Rectangle rect1, Rectangle rect2) {
        if (rect1 == null || rect2 == null){
            return null;
        }

        double x1 = Math.min(rect1.getLLX(), rect2.getLLX());
        double y1 = Math.min(rect1.getLLY(), rect2.getLLY());
        double x2 = Math.max(rect1.getLLX() + rect1.getWidth(), rect2.getLLX() + rect2.getWidth());
        double y2 = Math.max(rect1.getLLY() + rect1.getHeight(), rect2.getLLY() + rect2.getHeight());

        return new Rectangle(x1, y1, x2, y2);
    }

    /**
     * 添加高亮批注
     */
    private static void addHighlightAnnotation(Page page, Rectangle rect) {
        HighlightAnnotation highlight = new HighlightAnnotation(page, rect);
        highlight.setColor(Color.getYellow());
        highlight.setContents("This is a highlighted comment.");
        highlight.setOpacity(0.5);
        highlight.setTitle("Author: John Doe");
        page.getAnnotations().add(highlight);
    }

    /**
     * 添加自由文本批注
     */
//    private static void addFreeTextAnnotation(Page page, Rectangle rect, String comment) {
//        FreeTextAnnotation freeText = new FreeTextAnnotation(page, rect);
//        freeText.setContents(comment);
//        freeText.setColor(Color.getGreen());
//        freeText.setOpacity(0.8);
//        freeText.setBorder(new Border(freeText));
//        freeText.getBorder().setWidth(1);
//        page.getAnnotations().add(freeText);
//    }

    /**
     * 辅助类：表示一行文本
     */
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
            fragments.sort(Comparator.comparingDouble(f -> f.getRectangle().getLLX()));
            // 提取所有字符的位置信息
            extractCharacters();
        }

        private void extractCharacters() {
            for (TextFragment fragment : fragments) {
                String text = fragment.getText();
                double x = fragment.getRectangle().getLLX();
                double fontSize = fragment.getTextState().getFontSize();
                double fragmentWidth = fragment.getRectangle().getWidth();
                double charWidth = fragmentWidth / text.length();

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