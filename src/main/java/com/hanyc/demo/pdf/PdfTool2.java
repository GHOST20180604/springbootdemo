package com.hanyc.demo.pdf;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.conversion.PdfToWordConverter;
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
public class PdfTool2 {
    public static void main(String[] args) throws Exception {
        // 使用PDF文件路径或流创建一个PdfToWordConverter实例
        PdfToWordConverter converter = new PdfToWordConverter("D:\\del\\nraq2\\response.pdf");

        // 将PDF文件转换为Word文档，并保存到文件或流中
        converter.saveToDocx("D:\\del\\nraq2\\response.docx");

        // 释放资源
        converter.dispose();
    }


}
