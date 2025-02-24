package com.hanyc.demo.testdemo;

/**
 * 字节转化mb kb
 *
 */
public class ByteToMBorKB {
    public static void main(String[] args) {
        // 示例字节数
        long bytes = 987654; // 修改此值以测试不同情况

        // 判断是否达到1MB
        if (bytes >= 1024 * 1024) {
            // 转换为MB并保留两位小数
            double mb = bytes / (1024.0 * 1024.0);
            String formattedMB = String.format("%.2f", mb);
            System.out.println("字节大小: " + formattedMB + " MB");
        } else {
            // 转换为KB并保留两位小数
            double kb = bytes / 1024.0;
            String formattedKB = String.format("%.2f", kb);
            System.out.println("字节大小: " + formattedKB + " KB");
        }
    }
}