package com.atguigu.gmall.test;

import com.alibaba.fastjson.JSONArray;
import java.util.Scanner;

public class PrintIndex {
    public static void main(String[] args) {
        System.out.println("请输入类似字符：[2,2,2]");
        System.out.println();
        Scanner input = new Scanner(System.in);
        String strArray = input.nextLine();
        JSONArray array = JSONArray.parseArray(strArray);
        StringBuilder builder = new StringBuilder("[");
        outprint(array, builder, 0);
    }

    public static void outprint(JSONArray array, StringBuilder builder, int index) {
        int key = array.getInteger(index);

        int indexTemp = index;
        StringBuilder builderTemp = new StringBuilder(builder.toString());
        for (int k = 0; k < key; k++) {
            builder.append(k + ", ");
            if (++index < array.size()) {
                outprint(array, builder, index);
            } else {
                builder = new StringBuilder(builder.substring(0, builder.toString().lastIndexOf(",")));
                builder.append("]");
                System.out.println(builder.toString());
            }
            builder = new StringBuilder(builderTemp.toString());
            index = indexTemp;

        }
    }


}
