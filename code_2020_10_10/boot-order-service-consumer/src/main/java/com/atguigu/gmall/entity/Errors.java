package com.atguigu.gmall.entity;

public class Errors {
    private Integer id;

    private String threads;

    private String typeoferror;

    private String numberoferrors;

    private String inerrors;

    private String inallsamples;

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

    public String getTypeoferror() {
        return typeoferror;
    }

    public void setTypeoferror(String typeoferror) {
        this.typeoferror = typeoferror == null ? null : typeoferror.trim();
    }

    public String getNumberoferrors() {
        return numberoferrors;
    }

    public void setNumberoferrors(String numberoferrors) {
        this.numberoferrors = numberoferrors == null ? null : numberoferrors.trim();
    }

    public String getInerrors() {
        return inerrors;
    }

    public void setInerrors(String inerrors) {
        this.inerrors = inerrors == null ? null : inerrors.trim();
    }

    public String getInallsamples() {
        return inallsamples;
    }

    public void setInallsamples(String inallsamples) {
        this.inallsamples = inallsamples == null ? null : inallsamples.trim();
    }
}