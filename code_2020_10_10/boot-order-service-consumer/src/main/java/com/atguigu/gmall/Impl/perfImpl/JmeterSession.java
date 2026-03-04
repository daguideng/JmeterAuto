package com.atguigu.gmall.Impl.perfImpl;

/**
 * @Author: dengdagui
 * @Description: Session 对于不同Session不同的key
 * @Date: Created in 2018-9-28
 */
public class JmeterSession {
    public final static String SESSION_PERF = "Perf_ThreadName";   //性能
    public final static String SESSION_INTER = "Inter_ThreadName";  //接口

    //为定时器而设计的变量：
    public static String SESSION_TIMER_Start  = "Perf_Inter_Timer_Start"  ; //定时器开始
    public static String SESSION_TIMER_End  = "Perf_Inter_Timer_Mid"  ; //定时器结束
    public static String SESSION_TIMER_Mid  = "Perf_Inter_Timer_Mid"  ; //定时器结束

    public JmeterSession(){}

}
