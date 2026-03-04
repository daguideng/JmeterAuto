package com.atguigu.gmall.entity;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Statistics {
    private Integer id;

    private Integer uploadid;

    private String perfstarttime ;

    private String lastruntime;

    private String scriptname;

    private String threads;

    private String label;

    private String samples;

    private String ko;

    private String error;

    private String average;

    private String min;

    private String max;

    private String thpct90;

    private String thpct95;

    private String thpct99;

    private String median;

    private String throughput;

    private String received;

    private String sent;

    private String ip;

    private String starttime;

    private String endtime;

    private String jtlpath;

    private String indexpath;

    private Integer state;

    public String getPerfstarttime() {
        return perfstarttime;
    }

    public void setPerfstarttime(String perfstarttime) {
        this.perfstarttime = perfstarttime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        this.lastruntime = lastruntime == null ? null : lastruntime.trim();
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

    public String getMedian() {
        return median;
    }

    public void setMedian(String median) {
        this.median = median == null ? null : median.trim();
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

    public String getJtlpath() {
        return jtlpath;
    }

    public void setJtlpath(String jtlpath) {
        this.jtlpath = jtlpath == null ? null : jtlpath.trim();
    }

    public String getIndexpath() {
        return indexpath;
    }

    public void setIndexpath(String indexpath) {
        this.indexpath = indexpath == null ? null : indexpath.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }




    public Statistics(){
    }



    public Statistics(Integer uploadid,String lastruntime,String scriptname,String threads, String label, String samples, String ko, String error, String average, String min, String max, String thpct90, String thpct95, String thpct99, String median, String throughput, String received, String sent,String ip,String starttime,String endtime  ) {

        this.uploadid = uploadid ;
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
        this.median = median;
        this.throughput = throughput;
        this.received = received;
        this.sent = sent;
        this.ip = ip;
        this.starttime = starttime;
        this.endtime = endtime;
    }


    /***s
     * 为生成简洁报告而生成的而写的：
     */
    public Statistics(Integer id,Integer uploadid,String lastruntime,String scriptname, String threads, String label, String samples, String error, String average, String min, String max, String thpct90, String thpct95, String thpct99, String median, String throughput, String ip, String starttime, String endtime) {
        this.id = id ;
        this.uploadid = uploadid ;
        this.lastruntime = lastruntime ;
        this.scriptname = scriptname;
        this.threads = threads;
        this.label = label;
        this.samples = samples;
        this.error = error;
        this.average = average;
        this.min = min;
        this.max = max;
        this.thpct90 = thpct90;
        this.thpct95 = thpct95;
        this.thpct99 = thpct99;
        this.median = median;
        this.throughput = throughput;
        this.ip = ip;
        this.starttime = starttime;
        this.endtime = endtime;
    }



    public Statistics(Integer id,Integer uploadid,String perfstarttime,String lastruntime,String scriptname, String threads, String label, String samples, String error, String average, String min, String max, String thpct90, String thpct95, String thpct99, String median, String throughput, String ip, String starttime, String endtime) {
        this.id = id ;
        this.uploadid = uploadid ;
        this.perfstarttime = perfstarttime ;
        this.lastruntime = lastruntime ;
        this.scriptname = scriptname;
        this.threads = threads;
        this.label = label;
        this.samples = samples;
        this.error = error;
        this.average = average;
        this.min = min;
        this.max = max;
        this.thpct90 = thpct90;
        this.thpct95 = thpct95;
        this.thpct99 = thpct99;
        this.throughput = throughput;
        this.ip = ip;
        this.starttime = starttime;
        this.endtime = endtime;
    }









    public Statistics(List statisticsList) {
        this.uploadid = (Integer) statisticsList.get(0);
        this.perfstarttime = (String) statisticsList.get(1);
        this.lastruntime = (String) statisticsList.get(2);
        this.scriptname = (String) statisticsList.get(3);
        this.threads = (String) statisticsList.get(4);
        this.label = (String) statisticsList.get(5);
        this.samples = (String) statisticsList.get(6);
        this.ko = (String) statisticsList.get(7);
        this.error = (String) statisticsList.get(8);
        this.average = (String) statisticsList.get(9);
        this.min = (String) statisticsList.get(10);
        this.max = (String) statisticsList.get(11);
        this.thpct90 = (String) statisticsList.get(12);
        this.thpct95 = (String) statisticsList.get(13);
        this.thpct99 = (String) statisticsList.get(14);
        this.median = (String) statisticsList.get(15);
        this.throughput = (String) statisticsList.get(16);
        this.received = (String) statisticsList.get(17);
        this.sent = (String) statisticsList.get(18);
        this.ip = (String) statisticsList.get(19);
        this.starttime = (String) statisticsList.get(20);
        this.endtime = (String) statisticsList.get(21);
        this.jtlpath = (String) statisticsList.get(22);
        this.indexpath = (String) statisticsList.get(23);
        this.state = (Integer) statisticsList.get(24);
    }

    public Statistics(Map<String, Object> dataMap) {
        this.uploadid = dataMap.containsKey("uploadid") ? (Integer) dataMap.get("uploadid") : null;
        this.perfstarttime = Objects.toString(dataMap.get("perfstarttime"), null);
        this.lastruntime = Objects.toString(dataMap.get("lastruntime"), null);
        this.scriptname = Objects.toString(dataMap.get("scriptNameLink"), null);
        this.threads = Objects.toString(dataMap.get("threadCount"), null);
        this.label = Objects.toString(dataMap.get("transactionName"), null);
        this.samples = Objects.toString(dataMap.get("sampleCount"), null);
        this.ko = Objects.toString(dataMap.get("errorCount"), null);
        this.error = Objects.toString(dataMap.get("errorPercent"), null);
        this.average = Objects.toString(dataMap.get("averageResponseTime"), null);
        this.min = Objects.toString(dataMap.get("min"), null);
        this.max = Objects.toString(dataMap.get("max"), null);
        this.thpct90 = Objects.toString(dataMap.get("thpct90"), null);
        this.thpct95 = Objects.toString(dataMap.get("thpct95"), null);
        this.thpct99 = Objects.toString(dataMap.get("thpct99"), null);
        this.median = Objects.toString(dataMap.get("median"), null);
        this.throughput = Objects.toString(dataMap.get("throughput"), null);
        this.received = Objects.toString(dataMap.get("received"), null);
        this.sent = Objects.toString(dataMap.get("sent"), null);
        this.ip = Objects.toString(dataMap.get("nodeIp"), null);
        this.starttime = Objects.toString(dataMap.get("startRunTime"), null);
        this.endtime = Objects.toString(dataMap.get("endRunTime"), null);
        this.jtlpath = Objects.toString(dataMap.get("jtlPath"), null);
        this.indexpath = Objects.toString(dataMap.get("indexPath"), null);
        this.state = dataMap.containsKey("status") ? (Integer) dataMap.get("status") : null;
    }





}