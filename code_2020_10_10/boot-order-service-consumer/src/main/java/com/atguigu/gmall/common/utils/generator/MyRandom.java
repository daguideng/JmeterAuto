package com.atguigu.gmall.common.utils.generator;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * @author ellen 随机数据生成器
 */
public class MyRandom {


    /**
     * 随机整数生成器，用于生成min -> max 之间的int类型数据，包含min，不包含max
     *
     * @param min
     * @param max
     * @return
     */
    public static int randomInteger(int min, int max) {
        Random random = new Random();
        int var = random.nextInt(max) % (max - min + 1) + min;
        return var;
    }

    /**
     * 随机生成一个汉字
     *
     * @return
     */
    public static char getRandomChar() {
        return (char) (0x4e00 + (int) (Math.random() * (0x9fa5 - 0x4e00 + 1)));
    }

    /**
     * 随机生成最少min，最大max个汉字
     *
     * @param min
     * @param max
     * @return
     */
    public static String getRandomStr(int min, int max) {
        int num = randomInteger(min, max);
        return getRandomJianHan(num);
    }

    private static String getRandomJianHan(int len) {
        String ret = "";
        for (int i = 0; i < len; i++) {
            String str = null;
            int hightPos, lowPos; // 定义高低位
            Random random = new Random();
            hightPos = (176 + Math.abs(random.nextInt(39))); //获取高位值
            lowPos = (161 + Math.abs(random.nextInt(93))); //获取低位值
            byte[] b = new byte[2];
            b[0] = (new Integer(hightPos).byteValue());
            b[1] = (new Integer(lowPos).byteValue());
            try {
                str = new String(b, "gbk"); //转成中文
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            ret += str;
        }
        return ret;
    }


    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            int var = randomInteger(0, 10);
            System.out.println("\t" + var + "\t" + getRandomChar() + "\t" + getRandomStr(10, 10));
        }
    }
}
