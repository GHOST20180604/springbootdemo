package com.hanyc.demo.util;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author ：hanyc
 * @date ：2023/4/26 14:44
 * @description： 流处理日志输出工具类
 */
@Slf4j
public class HtmlToPdfThread extends Thread {
    private InputStream is;

    public HtmlToPdfThread(InputStream is) {
        this.is = is;
    }

    @Override
    public void run() {
        BufferedReader br = null;
        try {
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                //输出内容
                log.info(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("HtmlToPdfThread: ", e);
        } finally {
            IoUtil.close(is);
            IoUtil.close(br);
        }
    }
}
