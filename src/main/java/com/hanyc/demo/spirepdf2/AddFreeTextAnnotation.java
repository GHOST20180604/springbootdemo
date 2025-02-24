package com.hanyc.demo.spirepdf2;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.annotations.PdfAnnotationBorder;
import com.spire.pdf.annotations.PdfFreeTextAnnotation;
import com.spire.pdf.graphics.PdfRGBColor;
import com.spire.pdf.texts.PdfTextFinder;
import com.spire.pdf.texts.PdfTextFragment;
import com.spire.pdf.texts.TextFindParameter;
import com.spire.pdf.graphics.PdfTrueTypeFont;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.EnumSet;
import java.util.List;

public class AddFreeTextAnnotation {

   public static void main(String[] args) {
       String filePath = "D:\\del\\nraq2\\a.pdf";
       String targetPath = "D:\\del\\nraq2\\a - " + System.currentTimeMillis() + ".pdf";
       // 创建一个 PdfDocument 对象
       PdfDocument doc = new PdfDocument();

       // 加载 PDF 文件
       doc.loadFromFile(filePath);

       // 获取特定页面
       PdfPageBase page = doc.getPages().get(0);

       // 基于页面创建 PdfTextFinder 对象
       PdfTextFinder finder = new PdfTextFinder(page);

       // 设置查找选项
       EnumSet<TextFindParameter> parameterSet = EnumSet.of(TextFindParameter.WholeWord, TextFindParameter.IgnoreCase);
       finder.getOptions().setTextFindParameter(parameterSet);

       // 查找指定文本的实例
       List<PdfTextFragment> fragments = finder.find("最直观的优势就是高效性");

       // 获取第一个实例
       PdfTextFragment textFragment = fragments.get(0);

       // 获取文本的边界
       Rectangle2D rect = textFragment.getBounds()[0];

       // 指定添加注释的 x 和 y 坐标
       double x = rect.getMaxX() + 3;
       double y = rect.getMaxY();

       // 创建一个自由文本注释
       Rectangle2D rectangle = new Rectangle2D.Double(x, y, 170, 40);
       PdfFreeTextAnnotation textAnnotation = new PdfFreeTextAnnotation(rectangle);

       // 设置注释的内容
       textAnnotation.setText("这是一个文本注释\r由 Spire.PDF for Java 添加");

       // 设置注释的其他属性
       // 使用 PdfTrueTypeFont 设置字体为 "华文中宋"（系统字体）并指定加粗
       PdfTrueTypeFont font = new PdfTrueTypeFont(new Font("华文中宋", Font.BOLD, 12));
       PdfAnnotationBorder border = new PdfAnnotationBorder(1f);
       textAnnotation.setFont(font);
       textAnnotation.setBorder(border);
       textAnnotation.setBorderColor(new PdfRGBColor(Color.blue));
       textAnnotation.setColor(new PdfRGBColor(Color.green));
       textAnnotation.setOpacity(1.0f);

       // 将注释添加到注释集合中
       page.getAnnotations().add(textAnnotation);

       // 保存结果到文件
       doc.saveToFile(targetPath);

       // 释放资源
       doc.dispose();
   }
}
