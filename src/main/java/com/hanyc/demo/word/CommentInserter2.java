package com.hanyc.demo.word;

import com.aspose.words.*;

import java.util.Date;

/**
 * 使用  aspose.words 添加批注 .
 */
public class CommentInserter2 {

    public static void main(String[] args) throws Exception {
        String filePath = "D:\\del\\nraq2\\内容安全审核-样例.docx";
        String targetPath = "D:\\del\\nraq2\\内容安全审核-样例 - " + System.currentTimeMillis() + ".docx";
        // 创建一个新的文档
        Document doc = new Document(filePath);
        // 获取文档中所有的批注节点
        NodeCollection<Comment> comments = doc.getChildNodes(NodeType.COMMENT, true);
        // 删除所有批注
        comments.clear();
        // 在第4到第5个字符间添加批注
        Paragraph para = doc.getFirstSection().getBody().getFirstParagraph();
        Comment cmt = addComment(doc, para);
        para.appendChild(cmt);

        Comment cmt2 = addComment2(doc, para);
        para.appendChild(cmt2);

//        Paragraph para2 = doc.getFirstSection().getBody().getParagraphs().get(1);
//        Comment cmt3 = addComment2(doc, para2);
//        para2.appendChild(cmt3);
        // 保存文档
        doc.save(targetPath);

    }

    public static Comment addComment(Document doc, Paragraph para) throws Exception {
        int begin = 6; //需要加批注的文字 第一个字的偏移量
        int end = 7; //最后一个字的偏移量

        RunCollection runs = para.getRuns();
        int midFlag = 0;
        Comment cmt = new Comment(doc, "锐盾", "", new Date());
        cmt.setText("这里是批注");
        CommentRangeStart commentRangeStart = new CommentRangeStart(doc, cmt.getId());
        CommentRangeEnd commentRangeEnd = new CommentRangeEnd(doc, cmt.getId());
        int index = begin;//这个begin是你需要加批注的文字在这个段落里面的偏移量
        for (int i = 0; i < runs.getCount(); i++) {
            Run run = runs.get(i);
            int length = run.getText().length();
            midFlag = midFlag + length;
            if (midFlag >= index) {
                // 此时批注未跨越run 拆分当前run 插入comment
                // eg. midflag = 8  pos=[4,5]
                Run beforeRun = (Run) run.deepClone(true); // 批注前的run
                String text = run.getText();
                int off = index - (midFlag - length);
                beforeRun.setText(text.substring(0, off));
                run.setText(text.substring(off)); //批注的run
                run.getParentNode().insertBefore(beforeRun, run);
                run.getParentNode().insertBefore(index == begin ? commentRangeStart : commentRangeEnd, run);
                if (index == end) {
                    break;
                }
                index = end;
                midFlag = midFlag - length + beforeRun.getText().length();
            }
        }
        return cmt;
    }


    public static Comment addComment2(Document doc, Paragraph para) throws Exception {
        int begin = 10; //需要加批注的文字 第一个字的偏移量
        int end = 15; //最后一个字的偏移量

        RunCollection runs = para.getRuns();
        int midFlag = 0;
        Comment cmt = new Comment(doc, "admin", "", new Date());
        cmt.setText("这里是批注");
        CommentRangeStart commentRangeStart = new CommentRangeStart(doc, cmt.getId());
        CommentRangeEnd commentRangeEnd = new CommentRangeEnd(doc, cmt.getId());
        int index = begin;//这个begin是你需要加批注的文字在这个段落里面的偏移量
        for (int i = 0; i < runs.getCount(); i++) {
            Run run = runs.get(i);
            int length = run.getText().length();
            midFlag = midFlag + length;
            if (midFlag >= index) {
                // 此时批注未跨越run 拆分当前run 插入comment
                // eg. midflag = 8  pos=[4,5]
                Run beforeRun = (Run) run.deepClone(true); // 批注前的run
                String text = run.getText();
                int off = index - (midFlag - length);
                beforeRun.setText(text.substring(0, off));
                run.setText(text.substring(off)); //批注的run
                run.getParentNode().insertBefore(beforeRun, run);
                run.getParentNode().insertBefore(index == begin ? commentRangeStart : commentRangeEnd, run);
                if (index == end) {
                    break;
                }
                index = end;
                midFlag = midFlag - length + beforeRun.getText().length();
            }
        }
        return cmt;
    }
}
