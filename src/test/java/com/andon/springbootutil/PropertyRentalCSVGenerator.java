package com.andon.springbootutil;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * @author Andon
 * 2024/6/19
 */
public class PropertyRentalCSVGenerator {

    public static void main(String[] args) {
        // 设置房源数量
        int numberOfProperties = 128;
        // 设置价格区间
        int minPrice = 1000;
        int maxPrice = 12000;

        // 创建CSV文件
        String csvFileName = "property_rental_info.csv";
        try (PrintWriter out = new PrintWriter(new FileWriter(csvFileName))) {
            // 写入CSV头部
            out.println("id,price");

            // 生成房源信息并写入CSV
            Random random = new Random();
            for (int i = 1; i <= numberOfProperties; i++) {
                int propertyId = i; // 房源号，这里简单使用循环计数
                int rentPrice = minPrice + random.nextInt(maxPrice - minPrice + 1); // 随机生成租金价格

                // 写入CSV文件
                out.printf("%d,%d%n", propertyId, rentPrice);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
