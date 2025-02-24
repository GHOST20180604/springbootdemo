package com.hanyc.demo.spirepdf2;

import com.spire.pdf.*;
import com.spire.pdf.utilities.PdfTable;
import com.spire.pdf.utilities.PdfTableExtractor;

import java.io.IOException;

public class ExtractTable {
    public static void main(String[] args) throws IOException {
        //实例化PdfDocument类的对象
        PdfDocument pdf = new PdfDocument();
        String filePath = "D:\\del\\nraq2\\a.pdf";
        //加载PDF文档
        pdf.loadFromFile(filePath);

        //创建StringBuilder类的实例
        StringBuilder builder = new StringBuilder();

        //创建PdfTableExtractor类的对象
        PdfTableExtractor extractor = new PdfTableExtractor(pdf);

        //遍历每一页
        for (int page = 0; page < pdf.getPages().getCount(); page++) {
            //提取页面中的表格存入PdfTable[]数组

            PdfTable[] tableLists = extractor.extractTable(page);
            if (tableLists != null && tableLists.length > 0) {
                //遍历表格
                for (PdfTable table : tableLists) {

                    int row = table.getRowCount();//获取表格行
                    int column = table.getColumnCount();//获取表格列
                    for (int i = 0; i < row; i++) {
                        for (int j = 0; j < column; j++) {
                            //获取表格中的文本内容
                            String text = table.getText(i, j);

                            //将获取的text写入StringBuilder容器
                            builder.append(text + " ");
                            System.out.println(text);
                        }
//                        builder.append("\r\n");
//                        System.out.println(builder);
                    }
                }
            }
        }

        //保存为txt文档
//        FileWriter fileWriter = new FileWriter("ExtractedTable.txt");
//        fileWriter.write(builder.toString());
//        fileWriter.flush();
//        fileWriter.close();
    }
}
