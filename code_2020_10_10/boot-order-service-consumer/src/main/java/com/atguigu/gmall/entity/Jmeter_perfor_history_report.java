package com.atguigu.gmall.entity;

import java.util.List;
import java.util.Map;
import java.util.Date;

public class Jmeter_perfor_history_report {
    private Integer id;

    private Integer uploadid ;

    private String perfstarttime ;

    private String lastruntime ;

    private String scriptname;

    private String threads;

    private String label;

    private String samples;

    private String ko;

    private String error;

    private String average;

    private String min;

    private String max;

    private String median;

    private String thpct90;

    private String thpct95;

    private String thpct99;

    private String throughput;

    private String received;

    private String sent;

    private String ip;

    private String starttime;

    private String endtime;

    private String jtlpath;

    private String indexpath ;

    private Integer state ;


    public String getPerfstarttime() {
        return perfstarttime;
    }

    public void setPerfstarttime(String perfstarttime) {
        this.perfstarttime = perfstarttime;
    }

    public Integer getUploadid() {
        return uploadid;
    }

    public void setUploadid(Integer uploadid) {
        this.uploadid = uploadid;
    }

    public String getLastruntime() {
        return lastruntime;
    }

    public void setLastruntime(String lastruntime) {
        this.lastruntime = lastruntime;
    }


    public String getJtlpath() {
        return jtlpath;
    }

    public void setJtlpath(String jtlpath) {
        this.jtlpath = jtlpath;
    }

    public String getIndexpath() {
        return indexpath;
    }

    public void setIndexpath(String indexpath) {
        this.indexpath = indexpath;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScriptname() {
        return scriptname;
    }

    public void setScriptname(String scriptname) {
        this.scriptname = scriptname == null ? null : scriptname.trim();
    }

    public String getThreads() {
        return threads;
    }

    public void setThreads(String threads) {
        this.threads = threads == null ? null : threads.trim();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

    public String getSamples() {
        return samples;
    }

    public void setSamples(String samples) {
        this.samples = samples == null ? null : samples.trim();
    }

    public String getKo() {
        return ko;
    }

    public void setKo(String ko) {
        this.ko = ko == null ? null : ko.trim();
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error == null ? null : error.trim();
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average == null ? null : average.trim();
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min == null ? null : min.trim();
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max == null ? null : max.trim();
    }

    public String getMedian() {
        return median;
    }

    public void setMedian(String median) {
        this.median = median == null ? null : median.trim();
    }

    public String getThpct90() {
        return thpct90;
    }

    public void setThpct90(String thpct90) {
        this.thpct90 = thpct90 == null ? null : thpct90.trim();
    }

    public String getThpct95() {
        return thpct95;
    }

    public void setThpct95(String thpct95) {
        this.thpct95 = thpct95 == null ? null : thpct95.trim();
    }

    public String getThpct99() {
        return thpct99;
    }

    public void setThpct99(String thpct99) {
        this.thpct99 = thpct99 == null ? null : thpct99.trim();
    }

    public String getThroughput() {
        return throughput;
    }

    public void setThroughput(String throughput) {
        this.throughput = throughput == null ? null : throughput.trim();
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received == null ? null : received.trim();
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent == null ? null : sent.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime == null ? null : starttime.trim();
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime == null ? null : endtime.trim();
    }



    public Jmeter_perfor_history_report(){}

    public Jmeter_perfor_history_report(Integer uploadid,String perfstarttime,String lastruntime,String scriptname, String threads, String label, String samples, String ko, String error, String average, String min, String max, String thpct90, String thpct95, String thpct99, String throughput, String received, String sent, String ip, String starttime, String endtime) {
        this.uploadid = uploadid ;
        this.perfstarttime = perfstarttime ;
        this.lastruntime = lastruntime ;
        this.scriptname = scriptname;
        this.threads = threads;
        this.label = label;
        this.samples = samples;
        this.ko = ko;
        this.error = error;
        this.average = average;
        this.min = min;
        this.max = max;
        this.thpct90 = thpct90;
        this.thpct95 = thpct95;
        this.thpct99 = thpct99;
        this.throughput = throughput;
        this.received = received;
        this.sent = sent;
        this.ip = ip;
        this.starttime = starttime;
        this.endtime = endtime;
    }



    public Jmeter_perfor_history_report(List historyList) {
        this.uploadid = (Integer) historyList.get(0);
        this.perfstarttime = (String) historyList.get(1);
        this.lastruntime = (String) historyList.get(2);
        this.scriptname = (String) historyList.get(3);
        this.threads = (String) historyList.get(4);
        this.label = (String) historyList.get(5);
        this.samples = (String) historyList.get(6);
        this.ko = (String) historyList.get(7);
        this.error = (String) historyList.get(8);
        this.average = (String) historyList.get(9);
        this.min = (String) historyList.get(10);
        this.max = (String) historyList.get(11);
        this.thpct90 = (String) historyList.get(12);
        this.thpct95 = (String) historyList.get(13);
        this.thpct99 = (String) historyList.get(14);
        this.throughput = (String) historyList.get(15);
        this.received = (String) historyList.get(16);
        this.sent = (String) historyList.get(17);
        this.ip = (String) historyList.get(18);
        this.starttime = (String) historyList.get(19);
        this.endtime = (String) historyList.get(20);
        this.jtlpath = (String) historyList.get(21);
        this.indexpath = (String) historyList.get(22);
        this.state = (Integer) historyList.get(23);
    }

    public Jmeter_perfor_history_report(Map<String, Object> dataMap) {
        this.uploadid = (Integer) dataMap.get("uploadid");
        // 处理perfstarttime字段，如果dataMap中不存在则设置为当前时间
        this.perfstarttime = dataMap.get("perfstarttime") != null ? (String) dataMap.get("perfstarttime") : new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.lastruntime = (String) dataMap.get("lastruntime");
        this.scriptname = (String) dataMap.get("scriptNameLink");
        this.threads = (String) dataMap.get("threadCount");
        this.label = (String) dataMap.get("transactionName");
        this.samples = (String) dataMap.get("sampleCount");
        this.ko = dataMap.get("ko") != null ? (String) dataMap.get("ko") : "0";
        this.error = (String) dataMap.get("errorPercent");
        this.average = (String) dataMap.get("averageResponseTime");
        this.min = dataMap.get("min") != null ? (String) dataMap.get("min") : "0";
        this.max = dataMap.get("max") != null ? (String) dataMap.get("max") : "0";
        this.thpct90 = dataMap.get("thpct90") != null ? (String) dataMap.get("thpct90") : "0";
        this.thpct95 = dataMap.get("thpct95") != null ? (String) dataMap.get("thpct95") : "0";
        this.thpct99 = dataMap.get("thpct99") != null ? (String) dataMap.get("thpct99") : "0";
        this.throughput = (String) dataMap.get("throughput");
        this.received = dataMap.get("received") != null ? (String) dataMap.get("received") : "0";
        this.sent = dataMap.get("sent") != null ? (String) dataMap.get("sent") : "0";
        this.ip = (String) dataMap.get("nodeIp");
        this.starttime = (String) dataMap.get("startRunTime");
        this.endtime = (String) dataMap.get("endRunTime");
        this.jtlpath = (String) dataMap.get("jtlPath");
        this.indexpath = (String) dataMap.get("indexPath");
        this.state = (Integer) dataMap.get("status");
    }

}