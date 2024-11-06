package com.hanyc.demo.util;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSON;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：hanyc
 * @date ：2024/1/25 9:33
 * @description： wkhtmltopdf 工具类
 */
@Slf4j
public class WkhtmltopdfTool {
    private static final String WKHTMLTOPDF_PATH = "D:\\ruanjian\\wkhtmltopdf\\bin\\wkhtmltoimage.exe"; // 替换为实际路径

    /**
     * html转pdf
     *
     * @param srcPath  html路径，可以是硬盘上的路径，也可以是网络路径
     * @param destPath 图片保存路径
     * @param width    宽度
     */
    public static void convert(String srcPath, String destPath, Integer width) throws IOException, InterruptedException {
        File file = new File(destPath);
        File parent = file.getParentFile();
        //如果pdf保存路径不存在，则创建路径
        if (!parent.exists()) {
            parent.mkdirs();
        }

        StringBuilder cmd = new StringBuilder();
        cmd.append(WKHTMLTOPDF_PATH);
        cmd.append(" ");
        // 去掉左右 边距
//        cmd.append(" --margin-left 0mm --margin-right 0mm  --margin-top 0mm  --margin-bottom 5mm ");
//        cmd.append("   --enable-local-file-access ");
        //设置页面上边距 (default 10mm)
//        cmd.append("  --margin-top 0mm ");
        //设置页面下边距 (default 10mm)
//        cmd.append("  --margin-bottom 0mm ");
        // (设置页眉和内容的距离,默认0)
//        cmd.append(" --header-spacing 0 ");
        // 添加页码
//        cmd.append("  --footer-center [page]/[topage] ");

//        1.--format.\<格式》:指定输出图像的格式。可以是PNG、JPEG、BMP等，默认为PNG。
        cmd.append(" --format png ");
        // 2 . –quality 75：就表示生成图片的质量为原来的 75%!
        cmd.append(" --quality 75 ");
//        3  --width \<宽度\>:设置输出图像的宽度。可以使用像素(如800px)或其他单位(如cm、mm等)指定，默认为 1024像素。
        if (width != null) {
            cmd.append(" --width ");
            cmd.append(width);
            cmd.append(" ");
        }
//        4 --height \<高度\>:设置输出图像的高度。同样可以使用像素或其他单位指定，默认为0，表示自适应高度。
//        cmd.append(" --height 600");
//        5 --crop-w \<宽度\>:将输入HI文档裁剪为指定宽度的图像。宽度单位与--width相同，默认为0，表示不进行裁剪。
//        6 --crop-h \高度\>:将输入HI文档裁剪为指定高度的图像。高度单位与--height相同，默认为0，表示不进行裁剪。
//        7 --crop-x\<x坐标\>:设置裁剪的左上角x坐标。默认为0。
//        8 --crop-y \<y坐标\>:设置裁剪的左上角y坐标。默认为0。
//        9. --no-outline:禁用轮廓线，即去掉输出图像中的边框线
//        10 .--no-background:禁用背景，即去掉输出图像中的背景色。
//        11 --disable-smart-width:禁用智能调整宽度，即不根据内容自适应调整宽度。
//        12 --transparent:将输出图像的背景色设置为透明。
//         13.--encoding<编码》>:设置HTML文档的字符编码
//        14.--quiet:静默模式，不输出任何日志信息。
//        15 --version:显示wkhtmltoimage的版本信息
        cmd.append(srcPath);
        cmd.append(" ");
        cmd.append(destPath);

        boolean result = true;
        try {
            log.info("执行命令:  {}", cmd.toString());
            Process proc = Runtime.getRuntime().exec(cmd.toString());
            HtmlToPdfThread error = new HtmlToPdfThread(proc.getErrorStream());
            HtmlToPdfThread output = new HtmlToPdfThread(proc.getInputStream());
            error.start();
            output.start();
            proc.waitFor();
        } catch (Exception e) {
            result = false;
            log.error("html转pdf fail:{}", e.getMessage(), e);
            throw e;
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException, TemplateException {
        String sourceFile = "D:\\code\\springbootdemo2\\src\\main\\resources\\template\\echart-test-1706154543908.html";
        String destFile = "D:\\code\\springbootdemo2\\src\\main\\resources\\template\\echart-test-1706154543908.png";
        WkhtmltopdfTool.convert(sourceFile, destFile,500);
    }

//    实现目标: java代码生成 pdf 携带echart图片.

//    1. 通过 poi-tl 工具 将word模版生成word文件
//    2. 通过 Freemarker 将ftl文件生成html(主要为了获取Echart.html.)
//    3. 通过 wkhtmltoimage 将 Echart.html 处理成 图片 Echart.png
//    4. 将 Echart.png 插入word文件中.
//    5. 将word转为pdf文件.
}
