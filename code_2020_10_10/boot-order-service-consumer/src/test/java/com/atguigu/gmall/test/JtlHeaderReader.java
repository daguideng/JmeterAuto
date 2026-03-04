package com.atguigu.gmall.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JtlHeaderReader {
    public static void main(String[] args) {
        String jtlFilePath = "/home/test/source/code_2024_01_19/code_2020_10_10/boot-order-service-consumer/test_20250811_20250812153958_new_20250812173344.jtl";
        try (BufferedReader br = new BufferedReader(new FileReader(jtlFilePath))) {
            // 读取标题行
            String headerLine = br.readLine();
            if (headerLine != null) {
                System.out.println("JTL文件标题行：");
                System.out.println(headerLine);
                String[] headers = headerLine.split(",");
                for (int i = 0; i < headers.length; i++) {
                    System.out.println("索引 " + i + ": " + headers[i]);
                }
            } else {
                System.out.println("JTL文件为空或无法读取第一行");
            }
        } catch (IOException e) {
            System.err.println("读取JTL文件失败：" + e.getMessage());
        }
    }
}