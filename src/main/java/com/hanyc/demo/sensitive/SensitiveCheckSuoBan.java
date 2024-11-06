package com.hanyc.demo.sensitive;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.IoUtil;
import cn.hutool.http.ssl.TrustAnyHostnameVerifier;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.event.SyncReadListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.hanyc.demo.constants.FileConstant;
import com.hanyc.demo.entity.ExcelExportEntity;
import com.hanyc.demo.util.AhoCorasickAutomation;
import com.hanyc.demo.util.DocReadUtil;
import com.hanyc.demo.util.FileUtil;
import com.hanyc.demo.util.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 扫描所办内文件目录
 */
@Slf4j
public class SensitiveCheckSuoBan {

    /**
     * 读取目录下所有文件
     *
     * @param path
     */
    public static void readDirectory(String path, final List<ExcelExportEntity> list, List<String> rarList, List<String> zipList) {
        String word = "绝密、机密、秘密、此件不公开、★";
        final String[] split = word.split("、");

        AhoCorasickAutomation aca = new AhoCorasickAutomation(Arrays.asList(split));
        try {
            Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    // 打印文件路径
//                    System.out.println("File: " + file.toString());
                    InputStream inputStream = null;
                    ExcelExportEntity excelExportEntity = new ExcelExportEntity();
                    List<String> errorList = new ArrayList<>();
                    String path = file.toString();
                    try {
                        String suffix = path.substring(path.lastIndexOf(".") + 1);
                        suffix = suffix.toUpperCase();
                        inputStream = new FileInputStream(path);
                        if (FileConstant.SUFFIX_RAR.equals(suffix)) {
                            rarList.add(path);
                        }
                        if (FileConstant.SUFFIX_ZIP.equals(suffix)) {
                            zipList.add(path);
                        }
                        // 解析文件内容，
                        if (FileConstant.SUFFIX_PPT.equals(suffix)) {
                            String contentDoc = DocReadUtil.getContentPpt(path);
                            errorList.addAll(aca.find2ListKey(contentDoc));
                        }
                        if (FileConstant.SUFFIX_CSV.equals(suffix)) {
                            String contentDoc = DocReadUtil.getContentCsv(path);
                            errorList.addAll(aca.find2ListKey(contentDoc));
                        }
                        if (FileConstant.SUFFIX_XLSX.equals(suffix)) {
                            String contentDoc = DocReadUtil.getContentXlsx(path);
                            errorList.addAll(aca.find2ListKey(contentDoc));
                        }
                        if (FileConstant.SUFFIX_XLS.equals(suffix)) {
                            String contentDoc = DocReadUtil.getContentXls(path);
                            errorList.addAll(aca.find2ListKey(contentDoc));
                        }
                        if (FileConstant.SUFFIX_WPS.equals(suffix)) {
                            String contentDoc = DocReadUtil.getContentWps(path);
                            errorList.addAll(aca.find2ListKey(contentDoc));
                        }
                        if (FileConstant.SUFFIX_HTML.equals(suffix)) {
                            String contentDoc = DocReadUtil.getContentHtml(path);
                            errorList.addAll(aca.find2ListKey(contentDoc));
                        }
                        if (FileConstant.SUFFIX_PPTX.equals(suffix)) {
                            String contentDoc = DocReadUtil.getContentPpts(path);
                            errorList.addAll(aca.find2ListKey(contentDoc));
                        }
                        if (FileConstant.SUFFIX_TXT.equals(suffix)) {
                            String contentDoc = DocReadUtil.getContentTxt(path);
                            errorList.addAll(aca.find2ListKey(contentDoc));
                        }
                        if (FileConstant.SUFFIX_DOC.equals(suffix)) {
                            String contentDoc = DocReadUtil.getContentDoc(inputStream);
                            errorList.addAll(aca.find2ListKey(contentDoc));
                        }
                        if (FileConstant.SUFFIX_DOCX.equals(suffix)) {
                            String contentDoc = DocReadUtil.getContentDocx(inputStream);
                            errorList.addAll(aca.find2ListKey(contentDoc));
                        }
                        if (FileConstant.SUFFIX_PDF.equals(suffix)) {
                            String contentDoc = DocReadUtil.getContentDocxPdf(inputStream);
                            errorList.addAll(aca.find2ListKey(contentDoc));
                        }
                        if (FileConstant.SUFFIX_DOT.equals(suffix)) {
                            String contentDoc = DocReadUtil.getContentDot(path);
                            errorList.addAll(aca.find2ListKey(contentDoc));
                        }
                        excelExportEntity.setUrl(path);
                        excelExportEntity.setErrorItem(String.join(",", errorList));
                        list.add(excelExportEntity);
                    } catch (Exception e) {
                        excelExportEntity.setUrl(path);
                        excelExportEntity.setErrorItem("文件读取失败,失败原因:" + e.getMessage());
                        list.add(excelExportEntity);
                        e.printStackTrace();
                        log.error("识别文件错误:{} msg:{}", file, e.getMessage(), e);
                    } finally {
                        IoUtil.close(inputStream);
                    }
                    log.info("文件扫描完成 url:{} 含有风险词:{}", path, errorList);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    // 打印文件夹路径
//                    System.out.println("Directory: " + dir.toString());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        TimeInterval timer = DateUtil.timer();
        String path = "D:\\del\\test\\";
        String outPath = "D:\\del\\error.xlsx";
        String zipTempPath = "D:\\del\\checkSuoban\\";
        List<ExcelExportEntity> list = new ArrayList<>();
        List<String> zipList = new ArrayList<>();
        List<String> rarList = new ArrayList<>();
        FileUtil.createFile(new File(zipTempPath));
        FileUtil.createFile(new File(outPath));
        readDirectory(path, list, rarList, zipList);
        log.info("第一遍扫描文件: useTime: {}", timer.intervalRestart());
//        System.out.println(list);
        if (!zipList.isEmpty()) {
            log.info("含有zip文件");
            for (String zipPath : zipList) {
                String onePath_ = zipPath.replace(File.separator, "_").replace(".", "_").replaceAll(":", "_");
                if (onePath_.length() > 100) {
                    onePath_ = onePath_.substring(onePath_.length() - 100);
                }
                String zipTempPathOne = zipTempPath + onePath_ + "\\";
                FileUtil.createFile(new File(zipTempPathOne));
                try {
                    ZipUtils.unzip(zipPath, zipTempPathOne, "GBK");
                } catch (Exception e) {
                    log.info("解压文件失败,尝试使用UTF-8解压");
                    try {
                        ZipUtils.unzip(zipPath, zipTempPathOne, "UTF-8");
                    } catch (Exception e1) {
                        log.info("GBK 和 UTF-8 都解压文件失败");
                        ExcelExportEntity excelExportEntity = new ExcelExportEntity();
                        excelExportEntity.setUrl(zipPath);
                        excelExportEntity.setErrorItem("解压文件失败:" + e.getMessage());
                        list.add(excelExportEntity);
                    }
                }
            }
        }
        log.info("解压文件耗时: useTime: {}", timer.intervalRestart());
        rarList = new ArrayList<>();
        zipList = new ArrayList<>();
        readDirectory(zipTempPath, list, rarList, zipList);
        log.info("第二遍扫描文件: useTime: {}", timer.intervalRestart());
        FileUtil.deleteFileOrDirectory(new File(zipTempPath));

        //设置导出参数
        ExportParams params = new ExportParams();
        //设置excel类型，XSSF代表xlsx，HSSF代表xls
        params.setType(ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, ExcelExportEntity.class, list);
        OutputStream os = null;
        //将文件存入response对象中，返回给前端
        try {
            os = new FileOutputStream(outPath);
        } catch (Exception e) {
            log.error("导出excel失败，msg: {}", e.getMessage(), e);
        } finally {
            workbook.write(os);
            IoUtil.close(workbook);
            os.flush();
            IoUtil.close(os);

        }

    }

}
