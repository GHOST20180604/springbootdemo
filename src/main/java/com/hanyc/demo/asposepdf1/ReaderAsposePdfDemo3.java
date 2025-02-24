package com.hanyc.demo.asposepdf1;

import com.aspose.pdf.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加跨行转段落 添加按照段落获取 行的开始结束为止.
 * 终版!!!!
 */
public class ReaderAsposePdfDemo3 {

    // 定义句末标点符号（用于判断段落结尾）
    private static final String END_PUNCTUATION = "。！？.!?；;";

    public static void main(String[] args) {
        String filePath = "D:\\del\\nraq2\\a.pdf";
        String targetPath = "D:\\del\\nraq2\\a2 - " + System.currentTimeMillis() + ".pdf";

        // 1. 加载 PDF 文档
        Document pdfDocument = new Document(filePath);
        for (int pageIndex = 1; pageIndex <= pdfDocument.getPages().size(); pageIndex++) {
            System.out.println("当前正在解析第 {" + pageIndex + "} 内容:");
            // 2. 定位到指定页面（例如第1页，索引从1开始）
            Page targetPage = pdfDocument.getPages().get_Item(pageIndex);
            // 每行内容
            List<TextLine> textLines = extractTextLines(targetPage);

            // 4. 打印所有段落内容
            for (int i = 0; i < textLines.size(); i++) {
                TextLine textLine = textLines.get(i);
                System.out.println(textLine.getText());
            }

            // 3. 提取并合并段落
//            List<TextParagraph> paragraphs = extractParagraphs(textLines);

            // 4. 打印所有段落内容
//        for (int i = 0; i < paragraphs.size(); i++) {
//            TextParagraph para = paragraphs.get(i);
//            System.out.println("段落" + (i + 1) + "（行" + para.startLine + "-" + para.endLine + "）：");
//            System.out.println(para.getText());
//        }

            // 5. 示例：获取第二个段落的字符信息位置
//        if (paragraphs.size() >= 7) {
//            TextParagraph targetPara = paragraphs.get(6);
//
//            // 获取段落中每行的字符范围
//            System.out.println("\n段落7的每行字符范围：");
//            List<LineRange> lineRanges = targetPara.getLineRanges();
//            for (LineRange range : lineRanges) {
//                System.out.printf("行%d: 字符%d-%d%n",
//                        range.lineIndex, range.startCharIndex, range.endCharIndex);
//            }
//
//            // 获取指定字符范围的位置信息
//            List<CharPosition> positions = targetPara.getCharPositions(5, 15); // 获取第5到15字符的位置
//            System.out.println("\n示例字符位置信息：");
//            for (CharPosition pos : positions) {
//                System.out.printf("行%d: 字符%d-%d (坐标:%.2f,%.2f -> %.2f,%.2f)%n",
//                        pos.lineIndex, pos.startCharIndex, pos.endCharIndex,
//                        pos.rect.getLLX(), pos.rect.getLLY(),
//                        pos.rect.getURX(), pos.rect.getURY());
//                TextLine textLine = textLines.get(pos.lineIndex);
//                Rectangle highlightRect = calculateHighlightRect(textLine, pos.startCharIndex, pos.endCharIndex);
//                // 6. 添加高亮批注
//                if (highlightRect != null) {
//                    addHighlightAnnotation(targetPage, highlightRect);
//                }
//            }
//        }
        }
        // 6. 保存修改后的文档
//        pdfDocument.save(targetPath);
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
        page.getAnnotations().add(highlight);
    }


