package com.hanyc.demo.spirepdf2;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.annotations.*;
import com.spire.pdf.graphics.PdfRGBColor;
import com.spire.pdf.texts.PdfTextFinder;
import com.spire.pdf.texts.PdfTextFragment;
import com.spire.pdf.texts.TextFindParameter;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.EnumSet;
import java.util.List;

public class AddShapeAnnotation {

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
       List<PdfTextFragment> fragments = finder.find("和学习自闭症障碍");

       // 获取第一个实例
       PdfTextFragment textFragment = fragments.get(0);

       // 获取文本的边界
       Rectangle2D rect = textFragment.getBounds()[0];

       // 获取添加注释的坐标
       double left = rect.getMinX();
       double top = rect.getMinY();
       double right = rect.getMaxX();
       double bottom = rect.getMaxY();

       // 创建一个形状注释
       PdfPolygonAnnotation polygonAnnotation = new PdfPolygonAnnotation(page, new Point2D[]{
               new Point2D.Double(left, top),
               new Point2D.Double(right, top),
               new Point2D.Double(right + 5, bottom),
               new Point2D.Double(left - 5, bottom),
               new Point2D.Double(left, top)
       });

       // 设置注释的边框
       PdfAnnotationBorder border = new PdfAnnotationBorder(1f);
       polygonAnnotation.setBorder(border);
       polygonAnnotation.setBorderEffect(PdfBorderEffect.Small_Cloud);
       polygonAnnotation.setColor(new PdfRGBColor(Color.green));

       // 设置注释的文本
       polygonAnnotation.setText("这是一个多边形注释\r由 Spire.PDF for Java 添加");

       // 将注释添加到注释集合中
       page.getAnnotations().add(polygonAnnotation);

       // 保存结果到文件
       doc.saveToFile(targetPath);

       // 释放资源
       doc.dispose();
   }
}
