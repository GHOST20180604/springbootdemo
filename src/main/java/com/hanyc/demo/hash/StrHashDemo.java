package com.hanyc.demo.hash;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StrHashDemo {

    public static void main(String[] args) {
        // 生成1000个随机字符串
        List<String> randomStrings = new ArrayList<>();
        String filePath = "C:\\Users\\hanyu\\Desktop\\ids.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                randomStrings.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 将这些字符串平均分配到16个数组中
        List<String>[] result = distributeStrings(randomStrings, 18);

        // 打印每个数组的字符串数量
        for (int i = 0; i < 16; i++) {
            System.out.println("Array " + (i + 1) + " has " + result[i].size() + " strings.");
        }
    }
//
//    // 生成指定数量的随机字符串
//    public static List<String> generateRandomStrings(int count) {
//        List<String> strings = new ArrayList<>();
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        Random random = new Random();
//
//        for (int i = 0; i < count; i++) {
//            StringBuilder sb = new StringBuilder(10); // 每个字符串长度为10
//            for (int j = 0; j < 10; j++) {
//                sb.append(characters.charAt(random.nextInt(characters.length())));
//            }
//            strings.add(sb.toString());
//        }
//        return strings;
//    }

    /**
     * 将字符串列表分配到指定数量的数组中，使用哈希计算
     */

    public static List<String>[] distributeStrings(List<String> strings, int numArrays) {
        // 创建16个空的数组
        List<String>[] result = new List[numArrays];
        for (int i = 0; i < numArrays; i++) {
            result[i] = new ArrayList<>();
        }

        // 使用哈希值来确定字符串应该放入哪个数组
        for (String str : strings) {
            // 获取字符串的哈希值
            int hashCode = str.hashCode();
            // 通过哈希值计算数组索引
            int arrayIndex = Math.abs(hashCode % numArrays);
            // 将字符串放入对应的数组
            result[arrayIndex].add(str);
        }

        return result;
    }
}