    /**
     * 提取并合并段落
     */
    private static List<TextParagraph> extractParagraphs(List<TextLine> textLines) {
        List<TextParagraph> paragraphs = new ArrayList<>();

        TextParagraph currentPara = null;
        int lineIndex = 0;

        while (lineIndex < textLines.size()) {
            TextLine line = textLines.get(lineIndex);

            if (currentPara == null) {
                currentPara = new TextParagraph();
                currentPara.startLine = lineIndex;
            }

            currentPara.addLine(line);
            currentPara.endLine = lineIndex;

            // 判断是否结束当前段落
            if (endsWithPunctuation(line.getText())) {
                paragraphs.add(currentPara);
                currentPara = null;
            }

            lineIndex++;

            // 处理最后未闭合的段落
            if (lineIndex == textLines.size() && currentPara != null) {
                paragraphs.add(currentPara);
            }
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
     * 提取页面中的所有文本行（保持原始顺序）
     */
    private static List<TextLine> extractTextLines(Page page) {
        TextFragmentAbsorber absorber = new TextFragmentAbsorber();
        page.accept(absorber);
        TextFragmentCollection textFragments = absorber.getTextFragments();

        List<TextLine> textLines = new ArrayList<>();
        for (TextFragment fragment : textFragments) {
            double y = fragment.getPosition().getYIndent();
            boolean isNewLine = true;

            for (TextLine line : textLines) {
                if (Math.abs(line.getY() - y) < 5) {
                    line.addFragment(fragment);
                    isNewLine = false;
                    break;
                }
            }

            if (isNewLine) {
                TextLine newLine = new TextLine(y);
                newLine.addFragment(fragment);
                textLines.add(newLine);
            }
        }

        // 按Y坐标从上到下排序
        textLines.sort((a, b) -> Double.compare(b.getY(), a.getY()));

        // 为每行添加索引
        for (int i = 0; i < textLines.size(); i++) {
            textLines.get(i).lineIndex = i;
        }

        return textLines;
    }

    /**
     * 段落信息类
     */
    @Data
    static class TextParagraph {
        private int startLine;  // 起始行号
        private int endLine;    // 结束行号
        private final List<TextLine> lines = new ArrayList<>();
        private final List<TextCharInfo> allChars = new ArrayList<>();

        public void addLine(TextLine line) {
            lines.add(line);
            allChars.addAll(line.getCharacters());
        }

        public String getText() {
            StringBuilder sb = new StringBuilder();
            for (TextLine line : lines) {
                sb.append(line.getText());
            }
            return sb.toString();
        }

        /**
         * 获取段落中每行的字符范围
         */
        public List<LineRange> getLineRanges() {
            List<LineRange> ranges = new ArrayList<>();
            if (allChars.isEmpty()) {
                return ranges;
            }

            int currentStart = 0;
            int currentLine = allChars.get(0).sourceLine.lineIndex;

            for (int i = 1; i < allChars.size(); i++) {
                if (allChars.get(i).sourceLine.lineIndex != currentLine) {
                    // 记录当前行的范围
                    ranges.add(new LineRange(
                            currentLine,
                            currentStart - getLineStartIndex(currentLine),
                            i - 1 - getLineStartIndex(currentLine)
                    ));

                    // 更新当前行信息
                    currentStart = i;
                    currentLine = allChars.get(i).sourceLine.lineIndex;
                }
            }

            // 添加最后一行的范围
            ranges.add(new LineRange(
                    currentLine,
                    currentStart - getLineStartIndex(currentLine),
                    allChars.size() - 1 - getLineStartIndex(currentLine)
            ));

            return ranges;
        }

        /**
         * 获取字符位置信息
         */
        public List<CharPosition> getCharPositions(int startIndex, int endIndex) {
            List<CharPosition> positions = new ArrayList<>();
            if (allChars.isEmpty() || startIndex < 0 || endIndex >= allChars.size()) {
                return positions;
            }

            int currentStart = startIndex;

            while (currentStart <= endIndex) {
                TextCharInfo startChar = allChars.get(currentStart);
                int currentLine = startChar.sourceLine.lineIndex;
                int lineStartIndex = currentStart;
                int lineEndIndex = currentStart;

                // 找到同一行的结束位置
                while (lineEndIndex <= endIndex &&
                        allChars.get(lineEndIndex).sourceLine.lineIndex == currentLine) {
                    lineEndIndex++;
                }
                lineEndIndex--;

                // 创建位置信息
                CharPosition pos = new CharPosition();
                pos.lineIndex = currentLine;
                pos.startCharIndex = lineStartIndex - getLineStartIndex(currentLine);
                pos.endCharIndex = lineEndIndex - getLineStartIndex(currentLine);
                pos.rect = calculateRect(lineStartIndex, lineEndIndex);
                positions.add(pos);

                currentStart = lineEndIndex + 1;
            }

            return positions;
        }

        private int getLineStartIndex(int lineIndex) {
            for (int i = 0; i < allChars.size(); i++) {
                if (allChars.get(i).sourceLine.lineIndex == lineIndex) {
                    return i;
                }
            }
            return 0;
        }

        private Rectangle calculateRect(int start, int end) {
            TextCharInfo first = allChars.get(start);
            TextCharInfo last = allChars.get(end);

            double minY = Double.MAX_VALUE;
            double maxY = Double.MIN_VALUE;

            for (int i = start; i <= end; i++) {
                TextCharInfo ci = allChars.get(i);
                minY = Math.min(minY, ci.getY());
                maxY = Math.max(maxY, ci.getY() + ci.height);
            }

            return new Rectangle(
                    first.getX(),
                    minY,
                    last.getX() + last.getWidth(),
                    maxY
            );
        }
    }

    /**
     * 字符位置信息
     */
    static class CharPosition {
        int lineIndex;         // 行号
        int startCharIndex;    // 行内起始位置
        int endCharIndex;      // 行内结束位置
        Rectangle rect;        // 坐标信息
    }

    /**
     * 行范围信息
     */
    static class LineRange {
        int lineIndex;         // 行号
        int startCharIndex;    // 行内起始位置
        int endCharIndex;      // 行内结束位置

        public LineRange(int lineIndex, int startCharIndex, int endCharIndex) {
            this.lineIndex = lineIndex;
            this.startCharIndex = startCharIndex;
            this.endCharIndex = endCharIndex;
        }
    }

    /**
     * 行信息类
     */
    @Data
    static class TextLine {
        // 行号（从0开始）
        private int lineIndex;
        private final double y;
        private final List<TextFragment> fragments = new ArrayList<>();
        private final List<TextCharInfo> characters = new ArrayList<>();

        public TextLine(double y) {
            this.y = y;
        }

        public void addFragment(TextFragment fragment) {
            fragments.add(fragment);
            extractCharacters(fragment);
        }

        private void extractCharacters(TextFragment fragment) {
            String text = fragment.getText();
            double x = fragment.getPosition().getXIndent();
            double fontSize = fragment.getTextState().getFontSize();
            double charWidth = fragment.getRectangle().getWidth() / text.length();

            for (int i = 0; i < text.length(); i++) {
                characters.add(new TextCharInfo(
                        x + i * charWidth,
                        y,
                        charWidth,
                        fontSize,
                        this // 记录来源行
                ));
            }
        }

        public String getText() {
            StringBuilder sb = new StringBuilder();
            for (TextFragment f : fragments) {
                sb.append(f.getText());
            }
            return sb.toString();
        }

        public double getLineHeight() {
            return !fragments.isEmpty() ? fragments.get(0).getTextState().getFontSize() : 12;
        }
    }

    /**
     * 字符信息类（增强版）
     */
    static class TextCharInfo {
        private final double x;
        private final double y;
        private final double width;
        private final double height;
        private final TextLine sourceLine; // 来源行信息

        public TextCharInfo(double x, double y, double width, double height, TextLine sourceLine) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.sourceLine = sourceLine;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getWidth() {
            return width;
        }

        public double getHeight() {
            return height;
        }
    }
}