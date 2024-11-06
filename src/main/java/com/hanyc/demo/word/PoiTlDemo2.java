package com.hanyc.demo.word;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.style.Style;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * @author ：hanyc
 * @date ：2023/7/24 14:57
 * @description： POI-TL 测试类demo
 */
@Slf4j
public class PoiTlDemo2 {


    public static void main(String[] args) throws IOException {
//        官网文档地址  https://deepoove.com/poi-tl/
//        github 源码地址 https://github.com/Sayi/poi-tl
//        版本依赖
//1.11.1 Documentation，Apache POI5.1.0+，JDK1.8+
//
//1.10.x Documentation，Apache POI4.1.2，JDK1.8+
//
//1.9.x Documentation，Apache POI4.1.2，JDK1.8+
//
//1.8.x Documentation，Apache POI4.1.2，JDK1.8+
//
//1.7.x Documentation，Apache POI4.0.0+，JDK1.8+
//
//1.6.x Documentation，Apache POI4.0.0+，JDK1.8+
//
//1.5.x Documentation，Apache POI3.16+，JDK1.6+

        //存放要填充的数据
        Map<String, Object> datas = new HashMap<String, Object>();
        //模板地址
        String templateFilePath = "D:\\del\\poi-tl\\poi-tl-模版.docx";
        //生成文件的保存地址
        String destFilePath = "D:\\del\\poi-tl\\目标poi-tl-模版" + System.currentTimeMillis() + ".docx";

        //文字 这里介绍两种数据填充方式
        //1.可以设置一些通用样式
        Style style = Style.builder().buildUnderlineColor("00FF00").buildFontSize(18).buildColor("00FF00").build();
        datas.put("var1", Texts.of("内容1").style(style).create());
        datas.put("var2", Texts.of("超链接").link("http://deepoove.com").style(style).create());
        //2.文字可以通过Texts创建，也可以使用对象new TextRenderData("000000", "Sayi")
        datas.put("var3", new TextRenderData("000000", "内容3"));

        //图片
        datas.put("image", Pictures.ofUrl("https://img0.baidu.com/it/u=3232582821,3516640051&fm=253&fmt=auto&app=138&f=JPEG?w=625&h=500").size(100, 100).create());
        String json = "{\"theme\":\"macarons\",\"tooltip\":{\"trigger\":\"item\"},\"legend\":{\"orient\":\"vertical\",\"left\":\"left\"},\"series\":[{\"name\":\"Access From\",\"type\":\"pie\",\"radius\":\"50%\",\"data\":[{\"value\":1048,\"name\":\"Search Engine\"},{\"value\":29,\"name\":\"Direct\"},{\"value\":22,\"name\":\"Email\"},{\"value\":11,\"name\":\"Union Ads\"},{\"value\":2,\"name\":\"Video Ads\"}],\"label\":{\"formatter\":\"{a}\\n{c}\\n{d}%\"},\"emphasis\":{\"itemStyle\":{\"shadowBlur\":10,\"shadowOffsetX\":0,\"shadowColor\":\"rgba(0, 0, 0, 0.5)\"}}}]}";


        HttpRequest post = HttpUtil.createPost("http://172.16.8.51:3000/");
        post.body(json);
        // 获取发送请求后的响应对象
        HttpResponse execute = post.timeout(30000).execute();
        datas.put("streamImg", Pictures.ofStream(new ByteArrayInputStream(execute.bodyBytes()), PictureType.JPEG).size(600, 380).create());

        Student student1 = new Student();
        student1.setAge("12");
        student1.setIndex("1");
        student1.setName("张三");
        student1.setSex("男");
        Student student2 = new Student();
        student2.setAge("122");
        student2.setIndex("11");
        student2.setName("张三1");
        student2.setSex("男1");
        List<Student> studentList = ListUtil.of(student1, student2);
        datas.put("lists", studentList);

        // 插件列表,可以去官网查看，有列循环，还有行循环，这里是行循环实例
        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();

        //这里可以指定一个config类，用来指定一些规则，也可以改变模板中{{}}的这种格式
        Configure config = Configure.builder()
                .bind("lists", policy).build();
        // 执行模版文件
        XWPFTemplate compile = XWPFTemplate.compile(templateFilePath, config);
        // 渲染文件
        compile.render(datas);
        compile.writeToFile(destFilePath);

    }

    @Data
    public static class Student {
        private String age;
        private String index;
        private String name;
        private String sex;
    }

}
