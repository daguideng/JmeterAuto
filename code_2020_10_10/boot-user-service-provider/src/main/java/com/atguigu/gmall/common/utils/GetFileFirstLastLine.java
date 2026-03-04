package com.atguigu.gmall.common.utils;

import java.io.RandomAccessFile;

/**
 * @Author: dengdagui
 * @Description:读取文件的最后一行及第二行,取运行时间：
 * @Date: Created in 2018-8-8
 */
public class GetFileFirstLastLine {

    public static String getFileLastLine(String filePath) {
        RandomAccessFile raf;
        String lastLine = "";
        try {
            raf = new RandomAccessFile(filePath, "r");
            long len = raf.length();
            if (len != 0L) {
                long pos = len - 1;

                while (pos > 0) {
                    pos--;
                    raf.seek(pos);
                    if (raf.readByte() == '\n') {
                        lastLine = raf.readLine();
                        System.out.println();
                        break;

                    }
                }
            }
            System.out.println();
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastLine;
    }


    public static String getFileFirstLine(String filePath) {
        RandomAccessFile raf;
        String firstLine = "";
        try {
            raf = new RandomAccessFile(filePath, "r");
            firstLine = raf.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return firstLine;
    }

    //读取文件第二行：
    public static String getFileSecondLine(String filePath) {
        RandomAccessFile raf;
        String secondLine = "";
        try {
            raf = new RandomAccessFile(filePath, "r");
            long len = raf.length();
            if (len != 0L) {
                long pos = 1;
                while (pos > 0) {
                    pos++;
                    raf.seek(pos);
                    if (raf.readByte() == '\n') {
                        secondLine = raf.readLine();
                        break;
                    }
                }
            }
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secondLine;
    }
}
