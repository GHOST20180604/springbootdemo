package com.hanyc.demo.sensitive;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.http.ssl.TrustAnyHostnameVerifier;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.event.SyncReadListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.hanyc.demo.util.DocReadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

@Slf4j
public class SensitiveCheck {
    public static void main(String[] args) throws IOException {
        String word = "绝密、机密、秘密、此件不公开、★";
        String[] split = word.split("、");
        TimeInterval totleTimer = DateUtil.timer();
        ExcelReader excelReader = null;
        ByteArrayOutputStream bos = null;
        InputStream inputStream = null;
        Workbook workbook = null;
        ExcelReaderBuilder excelReaderBuilder = EasyExcel.read("C:\\Users\\hanyu\\Desktop\\需要校验的文件.xlsx");
        excelReader = excelReaderBuilder.build();
        log.info("excelReaderBuilder. {} ms", totleTimer.interval());
//            List<ReadSheet> sheets = excelReader.excelExecutor().sheetList();
        SyncReadListener sheetListener = new SyncReadListener();
        //从excel第三行读取excel
        ReadSheet currentSheet = EasyExcel.readSheet(0).headRowNumber(0).registerReadListener(sheetListener).build();
        excelReader.read(currentSheet);
        log.info("excelReader. ,{} ms", totleTimer.interval());
        // 校验表头
        List<Object> list = sheetListener.getList();
        for (int x = 1; x < list.size(); x++) {
            if (list.get(x) != null) {
                Map<Integer, Object> linkMap = (Map<Integer, Object>) list.get(x);
                String url2 = String.valueOf(linkMap.get(2));
                String url3 = String.valueOf(linkMap.get(3));
                String url4 = String.valueOf(linkMap.get(4));
                String url5 = String.valueOf(linkMap.get(5));
                String url = url3 + url2;
                if (url.endsWith("pdf")) {
                    inputStream = get(url);
                    if (inputStream == null || inputStream.available() == 0) {
                        System.out.println("文件下载失败: 链接: " + url + "_______ 库名: " + url4 + "_______ 表名: " + url5);
                        continue;
                    }
                    String contentDoc = DocReadUtil.getContentDocxPdf(inputStream);
                    if (StringUtils.isNotEmpty(contentDoc)) {
                        for (String str : split) {
                            if (contentDoc.contains(str)) {
                                System.out.println("含有敏感词:" + str + "----" + contentDoc);
                            }
                        }
                        System.out.println("文件下载成功,不含敏感词:{}" + url + "_______ 库名: " + url4 + "_______ 表名: " + url5);
                    } else {
                        System.out.println("文件下载成功,但是不含文字 {}" + url + "_______ 库名: " + url4 + "_______ 表名: " + url5);
                    }
                }
            }
        }
        log.info("checkTitle  {} ms", totleTimer.interval());
    }

    /**
     * get方式请求服务器(https协议)
     *
     * @param url 请求地址
     * @return
     */
    public static InputStream get(String url) {
//        log.debug("开始下载文件:{}", url);
        URL httpUrl = null;
        HttpsURLConnection conn = null;
        SSLContext sc = null;
        try {
            httpUrl = new URL(url);
            sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
            conn = (HttpsURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            //conn.setRequestProperty("Authorization", "xxxxxx" + token)
            conn.connect();
            return conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
//            log.error("get error:", e);
        }
        return null;
    }

    private static class TrustAnyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }
}
