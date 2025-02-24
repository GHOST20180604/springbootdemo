package com.hanyc.demo.spirepdf2;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.annotations.PdfAnnotationCollection;
import com.spire.pdf.annotations.PdfTextMarkupAnnotation;
import com.spire.pdf.graphics.PdfRGBColor;
import com.spire.pdf.texts.PdfTextFinder;
import com.spire.pdf.texts.PdfTextFragment;
import com.spire.pdf.texts.TextFindParameter;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.EnumSet;
import java.util.List;

public class AddMarkupAnnotation {

   public static void main(String[] args) {
       String filePath = "D:\\del\\nraq2\\a.pdf";
       String targetPath = "D:\\del\\nraq2\\a - " + System.currentTimeMillis() + ".pdf";
       // 创建一个 PdfDocument 对象
       PdfDocument doc = new PdfDocument();

       // 加载 PDF 文件
       doc.loadFromFile(filePath);

       // 获取特定页面
       PdfPageBase page = doc.getPages().get(0);

       // 在该页面上创建一个 PdfTextFinder 对象
       PdfTextFinder finder = new PdfTextFinder(page);

       // 设置查找选项
       EnumSet<TextFindParameter> parameterSet = EnumSet.of(TextFindParameter.IgnoreCase);
       finder.getOptions().setTextFindParameter(parameterSet);

       // 查找特定文本的实例
       List<PdfTextFragment> fragments = finder.find("最直观的优势就是高效性");

       // 获取第一个实例
       PdfTextFragment textFragment = fragments.get(0);

       // 指定注释文本
       String text = "这是一个文本标注注释";

       // 遍历文本的边界
       for (int i = 0; i < textFragment.getBounds().length; i++) {

           // 获取指定的边界
           Rectangle2D rect = textFragment.getBounds()[i];

           // 创建文本标记注释
           PdfTextMarkupAnnotation annotation = new PdfTextMarkupAnnotation(rect);

           // 设置注释文本
           annotation.setText(text);

           // 设置标记颜色
           annotation.setTextMarkupColor(new PdfRGBColor(Color.green));

           // 将注释添加到注释集合中
           PdfAnnotationCollection annotations = page.getAnnotations();
           if (annotations != null){
               annotations.add(annotation);
           }

       }

       // 保存结果文件
       doc.saveToFile(targetPath);

       // 释放资源
       doc.dispose();
   }
}
