package com.hanyc.demo.word;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.*;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.compress.utils.Lists;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：hanyc
 * @date ：2023/7/24 14:57
 * @description： POI-TL 测试类demo
 */
public class PoiTlDemo {


    public static void main(String[] args) throws IOException {
//        String filePath = "D:\\del\\aaa\\报告模板-poi-tl.docx";
        String targetPath = "D:\\del\\aaa\\报告模板-poi-tl" + System.currentTimeMillis() + ".docx";

        //主模版
//        String filePath = "D:\\del\\aaa\\报告模板-20230809.docx";
        String filePath = "D:\\del\\aaa\\报告模板.docx";
        //子模版
        String filePathSon = "D:\\del\\aaa\\附录E子模板.docx";
        //输出目标地址
//        String targetPath = "d:\\del\\aaa\\report_test1.docx";
//        String picPath =  "C:\\Users\\17244\\Pictures\\Camera Roll\\test.jpg";
        //根据word模板生成 数据封装
        Map<String, Object> datas = new HashMap<>();

        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
        List<TableDemo1> table1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TextRenderData sayi = Texts.of("Sayi").color("F3B624").create();
            TableDemo1 tableDemo1 = new TableDemo1(String.valueOf(i + 1), sayi, "栏目" + i + 1, "数据格式分类" + i + 1, "2023-10-10", "业务分类" + i + 1);
            table1.add(tableDemo1);
//            RowRenderData row0 = Rows.of("序号", "文章标题", "栏目", "数据格式分类", "发布时间", "业务分类").textColor("FFFFFF").bgColor("4472C4").center().create();
//////        // 第一行
//            RowRenderData row1 = Rows.create("1", "文章标题1", "栏目1", "数据格式分类1", "2023-10-10", "业务分类1");
//////        // 第二行
//            RowRenderData row2 = Rows.create("2", "文章标题2", "栏目2", "数据格式分类2", "2023-10-10", "业务分类2");
//            datas.put("table1", Tables.create(row0, row1));


        }


//        Configure config = Configure.builder()
//                .bind("table1", policy).build();
        //        XWPFTemplate template = XWPFTemplate.compile(filePath).render(datas);
//        template.writeAndClose(new FileOutputStream(targetPath));

        datas.put("table1", table1);

        //加载子模版
        List<Map<String, Object>> params = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> sonTem = new HashMap<>();
            List<TableDemo2> sonTableList = Lists.newArrayList();
            for (int y = 0; y < 10; y++) {
                int start = 2;
                int end = 7;

                String content = "中央和地方各级国家机关的公务人员（包括行使科技计划管理职能的其他人员）不得申报项目（课题）。";
                String startStr = content.substring(0, start);
                String substring = content.substring(start, end);
                String endStr = content.substring(end, content.length() - 1);
                TextRenderData substringRender = Texts.of(substring).color("F3B624").create();

                TableDemo2 tableDemo2 = new TableDemo2(String.valueOf(i + 1), startStr, substringRender, endStr, "文章标题" + y + 1, "栏目" + y + 1, "数据格式分类" + y + 1, "数据格式分类" + y + 1, "2023-10-10", "业务分类" + y + 1);
                sonTableList.add(tableDemo2);
            }

            sonTem.put("sonTableList", sonTableList);
            sonTem.put("title", "年度报告" + i);
            sonTem.put("bankuai", "板块名称" + i);
            sonTem.put("shijian", "2023-8-10" + i);
            sonTem.put("anquanjibie", "公开" + i);
            sonTem.put("gaojingdengji", "告警等级" + i);
            sonTem.put("fengxianzhishu", "一级" + i);
            params.add(sonTem);
        }
        DocxRenderData docxRenderData = Includes.ofLocal(filePathSon).setRenderModel(params).create();
        Configure config = Configure.builder().bind("table1", policy).bind("sonTableList", policy).build();
        //嵌入子模版
//        DocxRenderData docxRenderData = new DocxRenderData(new File(filePathSon), params);
        datas.put("tables", docxRenderData);
//        datas.put("nested", Includes.ofLocal("sub.docx").setRenderModel(subData).create());

        XWPFTemplate template = XWPFTemplate.compile(filePath, config).render(datas);
        template.writeAndClose(new FileOutputStream(targetPath));
    }

    @Data
    @Builder
    public static class TableDemo1 {
        private String title;
        private TextRenderData lanmu1;
        private String lanmu2;
        private String fenlei;
        private String time;
        private String fenlei2;
    }

    @Data
    @Builder
    public static class TaskInfo {
        private String informEmail;
    }

    @Data
    @Builder
    public static class TableDemo2 {
        private String index;
        private String startStr;
        private TextRenderData content;
        private String endStr;
        private String title;
        private String lanmu1;
        private String lanmu2;
        private String fenlei;
        private String time;
        private String fenlei2;
    }
}
