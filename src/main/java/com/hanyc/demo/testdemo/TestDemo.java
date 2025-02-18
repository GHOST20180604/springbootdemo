package com.hanyc.demo.testdemo;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：hanyc
 * @date ：2023/7/19 13:49
 * @description：
 */
@Slf4j
public class TestDemo {
    public static void main(String[] args) {
        // 给定的长句子
        String text = "如果定位是在空字符串,需要添加新增内容,添加批注的时候,向空字符串内容中加一个空格. 符串,需要添加新增内容,添加批注的时候,向空字符串内容中加一个空个别文档解析也可能会有其他符号";

        // 要查找的目标句子（非连续）
        String targetSentence = "需要添加新增内容时候,向空字符串内容中加一个空格";

        // 目标句子中需要定位的字符范围（第2到第16个字符）
        int startCharIndex = 1; // 第2个字符的起始位置（索引从0开始）
        int endCharIndex = 16;  // 第16个字符的结束位置（不包括）

        // 检查目标句子的长度是否足够
        if (targetSentence.length() < endCharIndex) {
            System.out.println("目标句子长度不足，无法定位第2到第16个字符");
            return;
        }

        // 查找目标句子中每个部分在长句子中的位置
        int[] positions = findPositionsInText(text, targetSentence);
        if (positions == null) {
            System.out.println("未找到目标句子的所有部分");
            return;
        }

        // 计算第2到第16个字符在长句子中的位置
        int[] targetRange = getTargetRange(positions, startCharIndex, endCharIndex);
        if (targetRange == null) {
            System.out.println("无法定位第2到第16个字符在长句子中的位置");
            return;
        }

        // 获取长句子中第2到第16个字符的内容
        String targetChars = text.substring(targetRange[0], targetRange[1]);

        // 输出结果
        System.out.println("目标句子中第2到第16个字符在长句子中的位置: [" + targetRange[0] + ", " + targetRange[1] + ")");
        System.out.println("对应的字符内容: " + targetChars);
    }

    /**
     * 查找目标句子中每个部分在长句子中的位置
     *
     * @param text           长句子
     * @param targetSentence 目标句子
     * @return 目标句子中每个字符在长句子中的位置数组，如果未找到返回null
     */
    private static int[] findPositionsInText(String text, String targetSentence) {
        int[] positions = new int[targetSentence.length()];
        int textIndex = 0;

        for (int i = 0; i < targetSentence.length(); i++) {
            char targetChar = targetSentence.charAt(i);

            // 在长句子中查找当前字符
            int charIndex = text.indexOf(targetChar, textIndex);
            if (charIndex == -1) {
                return null; // 未找到字符
            }

            positions[i] = charIndex;
            textIndex = charIndex + 1; // 更新查找起始位置
        }

        return positions;
    }

    /**
     * 计算目标句子中第2到第16个字符在长句子中的位置范围
     *
     * @param positions      目标句子中每个字符在长句子中的位置数组
     * @param startCharIndex 目标句子中起始字符索引
     * @param endCharIndex   目标句子中结束字符索引
     * @return 长句子中的位置范围 [start, end)，如果无法定位返回null
     */
    private static int[] getTargetRange(int[] positions, int startCharIndex, int endCharIndex) {
        if (startCharIndex < 0 || endCharIndex > positions.length || startCharIndex >= endCharIndex) {
            return null; // 无效范围
        }

        int start = positions[startCharIndex];
        int end = positions[endCharIndex - 1] + 1; // 结束位置不包括

        return new int[]{start, end};
    }
}
