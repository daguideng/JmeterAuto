package com.atguigu.gmall.Impl.interfaceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-8-1
 */
public class JmeterLogInfo {
    //获取脚本的LogInfo：
    public static String LogInfo = "LogInfo";

    public static Map<String,String> maplog = new HashMap<>() ;
    //mapLoginfo 方便获取：
    public static Map<String,String> setMapLogInfo(String logInfoPath){


        maplog.put(LogInfo,logInfoPath);

        return maplog ;
    }

    public static String getmapLogInfoPath(){

        return maplog.get(LogInfo);

    }


}
