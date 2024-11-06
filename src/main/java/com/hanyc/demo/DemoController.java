package com.hanyc.demo;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.IoUtil;
import com.hanyc.demo.entity.ExcelExportEntity;
import com.hanyc.demo.sensitive.SensitiveCheckSuoBan;
import com.hanyc.demo.util.FileUtil;
import com.hanyc.demo.util.ZipUtils;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.*;

/**
 * @author ：hanyc
 * @date ：2022/10/10 17:57
 * @description： 验证码
 */
@RestController
@Slf4j
public class DemoController {

    /**
     * 获取验证码  访问这个 controller
     *
     * @return
     */
    @PostMapping("/getCaptcha")
    public Object getCaptcha() {
//        算数类型
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
//        几位数运算,默认两位
        captcha.setLen(2);
//        获取运算的公式: 3+2 = ?
        captcha.getArithmeticString();
        //获取运算结果
        String text = captcha.text();
        String prefix = UUID.randomUUID().toString().replace("-", "");
//        redisTemplate.opsForValue().set(RedisKeyConsts.LOGIN_CAPTCHA + prefix, text, 10, TimeUnit.MINUTES);
        Map<String, Object> map = new HashMap<>(5);
        map.put("key", prefix);
        map.put("captcha", captcha.toBase64());
        log.debug("获取验证码:{}:{}", prefix, text);
        map.put("code", text);
        return map;
    }

    /**
     * 扫描所办所有文件数据
     *
     * @return
     */
    @PostMapping("/sensitiveCheckSuoBan")
    public Boolean sensitiveCheckSuoBan(String urlList, String outPath, String zipTempPath) {
        for (String url : urlList.split(",")) {
            checkSuoban(url, outPath, zipTempPath);
        }
        return true;
    }

    public void checkSuoban(String url, String outPathExcel, String zipTempPathO) {
        TimeInterval timer = DateUtil.timer();
        String path = url;
        String outPath = "D:\\del\\error.xlsx";
        if (StringUtils.isNotEmpty(outPathExcel)) {
            outPath = outPathExcel;
        }
        String zipTempPath = "D:\\del\\checkSuoban\\";
        if (StringUtils.isNotEmpty(zipTempPathO)) {
            zipTempPath = zipTempPathO;
        }
        List<ExcelExportEntity> list = new ArrayList<>();
        List<String> zipList = new ArrayList<>();
        List<String> rarList = new ArrayList<>();
        FileUtil.createFile(new File(zipTempPath));
        FileUtil.createFile(new File(outPath));
        SensitiveCheckSuoBan sensitiveCheckSuoBan = new SensitiveCheckSuoBan();
        sensitiveCheckSuoBan.readDirectory(path, list, rarList, zipList,"非压缩文件");
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
        sensitiveCheckSuoBan.readDirectory(zipTempPath, list, rarList, zipList,"压缩文件");
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
            workbook.write(os);
            os.flush();
        } catch (Exception e) {
            log.error("导出excel失败，msg: {}", e.getMessage(), e);
        } finally {
            IoUtil.close(workbook);
            IoUtil.close(os);
        }
    }
}
