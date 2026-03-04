package com.atguigu.gmall.entity;

public class Jmeter_perfor_current_report {
    private Integer id;

    private Integer uploadid;

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

    private String indexpath;

    private Integer state;

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
}