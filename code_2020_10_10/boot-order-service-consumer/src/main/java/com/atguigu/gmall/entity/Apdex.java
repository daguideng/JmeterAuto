package com.atguigu.gmall.entity;

public class Apdex {
    private Integer id;

    private String threads;

    private String apdex;

    private String tolerationthreshold;

    private String frustrationthreshold;

    private String label;

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

    public String getApdex() {
        return apdex;
    }

    public void setApdex(String apdex) {
        this.apdex = apdex == null ? null : apdex.trim();
    }

    public String getTolerationthreshold() {
        return tolerationthreshold;
    }

    public void setTolerationthreshold(String tolerationthreshold) {
        this.tolerationthreshold = tolerationthreshold == null ? null : tolerationthreshold.trim();
    }

    public String getFrustrationthreshold() {
        return frustrationthreshold;
    }

    public void setFrustrationthreshold(String frustrationthreshold) {
        this.frustrationthreshold = frustrationthreshold == null ? null : frustrationthreshold.trim();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }
}