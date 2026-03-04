package com.atguigu.gmall.common.utils.generator;

public class refIDGenerator {


    /**
     * 随机生成一个MIQueue使用的ID
     *
     * @return
     */
    public static String randomMiQueID() {
        int code = (int) (Math.random() * 10000.0D);
        return code < 10 ? "000" + code : (code < 100 ? "00" + code : (code < 1000 ? "0" + code : "" + code));
    }

    /**
     * 随机生成一个app使用的ID
     *
     * @return
     */
    public static String randomAppID() {
        int code = (int) (Math.random() * 10000.0D);
        return code < 10 ? "A000" + code : (code < 100 ? "A00" + code : (code < 1000 ? "A0" + code : "A" + code));
    }

    public static void main(String[] args) {
     //   logger.info.print(randomMiQueID());
    }
}