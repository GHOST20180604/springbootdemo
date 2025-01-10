package com.hanyc.demo.word;//package com.scientific.thesaurus.utill;

import com.aspose.words.*;
import com.spire.doc.DocumentObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * @author ：hanyc
 * @description：word工具类 使用 aspose.words 给word 审核模式修改内容.
 *
 * @date ：2023/2/13 15:25
 */
@Slf4j
@Component
public class WordUtilDemo {

    public static void main(String[] args) throws Exception {
        String filePath = "D:\\del\\nraq2\\内容安全审核-样例.docx";
        String targetPath = "D:\\del\\nraq2\\内容安全审核-样例 - " + System.currentTimeMillis() + ".docx";
        Document doc = new Document("D:\\del\\nraq2\\aa (3).docx");
        String part0 = "(?<=^.{" + 40 + "}).{" + 8 + "}";
        Pattern pattern0 = Pattern.compile(part0);
        Paragraph firstParagraph0 = doc.getSections().get(0).getBody().getFirstParagraph();


        doc.setTrackRevisions(true);
        doc.startTrackRevisions("韩云川");

        String part = "(?<=^.{" + 40 + "}).{" + 8 + "}";
        Pattern pattern = Pattern.compile(part);
        Paragraph firstParagraph = doc.getSections().get(0).getBody().getFirstParagraph();

        Range range = firstParagraph.getRange();
        FieldCollection fields = range.getFields();

        String replacement = "---------------";

        FindReplaceOptions options = new FindReplaceOptions();
        final boolean[] isFirstReplacement = {true};
        options.setReplacingCallback(new IReplacingCallback() {
            @Override
            public int replacing(ReplacingArgs e) throws Exception {
                if (isFirstReplacement[0]) {
                    // 只替换第一个匹配项
                    e.setReplacement(replacement);
                    isFirstReplacement[0] = false;  // 替换第一个后，更新标志
                    return ReplaceAction.REPLACE;  // 执行替换
                }
                // 停止后续匹配项的替换
                return ReplaceAction.SKIP;
            }

        });

        int replace = range.replace(pattern, replacement, options);
        System.out.println("replace " + replace);
//        range.delete();
        doc.stopTrackRevisions();


        // 第二个段落添加批注
        doc.startTrackRevisions("韩云川22222");


        String part2 = "(?<=^.{" + 3 + "}).{" + 5 + "}";
        Pattern pattern2 = Pattern.compile(part2);
        Range range1 = doc.getSections().get(0).getBody().getParagraphs().get(1).getRange();


        String replacement2 = "---------------";

        FindReplaceOptions options2 = new FindReplaceOptions();
        final boolean[] isFirstReplacement2 = {true};
        options2.setReplacingCallback(new IReplacingCallback() {
            @Override
            public int replacing(ReplacingArgs e) throws Exception {
                if (isFirstReplacement2[0]) {
                    // 只替换第一个匹配项
                    e.setReplacement(replacement2);
                    isFirstReplacement2[0] = false;  // 替换第一个后，更新标志
                    return ReplaceAction.REPLACE;  // 执行替换
                }
                // 停止后续匹配项的替换
                return ReplaceAction.SKIP;
            }
        });

        int replace2 = range1.replace(pattern2, replacement2, options2);
        System.out.println("replace " + replace2);
//        range.delete();
        doc.stopTrackRevisions();


        // 第一个表格
        // 第二个段落添加批注
//        doc.startTrackRevisions("韩云川33333");
//
//
//        String part3 = "(?<=^.{" + 3 + "}).{" + 3 + "}";
//        Pattern pattern3 = Pattern.compile(part3);
//        Range range3 = doc.getSections().get(0).getBody().getTables().get(0).getRows().get(0).getCells().get(0).getRange();
//
//
//        String replacement3 = "---------------";
//
//        FindReplaceOptions options3 = new FindReplaceOptions();
//        final boolean[] isFirstReplacement3 = {true};
//        options3.setReplacingCallback(new IReplacingCallback() {
//            @Override
//            public int replacing(ReplacingArgs e) throws Exception {
//                if (isFirstReplacement3[0]) {
//                    // 只替换第一个匹配项
//                    e.setReplacement(replacement3);
//                    isFirstReplacement3[0] = false;  // 替换第一个后，更新标志
//                    return ReplaceAction.REPLACE;  // 执行替换
//                }
//                // 停止后续匹配项的替换
//                return ReplaceAction.SKIP;
//            }
//        });
//
//        int replace3 = range3.replace(pattern3, replacement3, options3);
//        System.out.println("replace " + replace3);
////        range.delete();
//        doc.stopTrackRevisions();


        doc.save("D:\\del\\nraq2\\内容安全审核-样例 - " + System.currentTimeMillis() + ".docx");
        System.out.println("完成");
        doc.cleanup();

    }


}
