package com.atguigu.gmall.Impl.interfaceImpl;

/**
 * @Author: dengdagui
 * @Description:  表示性能测试运行状态
 * @Date: Created in 2018-7-23
 */
public class InterConfigStatus
{
    //性能测试开始状态：提交性能测试参数时的状态：
    public static String start = "start";
    //性能测试运行状态：从性能测试开始时运行的状态：
    public static String run = "run";
    //此次性能测试结束时：结束时运行的状态
    public static String end = "end";

    public static String getStart() {
        return start;
    }

    public static void setStart(String start) {
        InterConfigStatus.start = start;
    }

    public static String getRun() {
        return run;
    }

    public static void setRun(String run) {
        InterConfigStatus.run = run;
    }

    public static String getEnd() {
        return end;
    }

    public static void setEnd(String end) {
        InterConfigStatus.end = end;
    }

    @Override
    public String toString() {
        return "PerfConfigStatus{}";
    }

    public InterConfigStatus() {
    }



}
