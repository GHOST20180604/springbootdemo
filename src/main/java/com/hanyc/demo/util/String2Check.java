package com.hanyc.demo.util;

import com.alibaba.fastjson.JSON;

/**
 * @author ：hanyc
 * @date ：2024/7/8 11:01
 * @description： 比较两个字符串的不同. 获取开始+结束位置.
 */
public class String2Check {

    public static int[] findDifference(String A, String B) {
        int lengthA = A.length();
        int lengthB = B.length();
        int minLength = Math.min(lengthA, lengthB);

        int startIndex = -1;
        int endIndex = -1;

        // Find the start index of the first difference
        for (int i = 0; i < minLength; i++) {
            if (A.charAt(i) != B.charAt(i)) {
                startIndex = i;
                break;
            }
        }

        // Find the end index of the last difference
        for (int i = 0; i < minLength; i++) {
            if (A.charAt(lengthA - 1 - i) != B.charAt(lengthB - 1 - i)) {
                endIndex = lengthA - 1 - i;
                break;
            }
        }
        // 如果没有发现不同,则返回空
        if (endIndex == -1 || startIndex == -1) {
            return null;
        }
        // Return the start and end indices
        return new int[]{startIndex, endIndex};
    }


    public static void main(String[] args) {

        // 1. 错误表述,正确表述  有一个为空,  则不提供起始标识
        // 2. 错误表述和正确表述都不为空, 并且


        String A = "为深入学习贯彻习近平文化思想，贯彻落实党中央关于全民阅读的重要部署，按照中国科学技术信息研究所（以下简称“中信所”）万方公司党总支办好青年读书实践活动要求，引导公司青年爱读书、读好书、善读书，用文化力量浸润人心、启迪智慧、陶冶情操，营造书香氛围，2024年4月24日，中信所万方青年理论学习小组举办“‘典’亮星火——奔跑的‘后浪’向上成长”读书分享会。党总支青年委员张秀梅出席，青年书友10余人参加活动。";
        String B = "为深入学习贯彻习近平文化思想，贯彻落实党中央关于全民阅读的重要部署，引导公司青年爱读书、读好书、善读书，用文化力量浸润人心、启迪智慧、陶冶情操，营造书香氛围，按照中国科学技术信息研究所（以下简称“中信所”）万方公司党总支关于办好青年读书实践活动的要求，2024年4月24日，中信所万方青年理论学习小组举办“‘典’亮星火——奔跑的‘后浪’向上成长”读书分享会。党总支青年委员张秀梅出席活动，青年书友10余人参加活动。";

        int[] result = findDifference(A, B);
        System.out.println(JSON.toJSONString(result));
    }

}
