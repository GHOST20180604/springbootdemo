package com.hanyc.demo.asposepdf1;

import com.aspose.pdf.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 作废
 */
public class PdfHighlighter0 {

    // 定义句末标点符号（用于判断段落结尾）
    private static final String END_PUNCTUATION = "。！？.!?";

    public static void main(String[] args) {
        String filePath = "D:\\del\\nraq2\\a.pdf";
        String targetPath = "D:\\del\\nraq2\\a2 - " + System.currentTimeMillis() + ".pdf";
        // 1. 加载 PDF 文档
        Document pdfDocument = new Document(filePath);

        // 2. 定位到指定页面（例如第1页，索引从1开始）
        Page targetPage = pdfDocument.getPages().get_Item(1);

        // 3. 提取页面的文本结构（按段落合并多行）
        List<TextParagraph> paragraphs = extractTextParagraphs(targetPage);

        for (TextParagraph textParagraph : paragraphs) {
            StringBuilder sb = new StringBuilder();
            for (TextFragment textFragment : textParagraph.getFragments()) {
                sb.append(textFragment.getText());
            }
            System.out.println(sb.toString());
        }
        // 4. 假设用户指定：第1段，第5到第10个字符
        int targetParagraphIndex = 0; // 段落索引从0开始
        int startCharIndex = 4;       // 字符索引从0开始
        int endCharIndex = 9;

        // 5. 获取目标段落的字符区域
        TextParagraph targetParagraph = paragraphs.get(targetParagraphIndex);
        Rectangle highlightRect = calculateHighlightRect(targetParagraph, startCharIndex, endCharIndex);

        // 6. 添加高亮批注
        if (highlightRect != null) {
            addHighlightAnnotation(targetPage, highlightRect);
        }

        // 7. 保存修改后的文档
        pdfDocument.save(targetPath);
    }

    /**
     * 提取页面中的所有段落（合并多行）
     * 合并规则：
     * 1. 上一行不以句末标点结尾
     */
    private static List<TextParagraph> extractTextParagraphs(Page page) {
        // 使用 TextFragmentAbsorber 提取所有文本片段
        TextFragmentAbsorber absorber = new TextFragmentAbsorber();
        page.accept(absorber);
        List<TextFragment> textFragments = new ArrayList<>();
        for (int i = 1; i <= absorber.getTextFragments().size(); i++) {
            textFragments.add(absorber.getTextFragments().get_Item(i));
        }

        // 按 Y 坐标从上到下排序，X 坐标从左到右排序
//        textFragments.sort(Comparator.comparingDouble((TextFragment f) -> f.getRectangle().getURY())
//                .reversed() // PDF 坐标原点在左下角，需要反向排序
//                .thenComparingDouble(f -> f.getRectangle().getLLX()));

        List<TextParagraph> paragraphs = new ArrayList<>();
        TextParagraph currentParagraph = null;
        TextFragment previousFragment = null;

        for (TextFragment fragment : textFragments) {
            // 判断是否为新段落
            if (currentParagraph == null) {
                currentParagraph = new TextParagraph();
                paragraphs.add(currentParagraph);
            } else {
                // 判断上一行是否以句末标点结尾
                boolean isNewParagraph = endsWithPunctuation(previousFragment.getText());

                if (isNewParagraph) {
                    currentParagraph = new TextParagraph();
                    paragraphs.add(currentParagraph);
                }
            }

            // 添加文本片段到当前段落
            currentParagraph.addFragment(fragment);
            previousFragment = fragment;
        }

        // 提取每个段落的字符位置
        for (TextParagraph paragraph : paragraphs) {
            paragraph.extractCharacters();
        }

        return paragraphs;
    }

    /**
     * 判断文本是否以句末标点结尾
     */
    private static boolean endsWithPunctuation(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        char lastChar = text.charAt(text.length() - 1);
        return END_PUNCTUATION.indexOf(lastChar) != -1;
    }

    /**
     * 计算高亮区域
     */
    private static Rectangle calculateHighlightRect(TextParagraph paragraph, int startCharIndex, int endCharIndex) {
        List<TextCharInfo> chars = paragraph.getCharacters();
        if (chars.isEmpty() || startCharIndex < 0 || endCharIndex >= chars.size()) {
            return null;
        }

        // 获取起始和结束字符的坐标
        TextCharInfo startChar = chars.get(startCharIndex);
        TextCharInfo endChar = chars.get(endCharIndex);

        // 计算高亮区域的矩形坐标
        double x1 = startChar.getX();
        double y1 = paragraph.getMaxY(); // 上边界
        double x2 = endChar.getX() + endChar.getWidth();
        double y2 = paragraph.getMaxY() + paragraph.getLineHeight(); // 下边界

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
     * 辅助类：表示一个段落（可能包含多行）
     */
    @Data
    static class TextParagraph {
        private final List<TextFragment> fragments = new ArrayList<>();
        private final List<TextCharInfo> characters = new ArrayList<>();
        private double minY = Double.MAX_VALUE;
        private double maxY = Double.MIN_VALUE;

        public void addFragment(TextFragment fragment) {
            fragments.add(fragment);
            // 更新段落的 Y 坐标范围
            double y = fragment.getRectangle().getLLY();
            if (y < minY) {
                minY = y;
            }
            if (y > maxY) {
                maxY = y;
            }
        }

        /**
         * 提取所有字符的位置信息
         */
        public void extractCharacters() {
            // 按 Y 坐标从上到下，X 坐标从左到右排序
            fragments.sort(Comparator.comparingDouble((TextFragment f) -> f.getRectangle().getLLY())
                    .reversed()
                    .thenComparingDouble(f -> f.getRectangle().getLLX()));

            for (TextFragment fragment : fragments) {
                String text = fragment.getText();
                double x = fragment.getRectangle().getLLX();
                double y = fragment.getRectangle().getLLY();
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

        public double getLineHeight() {
            return !fragments.isEmpty() ? fragments.get(0).getTextState().getFontSize() : 12;
        }

        public double getMaxY() {
            return maxY;
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