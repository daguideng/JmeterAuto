package com.atguigu.gmall.Impl.perfImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-7-3
 */
public class JmeterPerfType {

    public final static String Perf_Threads = "并发用户数";
    public final static String Perf_RunTime = "场景运行时间(分钟)";
    public final static String Perf_DelayTime = "延迟场景运行(秒)";
    public final static String Perf_Retry = "是否设置重试次数(Yes/No)";
    public final static String Perf_Modiy_Interval_Time = "是否修改控制台输出时间间隔(Yes/No)";
    public final static String Perf_Add_Custom_Listener = "是否追加自定义监听器(Yes/No)";
    public final static String Perf_Add_Log_Jtil = "是否记录日志log.jtl(Yes/No)";
    public final static String Perf_Add_Jmeter_Log = "是否记录日志jmeter.log(Yes/No)";
    public final static String Perf_Set_Between_Value = "是否设置BetweenValue(Yes/No)";







    public static List<String> perfType() {

        List <String> pertypelist = new ArrayList <String>();

        pertypelist.add(Perf_Threads);
        pertypelist.add(Perf_RunTime);
        pertypelist.add(Perf_DelayTime);
        pertypelist.add(Perf_Retry);
        pertypelist.add(Perf_Modiy_Interval_Time);
        pertypelist.add(Perf_Add_Custom_Listener);
        pertypelist.add(Perf_Add_Log_Jtil);
        pertypelist.add(Perf_Add_Jmeter_Log);
        pertypelist.add(Perf_Set_Between_Value);


        return pertypelist ;

    }

    public JmeterPerfType() {
    }




}