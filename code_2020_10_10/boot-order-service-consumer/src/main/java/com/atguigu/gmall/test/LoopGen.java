package com.atguigu.gmall.test;

import com.alibaba.fastjson.JSONArray;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author admin
 * @version v1.0
 * @description
 * @date 2020/1/9
 */
public class LoopGen {

    public static void main(String[] args) {

        String regex = "\\[(\\s*\\d\\s*,\\s*)*\\s*\\d\\s*]";
        while (true) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("------------请输入字符串------- eg. [1,2]");
            //因为Scanner的next默认的匹配方式是遇到空格和换行符
            //String next = scanner.next();
            String next = scanner.nextLine();
            if (next == null || "".equals(next.trim()) || !Pattern.matches(regex, next)) {
                System.out.println("字符串输入异常，请重新输入.....");
                continue;
            }
            System.out.println("当前输入的字符串是：" + next);
            String arrayStr = next.replaceAll(" ", "");
            System.out.println("当前输入的字符串(去除空格)是：" + arrayStr);
            JSONArray array = JSONArray.parseArray(arrayStr);

            StringBuilder builder = new StringBuilder("[ ");
            println(array, builder, 0);
        }
    }

    private static void println(JSONArray array, StringBuilder builder, int index) {
        System.out.println("array--->" + array.toJSONString());
        int key = array.getInteger(index);
        System.out.println("key--->" + key);
        int indexTemp = index;
        StringBuilder builderTemp = new StringBuilder(builder.toString());
        for (int k = 0; k < key; k++) {
            builder.append(k + ", ");
            if (++index < array.size()) {
                println(array, builder, index);
            } else {
                builder = new StringBuilder(builder.substring(0, builder.toString().lastIndexOf(",")));
                builder.append(" ]");
                System.out.println(builder.toString());
            }
            builder = new StringBuilder(builderTemp.toString());
            index = indexTemp;
        }
    }
}
