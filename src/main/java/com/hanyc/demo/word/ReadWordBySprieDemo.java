package com.hanyc.demo.word;

import cn.hutool.core.util.StrUtil;
import com.hanyc.demo.dto.TextDTO;
import com.spire.doc.*;
import com.spire.doc.collections.DocumentObjectCollection;
import com.spire.doc.collections.SectionCollection;
import com.spire.doc.documents.Paragraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：hanyc
 * @date ：2023/7/24 14:57
 * @description： POI-TL 测试类demo
 */
public class ReadWordBySprieDemo {

    public static void main(String[] args) throws IOException {
        String filePath = "D:\\del\\nraq2\\【二改】第三册  太后临朝：通往巅峰之路  2025-2-13_44c4dc17c2f8a04ba352b313e984cc6c (2).docx";
        String targetPath = "D:\\del\\aaa\\报告模板-poi-tl" + System.currentTimeMillis() + ".docx";
        //创建Word文档
        com.spire.doc.Document doc = new com.spire.doc.Document();
        List<TextDTO> returnList = new ArrayList<>();
        int m = 0;
        try {
            doc.loadFromFile(filePath);
            // 先删除所有批注
            doc.getComments().clear();
            SectionCollection sections = doc.getSections();
            for (int s = 0; s < sections.getCount(); s++) {
                //获取第一节
                Section sec = sections.get(s);
                //删除页眉
                sec.getHeadersFooters().getHeader().getChildObjects().clear();
                //删除页脚
                sec.getHeadersFooters().getFooter().getChildObjects().clear();
                Body body = sec.getBody();
                DocumentObjectCollection childObjects = body.getChildObjects();
                // 第几个段落
                int paragraphIndex = 0;
                // 第几个表格
                int tableIndex = 0;
                for (int b = 0; b < childObjects.getCount(); b++) {
                    DocumentObject documentObject = childObjects.get(b);
                    if (documentObject instanceof Paragraph) {
                        Paragraph paragraph = (Paragraph) documentObject;
                        String text = paragraph.getText();
                        TextDTO textDTO = new TextDTO();
                        // 组装ID paragraph + 第几节+  第几段
                        textDTO.setWordCommentPosition("PARAGRAPH" + StrUtil.DASHED + s + StrUtil.DASHED + paragraphIndex++);
                        textDTO.setId(String.valueOf(m++));
                        textDTO.setText(text);
                        returnList.add(textDTO);
                    }
//                    if (documentObject instanceof Table) {
//                        Table table = (Table) documentObject;
//                        RowCollection rows = table.getRows();
//                        for (int j = 0; j < rows.getCount(); j++) {
//                            TableRow tableRow = rows.get(j);
//                            CellCollection cells = tableRow.getCells();
//                            for (int x = 0; x < cells.getCount(); x++) {
//                                TableCell tableCell = cells.get(x);
//                                ParagraphCollection tableCellParagraphs = tableCell.getParagraphs();
//                                for (int y = 0; y < tableCellParagraphs.getCount(); y++) {
//                                    Paragraph paragraph = tableCellParagraphs.get(y);
//                                    String text = paragraph.getText();
//                                    if (WARING_MESSAGE.equals(text)) {
//                                        continue;
//                                    }
//                                    TextDTO textDTO = new TextDTO();
//                                    // 组装ID table + 第几节+  第几个table + 第几行 + 当前行第几个单元格 + 单元格中第几段
//                                    textDTO.setWordCommentPosition(DocumentReviewConstant.TABLE + StrUtil.DASHED + s + StrUtil.DASHED + tableIndex + StrUtil.DASHED + j + StrUtil.DASHED + x + StrUtil.DASHED + y);
//                                    textDTO.setText(text);
//                                    textDTO.setId(String.valueOf(m++));
//                                    htmlList.add(textDTO);
//                                }
//                            }
//                        }
//                        tableIndex++;
//                    }
                }
            }
        } finally {
            doc.dispose();
        }
        for (TextDTO dto : returnList) {
            System.out.println(dto.getText());
        }
    }

}
