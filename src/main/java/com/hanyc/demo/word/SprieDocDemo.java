//package com.hanyc.demo.word;
//
//import com.deepoove.poi.XWPFTemplate;
//import com.deepoove.poi.data.Pictures;
//import com.deepoove.poi.data.Texts;
//import com.spire.doc.Document;
//import com.spire.doc.FileFormat;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.HashMap;
//
///**
// * @author ：hanyc
// * @date ：2023/7/24 14:57
// * @description： POI-TL 测试类demo
// */
//public class SprieDocDemo {
//
//    public static void main(String[] args) throws IOException {
//        String filePath = "D:\\del\\aaa\\报告模板-poi-tl.docx";
//        String targetPath = "D:\\del\\aaa\\报告模板-poi-tl" + System.currentTimeMillis() + ".docx";
//        //创建Word文档
//        Document doc = new Document();
//        doc.loadFromFile(filePath);
//        //更新目录
//        doc.updateTableOfContents();
//        //保存结果文档
//        doc.saveToFile(targetPath, FileFormat.Docx);
//    }
//
//}
