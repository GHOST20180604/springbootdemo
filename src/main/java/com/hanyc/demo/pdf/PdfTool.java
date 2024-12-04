package com.hanyc.demo.pdf;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.texts.PdfTextExtractOptions;
import com.spire.pdf.texts.PdfTextExtractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author ：hanyc
 * @date ：2024/1/25 11:20
 * @description：
 */
public class PdfTool {
    public static void main(String[] args) throws IOException
    {
        // 创建 PdfDocument 对象
        PdfDocument doc = new PdfDocument();

        // 加载 PDF 文件
        doc.loadFromFile("D:\\del\\nraq2\\aaa2.pdf");

        // 获取第一页，遍历文档所有页便可提取文档所有文本内容
        PdfPageBase page = doc.getPages().get(0);

        // 创建PdfTextExtractor 对象
        PdfTextExtractor textExtractor = new PdfTextExtractor(page);

        // 创建PdfTextExtractOptions 对象
        PdfTextExtractOptions extractOptions = new PdfTextExtractOptions();

        // 从页面中提取文本
        String text = textExtractor.extract(extractOptions);

        // 写入到 txt 文件中
        Files.write(Paths.get("D:\\del\\nraq2\\aaa2.txt"), text.getBytes());
        doc.saveToFile("D:\\del\\nraq2\\aaa2.docx", FileFormat.DOCX);
        // 释放PdfDocument对象
        doc.dispose();
    }

}
