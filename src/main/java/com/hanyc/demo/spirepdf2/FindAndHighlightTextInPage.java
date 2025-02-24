package com.hanyc.demo.spirepdf2;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.texts.PdfTextFinder;
import com.spire.pdf.texts.PdfTextFragment;
import com.spire.pdf.texts.TextFindParameter;
import java.awt.*;
import java.util.EnumSet;

public class FindAndHighlightTextInPage
{
    public static void main(String[] args) {
        String filePath = "D:\\del\\nraq2\\a.pdf";
        String targetPath = "D:\\del\\nraq2\\a - " + System.currentTimeMillis() + ".pdf";
        // 创建一个PdfDocument对象
        PdfDocument doc = new PdfDocument();

        // 加载一个PDF文件
        doc.loadFromFile(filePath);

        // 获取特定页面
        PdfPageBase page = doc.getPages().get(0);

        // 基于该页面创建一个PdfTextFinder对象
        PdfTextFinder finder = new PdfTextFinder(page);

        // 指定查找选项
        finder.getOptions().setTextFindParameter(EnumSet.of(TextFindParameter.WholeWord));

        // 查找指定文本的实例
        java.util.List<PdfTextFragment> results = finder.find("表现包括社交互动困难");


        // 遍历查找结果
        for (PdfTextFragment text : results) {
            // 高亮文本
            text.highLight(Color.orange);
        }

        // 将文档保存到不同的PDF文件
        doc.saveToFile(targetPath);

        // 释放资源
        doc.dispose();
    }
}
