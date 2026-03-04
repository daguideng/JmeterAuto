package com.atguigu.gmall.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JtlFormatTest {
    public static void main(String[] args) {
        // 注意：实际运行时需要修改为正确的JTL文件路径
        String jtlFilePath = "/usr/local/jmeter/mars/ReportResultDir/20250812221102/test_20250811_20250812153958_new_20250812221102_1_1/test_20250811_20250812153958_new_20250812221102_report/test_20250811_20250812153958_new_20250812173344.jtl";
        try (BufferedReader br = new BufferedReader(new FileReader(jtlFilePath))) {
            // 读取标题行
            String headerLine = br.readLine();
            if (headerLine != null) {
                System.out.println("JTL文件标题行：");
                String[] headers = headerLine.split(",");
                for (int i = 0; i < headers.length; i++) {
                    System.out.println("索引 " + i + ": " + headers[i]);
                }
            }

            // 读取第一行数据
            String dataLine = br.readLine();
            if (dataLine != null) {
                System.out.println("\nJTL文件第一行数据：");
                String[] data = dataLine.split(",");
                for (int i = 0; i < data.length; i++) {
                    System.out.println("索引 " + i + ": " + data[i]);
                }
            }
        } catch (IOException e) {
            System.err.println("读取JTL文件失败：" + e.getMessage());
        }
    }
}