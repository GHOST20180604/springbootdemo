package com.hanyc.demo.util;

import com.itextpdf.kernel.geom.Rectangle;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 矩形工具类
 */
public class RectangleUtils {

    /**
     * 合并多个矩形为一个更大的矩形
     *
     * @param rectangles 需要合并的矩形数组
     * @return 合并后的矩形
     */
    public static Rectangle mergeRectangles(List<Rectangle> rectangles) {
        if (rectangles == null || rectangles.isEmpty()) {
            throw new IllegalArgumentException("矩形数组不能为空");
        }
        // 初始化最小和最大值
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;

        // 遍历所有矩形，找到最小和最大值
        for (Rectangle rect : rectangles) {
            if (rect == null) {
                continue;
            }
            minX = Math.min(minX, rect.getX());
            minY = Math.min(minY, rect.getY());
            maxX = Math.max(maxX, rect.getX() + rect.getWidth());
            maxY = Math.max(maxY, rect.getY() + rect.getHeight());
        }

        // 计算新矩形的宽度和高度
        float width = maxX - minX;
        float height = maxY - minY;

        // 返回合并后的矩形
        return new Rectangle(minX, minY, width, height);
    }

    /**
     * 合并多个矩形为一个更大的矩形
     *
     * @param rectangles 需要合并的矩形数组
     * @return 合并后的矩形
     */
    public static Rectangle mergeRectangles(Rectangle... rectangles) {
        return mergeRectangles(Arrays.asList(rectangles));
    }


    /**
     * 将多个矩形的顶点坐标合并为一个数组
     *
     * @param rectangles 需要合并的矩形数组
     * @return 顶点坐标数组
     */
    public static float[] mergeRectanglePoints(List<Rectangle> rectangles) {
        if (rectangles == null || rectangles.isEmpty()) {
            throw new IllegalArgumentException("矩形数组不能为空");
        }

        // 用于存储所有顶点的列表
        List<Float> pointsList = new LinkedList<>();
        // 遍历所有矩形，获取每个矩形的顶点坐标
        for (Rectangle rect : rectangles) {
            if (rect == null) {
                continue;
            }

            // 获取矩形的坐标和尺寸
            float x = rect.getX();
            float y = rect.getY();
            float width = rect.getWidth();
            float height = rect.getHeight();

            // 添加矩形的 4 个顶点坐标
            pointsList.add(x);          // 左上角 x
            pointsList.add(y + height); // 左上角 y
            pointsList.add(x + width);  // 右上角 x
            pointsList.add(y + height); // 右上角 y
            pointsList.add(x);          // 左下角 x
            pointsList.add(y);          // 左下角 y
            pointsList.add(x + width);  // 右下角 x
            pointsList.add(y);          // 右下角 y
        }

        // 将列表转换为数组
        float[] points = new float[pointsList.size()];
        for (int i = 0; i < pointsList.size(); i++) {
            points[i] = pointsList.get(i);
        }

        return points;
    }

    /**
     * 将多个矩形的顶点坐标合并为一个数组
     *
     * @param rectangles 需要合并的矩形数组
     * @return 顶点坐标数组
     */
    public static float[] mergeRectanglePoints(Rectangle... rectangles) {
        return mergeRectanglePoints(Arrays.asList(rectangles));
    }


    public static void main(String[] args) {
        // 示例矩形
        Rectangle rect1 = new Rectangle(50, 500, 200, 20);// 替换为你的实际坐标
        Rectangle rect2 = new Rectangle(50, 450, 200, 20);// 替换为你的实际坐标

        // 合并矩形
        Rectangle mergedRect = mergeRectangles(rect1, rect2);

        // 输出结果
        System.out.println("合并后的矩形: x=" + mergedRect.getX() + ", y=" + mergedRect.getY() +
                ", width=" + mergedRect.getWidth() + ", height=" + mergedRect.getHeight());

        System.out.println("---------------------合并矩形的顶端坐标为一个数组-----------------");
        // 合并矩形的顶点坐标
        float[] points = mergeRectanglePoints(rect1, rect2);

        // 输出顶点坐标
        System.out.println("顶点坐标数组:");
        for (float point : points) {
            System.out.print(point + " ");
        }

    }
}