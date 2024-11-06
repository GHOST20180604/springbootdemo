package com.hanyc.demo.util;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @Description 解压缩文件工具类
 * @Author Mr.nobody
 * @Date 2021/3/8
 * @Version 1.0.0
 */
@Slf4j
public class ZipUtils {

    /**
     * 解压
     *
     * @param zipFilePath  带解压文件
     * @param desDirectory 解压到的目录
     * @throws Exception
     */
    public static void unzip(String zipFilePath, String desDirectory,String encoding) throws Exception {

        File desDir = new File(desDirectory);
        if (!desDir.exists()) {
            boolean mkdirSuccess = desDir.mkdir();
            if (!mkdirSuccess) {
                throw new Exception("创建解压目标文件夹失败");
            }
        }
        // 读入流
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(new FileInputStream(zipFilePath),encoding);
        try {
            // 遍历每一个文件
            ArchiveEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                if (zipEntry.isDirectory()) { // 文件夹
                    String unzipFilePath = desDirectory + File.separator + zipEntry.getName();
                    // 直接创建
                    mkdir(new File(unzipFilePath));
                } else { // 文件
                    String unzipFilePath = desDirectory + File.separator + zipEntry.getName();
                    File file = new File(unzipFilePath);
                    // 创建父目录
                    mkdir(file.getParentFile());
                    // 写出文件流
                    BufferedOutputStream bufferedOutputStream =
                            new BufferedOutputStream(new FileOutputStream(unzipFilePath));
                    byte[] bytes = new byte[1024];
                    int readLen;
                    while ((readLen = zipInputStream.read(bytes)) != -1) {
                        bufferedOutputStream.write(bytes, 0, readLen);
                    }
                    bufferedOutputStream.close();
                }
                zipEntry = zipInputStream.getNextEntry();
            }
        } catch (Exception e) {
            log.error("解压文件失败:{}", e.getMessage(), e);
            throw e;
        } finally {
            IoUtil.close(zipInputStream);
        }
    }

    // 如果父目录不存在则创建
    private static void mkdir(File file) {
        if (null == file || file.exists()) {
            return;
        }
        mkdir(file.getParentFile());
        file.mkdir();
    }

//    public static void main(String[] args) throws Exception {
//        String zipFilePath = "D:\\code\\科管二期\\舆情系统.zip";
//        String desDirectory = "D:\\code\\科管二期\\舆情系统aaaaaaaa\\";
//        unzip(zipFilePath, desDirectory);
//    }
}