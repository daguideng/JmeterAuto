package com.atguigu.gmall.common.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: dengdagui
 * @Description:为方便取数据库的配置文件而写的方法
 * @Date: Created in 2018-7-24
 */
public class GetJmeterDataUtil {

    public static List<String> getJmeterPerfListData(String data){

        List<String> listdata = Arrays.asList(data.toString().replace("[","").replace("]","").replace(" ","").split(","));


        return listdata ;

    }


    public static List<String> getJmeterInterListData(String data){

        List<String> listdata = Arrays.asList(data.toString().replace("[","").replace("]","").replace(" ","").split(","));


        return listdata ;

    }


    public static String getJmeterPerfStringData(String data){

        String datastr = data.toString().replace("[","").replace("]","").replace(" ","");


        return datastr ;

    }
}
