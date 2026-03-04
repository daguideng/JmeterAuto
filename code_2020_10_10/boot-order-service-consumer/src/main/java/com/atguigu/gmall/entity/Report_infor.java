package com.atguigu.gmall.entity;

public class Report_infor {
    private Integer id;

    private String threads;

    private String sourcefile;

    private String starttime;

    private String endtime;

    private String filterdisplay;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getThreads() {
        return threads;
    }

    public void setThreads(String threads) {
        this.threads = threads == null ? null : threads.trim();
    }

    public String getSourcefile() {
        return sourcefile;
    }

    public void setSourcefile(String sourcefile) {
        this.sourcefile = sourcefile == null ? null : sourcefile.trim();
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

    public String getFilterdisplay() {
        return filterdisplay;
    }

    public void setFilterdisplay(String filterdisplay) {
        this.filterdisplay = filterdisplay == null ? null : filterdisplay.trim();
    }
}