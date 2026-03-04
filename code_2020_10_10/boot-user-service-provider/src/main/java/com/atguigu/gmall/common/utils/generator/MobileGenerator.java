package com.atguigu.gmall.common.utils.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class MobileGenerator {


    /**
     * 随机获取一个手机号
     *
     * @return
     */
    public static String randomMobile() {
        int i = 0;
        String sCode = "";
        do {
            int code = (int) (Math.random() * 9999.0D);
            if (code < 10) {
                sCode += "000" + code;
            } else if (code < 100) {
                sCode += "00" + code;
            } else if (code < 1000) {
                sCode += "0" + code;
            } else {
                sCode += "" + code;
            }
            i++;
        } while (i < 2);

        return "132" + sCode;
    }

    public static void main(String[] args) {
/* 输出数据 */
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("/Users/ellen/Desktop/rdc.txt")),
                    "UTF-8"));
            for (int i = 0; i < 12; ++i) {
                String IDs = randomMobile();
                System.out.print(IDs);
                System.out.print("\n");
                bw.write(IDs);
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            System.err.println("write errors :" + e);
        }

    }
}