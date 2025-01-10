package com.hanyc.demo.word;//package com.scientific.thesaurus.utill;

import com.aspose.words.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * @author ：hanyc
 * @description：word工具类 使用 aspose.words 解析word 内容
 * @date ：2023/2/13 15:25
 */
@Slf4j
@Component
public class WordUtilDemo2 {

    public static void main(String[] args) throws Exception {
        String filePath = "D:\\del\\nraq2\\内容安全审核-样例.docx";
        String targetPath = "D:\\del\\nraq2\\内容安全审核-样例 - " + System.currentTimeMillis() + ".docx";
        Document doc = new Document(filePath);
        SectionCollection sections = doc.getSections();
        // 获取文档中所有的批注节点
        NodeCollection<Comment> comments = doc.getChildNodes(NodeType.COMMENT, true);
        // 删除所有批注
        comments.clear();
//        doc.AcceptAllRevisions();
        RevisionCollection revisions = doc.getRevisions();
        // 拒绝所有修订
//        revisions.rejectAll();
        // 接受所有修订
        revisions.acceptAll();
        StringBuilder sb = new StringBuilder();
        for (int s = 0; s < sections.getCount(); s++) {
            //获取第一节
            Section sec = sections.get(s);
            //删除页眉
            sec.getHeadersFooters().clear();
//            sec.getHeadersFooters().getHeader().getChildObjects().clear();
//            //删除页脚
//            sec.getHeadersFooters().getFooter().getChildObjects().clear();
            Body body = sec.getBody();
            ParagraphCollection paragraphs = body.getParagraphs();
            for (int i = 0; i < paragraphs.getCount(); i++) {
                Paragraph paragraph = paragraphs.get(i);
                String text = paragraph.getText();
                System.out.println(text);
//                sb.append(text).append("\n");
            }
            TableCollection tables = body.getTables();
            System.out.println("---------------------------------");
            for (int j = 0; j < tables.getCount(); j++) {
                Table table = tables.get(j);
                RowCollection rows = table.getRows();
                for (int x = 0; x < rows.getCount(); x++) {
                    Row row = rows.get(x);
                    CellCollection cells = row.getCells();
                    for (int y = 0; y < cells.getCount(); y++) {
                        Cell nodes = cells.get(y);
                        ParagraphCollection paragraphsCell = nodes.getParagraphs();
                        for (int z = 0; z < paragraphsCell.getCount(); z++) {
                            Paragraph paragraph = paragraphsCell.get(z);
                            String text = paragraph.getText();
                            System.out.println(text);
                        }
                    }

                }
            }
        }
        System.out.println(sb);
    }

}
