package com.hanyc.demo.asposepdf1;


import com.aspose.pdf.Document;
import com.aspose.pdf.SaveFormat;
import com.aspose.words.PdfSaveOptions;

/**
 * @author ：hanyc
 * @date ：2024/1/25 11:20
 * @description：
 */
public class PdfTool2 {
    public static void main(String[] args) throws Exception {
        String filePath = "D:\\del\\nraq2\\a.pdf";
        String targetPath = "D:\\del\\nraq2\\a2 - " + System.currentTimeMillis() + ".docx";
        // 使用PDF文件路径或流创建一个PdfToWordConverter实例
        Document pdfDocument = new Document(filePath);

//将文档另存为 DOCX
        pdfDocument.save(targetPath, SaveFormat.DocX);


        String filePath2 = "D:\\del\\nraq2\\a2 - 1740369091619.docx";
        String targetPath2 = "D:\\del\\nraq2\\a2 - " + System.currentTimeMillis() + ".pdf";
        // 使用PDF文件路径或流创建一个PdfToWordConverter实例
        com.aspose.words.Document pdfDocument2 = new com.aspose.words.Document(filePath2);
//创建 PDF 保存选项对象
//        PdfSaveOptions saveOptions = new PdfSaveOptions();

//将文档另存为 DOCX
        pdfDocument2.save(targetPath2);

    }


}
