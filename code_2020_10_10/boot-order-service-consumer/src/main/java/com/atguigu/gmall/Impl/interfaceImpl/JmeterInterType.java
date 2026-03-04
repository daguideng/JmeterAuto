package com.atguigu.gmall.Impl.interfaceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-7-3
 */

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-7-3
 */
public class JmeterInterType {

    public final static String Inter_Threads = "并发用户数";
    public final static String Inter_RunTime = "场景运行时间(分钟)";
    public final static String Inter_DelayTime = "延迟场景运行(秒)";
    public final static String Inter_Retry = "是否设置重试次数(Yes/No)";
    public final static String Inter_Modiy_Interval_Time = "是否修改控制台输出时间间隔(Yes/No)";
    public final static String Inter_Add_Custom_Listener = "是否追加自定义监听器(Yes/No)";
    public final static String Inter_Add_Log_Jtil = "是否记录日志log.jtl(Yes/No)";
    public final static String Inter_Add_Jmeter_Log = "是否记录日志jmeter.log(Yes/No)";
    public final static String Inter_Set_Between_Value = "是否设置BetweenValue(Yes/No)";





    public static List<String> InterType() {

        List <String> intertypelist = new ArrayList <String>();

        intertypelist.add(Inter_Threads);
        intertypelist.add(Inter_RunTime);
        intertypelist.add(Inter_DelayTime);
        intertypelist.add(Inter_Retry);
        intertypelist.add(Inter_Modiy_Interval_Time);
        intertypelist.add(Inter_Add_Custom_Listener);
        intertypelist.add(Inter_Add_Log_Jtil);
        intertypelist.add(Inter_Add_Jmeter_Log);
        intertypelist.add(Inter_Set_Between_Value);


        return intertypelist ;

    }

    public JmeterInterType() {
    }

}