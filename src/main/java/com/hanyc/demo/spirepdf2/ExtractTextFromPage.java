package com.hanyc.demo.spirepdf2;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.texts.PdfTextExtractOptions;
import com.spire.pdf.texts.PdfTextExtractor;
import java.io.IOException;

public class ExtractTextFromPage
{
    public static void main(String[] args) throws IOException
    {
        String filePath = "D:\\del\\nraq2\\a.pdf";
        // 创建 PdfDocument 对象
        PdfDocument doc = new PdfDocument();

        // 加载 PDF 文件
        doc.loadFromFile(filePath);

        // 获取第一页，遍历文档所有页便可提取文档所有文本内容
        PdfPageBase page = doc.getPages().get(0);

        // 创建PdfTextExtractor 对象
        PdfTextExtractor textExtractor = new PdfTextExtractor(page);

        // 创建PdfTextExtractOptions 对象
        PdfTextExtractOptions extractOptions = new PdfTextExtractOptions();

        // 从页面中提取文本
        String text = textExtractor.extract(extractOptions);
        System.out.println(text);
        // 写入到 txt 文件中
//        Files.write(Paths.get("Extracted.txt"), text.getBytes());

        // 释放PdfDocument对象
        doc.dispose();
    }
}
