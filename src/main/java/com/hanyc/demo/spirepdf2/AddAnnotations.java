package com.hanyc.demo.spirepdf2;

import com.spire.pdf.*;
import com.spire.pdf.annotations.*;
import com.spire.pdf.graphics.*;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Date;

public class AddAnnotations {
    public static void main(String[] args) throws Exception{
        //创建PDF文档，添加一页
        PdfDocument pdf = new PdfDocument();
        PdfPageBase page = pdf.getPages().add();
        String targetPath = "D:\\del\\nraq2\\a - " + System.currentTimeMillis() + ".pdf";
        //添加箭头标注
        String text1 = "this is Arrow annotation";//指定文本字符串内容
        PdfFont font = new PdfFont(PdfFontFamily.Helvetica, 20);//创建字体
        PdfSolidBrush brush1 = new PdfSolidBrush(new PdfRGBColor(Color.blue));//创建画刷
        PdfStringFormat Alignment = new PdfStringFormat(PdfTextAlignment.Left, PdfVerticalAlignment.Middle);//设置文本对齐方式
        page.getCanvas().drawString(text1, font, brush1, 50, 70, Alignment);//绘制文本到页面

        Dimension2D dimension = font.measureString(text1);
        Rectangle2D.Float bounds = new Rectangle2D.Float(50, 50, (float) dimension.getWidth(), (float) dimension.getHeight());
        int[] linePoints = new int[]{92, (int) (page.getSize().getHeight() - bounds.getY() - 90),
                (int) (92 + bounds.getWidth()), (int) (page.getSize().getHeight() - bounds.getY() - 90)};
        PdfLineAnnotation annotation1 = new PdfLineAnnotation(linePoints, "LineArrow annotation test");
        annotation1.setBeginLineStyle(PdfLineEndingStyle.OpenArrow); 
        annotation1.setEndLineStyle(PdfLineEndingStyle.OpenArrow);
        annotation1.setBackColor(new PdfRGBColor(Color.red));
        annotation1.setCaptionType(PdfLineCaptionType.Inline);
        annotation1.setLineCaption(true);
        ((PdfNewPage) page).getAnnotations().add(annotation1);//添加标注

        //添加云朵标注
        String text2 = "this is Cloud annotation";
        PdfBrush brush2 = PdfBrushes.getBlue();
        page.getCanvas().drawString(text2, font, brush2, 50, 200);

        Point2D point2D[] = new Point2D[]{
                new Point2D.Float(30, 200),
                new Point2D.Float(300, 180),
                new Point2D.Float(300, 250),
                new Point2D.Float(30, 220),
                new Point2D.Float(30, 200)
        };
        PdfPolygonAnnotation annotation2 = new PdfPolygonAnnotation(page, point2D);
        annotation2.setText("PolygonCloud annotation test");
        annotation2.setAuthor("E-iceblue");
        annotation2.setSubject("test");
        annotation2.setModifiedDate(new Date());
        annotation2.setBorderEffect(PdfBorderEffect.Big_Cloud);//设置边框为云朵状的标注
        annotation2.setLocation(new Point2D.Float(190, 230));
        annotation2.setColor(new PdfRGBColor(new Color(34,139,34)));
        ((PdfNewPage) page).getAnnotations().add(annotation2);//添加标注


        //添加椭圆标注
        String text3 = "this is Circle annotation";
        PdfBrush brush3 = PdfBrushes.getBlue();
        Dimension2D dimension2D = font.measureString(text3);
        dimension2D.setSize(dimension2D.getWidth() + 35, dimension2D.getHeight() + 20);
        page.getCanvas().drawString(text3, font, brush3, 50, 300);

        Rectangle2D.Float annotationBounds1 = new Rectangle2D.Float();
        annotationBounds1.setFrame(new Point2D.Float(36, (float) 290), dimension2D);
        PdfSquareAndCircleAnnotation annotation3 = new PdfSquareAndCircleAnnotation(annotationBounds1);
        annotation3.setSubType(PdfSquareAndCircleAnnotationType.Circle);//设置椭圆标注
        float[] f1 = {0.5f, 0.5f, 0.5f, 0.5f};
        annotation3.setRectangularDifferenceArray(f1);
        annotation3.setText("Circle annotation test");
        annotation3.setColor(new PdfRGBColor(new Color(255,0,0)));
        annotation3.setModifiedDate(new Date());
        annotation3.setName("*****");
        LineBorder border1 = new LineBorder();
        border1.setBorderWidth(2);
        annotation3.setLineBorder(border1);
        ((PdfNewPage) page).getAnnotations().add(annotation3);//添加标注

        //添加矩形标注
        String text4 = "this is Square annotation";
        PdfBrush brush4 = PdfBrushes.getBlue();
        Dimension2D dimension4 = font.measureString(text4);
        dimension2D.setSize(dimension2D.getWidth() + 80, dimension2D.getHeight() + 20);
        page.getCanvas().drawString(text4, font, brush4, 50, 400);

        Rectangle2D.Float annotationBounds2 = new Rectangle2D.Float();
        annotationBounds2.setFrame(new Point2D.Float(50, (float) 400), dimension4);//矩形位置
        PdfSquareAndCircleAnnotation annotation4 = new PdfSquareAndCircleAnnotation(annotationBounds2);
        annotation4.setSubType(PdfSquareAndCircleAnnotationType.Square);//设置矩形标注
        float[] f2 = {0.5f, 0.5f, 0.5f, 0.5f};
        annotation4.setRectangularDifferenceArray(f2);
        annotation4.setText("Square annotation test");
        annotation4.setColor(new PdfRGBColor(new Color(255,0,255)));
        annotation4.setModifiedDate(new Date());
        LineBorder border2 = new LineBorder();
        border2.setBorderWidth(1);
        annotation4.setLineBorder(border2);
        ((PdfNewPage) page).getAnnotations().add(annotation4);

        //添加连接线标注
        String text5 = "this is Connected lines annotation";
        PdfBrush brush5 = PdfBrushes.getBlue();
        page.getCanvas().drawString(text5, font, brush5, 50, 465);
        Point2D pointzd[] = new Point2D[]{
                new Point2D.Float(30, 470),
                new Point2D.Float(350, 450),
                new Point2D.Float(350, 520),
                new Point2D.Float(30, 490),
                new Point2D.Float(30, 470)
        };
        PdfPolygonAnnotation annotation5 = new PdfPolygonAnnotation(page, pointzd);
        annotation5.setText("Connected Lines annotation test");
        annotation5.setAuthor("冰蓝");
        annotation5.setSubject("test");
        annotation5.setModifiedDate(new Date());
        annotation5.setBorderEffect(PdfBorderEffect.None);
        annotation5.setLocation(new Point2D.Float(190, 230));
        annotation5.setColor(new PdfRGBColor(new Color(255,215,0)));
        ((PdfNewPage) page).getAnnotations().add(annotation5);

        //保存文档
        pdf.saveToFile(targetPath);
        pdf.dispose();
    }
}
