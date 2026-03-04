package com.atguigu.gmall.entity;

public class Interface_run_ip {
    private Integer id;

    private String jmeterLoadRun;

    private String toEmail;

    private String ccEmail;

    private String bccEmail;

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
}