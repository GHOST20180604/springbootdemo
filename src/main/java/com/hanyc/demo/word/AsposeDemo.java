package com.hanyc.demo.word;

import com.aspose.words.*;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.data.Texts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author ：hanyc
 * @date ：2023/7/24 14:57
 * @description： POI-TL 测试类demo
 */
public class AsposeDemo {

    public static void main(String[] args) throws Exception {
        String filePath = "D:\\del\\poi-tl\\目标poi-tl-模版1711508838507.docx";
        String targetPath = "D:\\del\\poi-tl\\目标poi-tl-模版1711508838507" + System.currentTimeMillis() + ".pdf";
        //加载已设置大纲级别的测试文档
        long start = System.currentTimeMillis();
        Document doc = new Document(filePath);
//        doc.updateFields();

//        // TODO: 2023/7/26  使用aspose-words 只更新页码
//        for (Field field : doc.getRange().getFields()) {
//            if (field.getType() == FieldType.FIELD_TOC) {
//                FieldToc toc = (FieldToc) field;
//                toc.updatePageNumbers(); //定位toc只更新页码
//            } else {
//                field.update();
//            }
//        }
        doc.save(targetPath, SaveFormat.PDF);//这里执行操作

    }

}
