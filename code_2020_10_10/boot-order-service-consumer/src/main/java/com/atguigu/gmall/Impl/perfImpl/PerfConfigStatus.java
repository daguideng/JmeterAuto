package com.atguigu.gmall.Impl.perfImpl;

/**
 * @Author: dengdagui
 * @Description:  表示性能测试运行状态
 * @Date: Created in 2018-7-23
 */
public class PerfConfigStatus
{
    //性能测试开始状态：提交性能测试参数时的状态：
    public static final String start = "start";
    //性能测试运行状态：从性能测试开始时运行的状态：
    public static final String run = "run";
    //此次性能测试结束时：结束时运行的状态
    public static final String end = "end";

    public static String getStart() {
        return start;
    }



    public static String getRun() {
        return run;
    }



    public static String getEnd() {
        return end;
    }



    @Override
    public String toString() {
        return "PerfConfigStatus{}";
    }

    public PerfConfigStatus() {
    }



}
