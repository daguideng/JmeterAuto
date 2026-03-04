package com.atguigu.gmall.Impl.perfImpl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @Author: dengdagui
 * @Description:自定义生成jmeter性能测试的结果名：
 * @Date: Created in 2018-7-25
 */
@Scope("prototype")
@Component
public class JmeterResultLogName implements Serializable{




    private  String jmeterjmx = ".jmx" ;
    private  String jmeterlog = ".log" ;
    private  String jmeterjtl = ".jtl";
    private  String report = "report";


    public  String getJmeterjmx() {

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = "_"+df.format(new Date());

        return time+jmeterjmx;
    }

    public  void setJmeterJmx(String jmeterjmx) {

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = "_"+df.format(new Date());

        this.jmeterjmx = time+jmeterjmx;

    }

    public  String getJmeterLog() {

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = "_"+df.format(new Date());

        return time+jmeterlog;
    }

    public  void setJmeterLog(String jmeterlog) {

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = "_"+df.format(new Date());

        this.jmeterlog = time+jmeterlog;
    }

    public  String getJmeterJtl() {

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = "_"+df.format(new Date());
        return time+jmeterjtl;
    }

    public  void setJmeterJtl(String jmeterjtl) {

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = "_"+df.format(new Date());
        this.jmeterjtl = time+jmeterjtl;
    }

    public  String getReport() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = "_"+df.format(new Date());
        return time+"_"+report;
    }

    public  void setReport(String report) {

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = "_"+df.format(new Date());
        this.report = time+"_"+report;
    }

    public JmeterResultLogName(String jmeterJmx, String jmeterLog, String jmeterJtl, String report) {
        this.jmeterjmx = jmeterJmx;
        this.jmeterlog = jmeterLog;
        this.jmeterjtl = jmeterJtl;
        this.report = report;
    }


    public JmeterResultLogName(String jmeterLog, String jmeterJtl, String report) {
        this.jmeterlog = jmeterLog;
        this.jmeterjtl = jmeterJtl;
        this.report = report;
    }

    public JmeterResultLogName(){}


    @Override
    public String toString() {
        return "JmeterPerfResultLogName{" +
                "jmeterJmx='" + jmeterjmx + '\'' +
                ", jmeterLog='" + jmeterlog + '\'' +
                ", jmeterJtl='" + jmeterjtl + '\'' +
                ", report='" + report + '\'' +
                '}';
    }


}
