package com.atguigu.gmall.entity;

public class Jmeter_perf_run_ip {
    private Integer id;

    private String jmeterLoadRun;

    private String toEmail;

    private String ccEmail;

    private String bccEmail;

    private Integer specifiedState;

    public Integer getSpecifiedState() {
        return specifiedState;
    }

    public void setSpecifiedState(Integer specifiedState) {
        this.specifiedState = specifiedState;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJmeterLoadRun() {
        return jmeterLoadRun;
    }

    public void setJmeterLoadRun(String jmeterLoadRun) {
        this.jmeterLoadRun = jmeterLoadRun == null ? null : jmeterLoadRun.trim();
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail == null ? null : toEmail.trim();
    }

    public String getCcEmail() {
        return ccEmail;
    }

    public void setCcEmail(String ccEmail) {
        this.ccEmail = ccEmail == null ? null : ccEmail.trim();
    }

    public String getBccEmail() {
        return bccEmail;
    }

    public void setBccEmail(String bccEmail) {
        this.bccEmail = bccEmail == null ? null : bccEmail.trim();
    }

    public Jmeter_perf_run_ip(){}

    public Jmeter_perf_run_ip(String jmeterLoadRun, String toEmail, String ccEmail, String bccEmail) {
        this.jmeterLoadRun = jmeterLoadRun;
        this.toEmail = toEmail;
        this.ccEmail = ccEmail;
        this.bccEmail = bccEmail;

    }


    public Jmeter_perf_run_ip(String jmeterLoadRun) {
        this.jmeterLoadRun = jmeterLoadRun;

    }

    public Jmeter_perf_run_ip(String jmeterLoadRun, String toEmail) {
        this.jmeterLoadRun = jmeterLoadRun;
        this.toEmail = toEmail;

    }

    public Jmeter_perf_run_ip(String jmeterLoadRun, String toEmail,String ccEmail) {
        this.jmeterLoadRun = jmeterLoadRun;
        this.toEmail = toEmail;
        this.ccEmail = ccEmail ;

    }
}