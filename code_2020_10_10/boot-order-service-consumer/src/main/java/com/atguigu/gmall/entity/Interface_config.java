package com.atguigu.gmall.entity;

public class Interface_config {
    private Integer id;

    private String threads;

    private String runtime;

    private String delaytime;

    private String ifretry;

    private String ifoutinterval;

    private String ifcustomlistener;

    private String ifrecordlogjtl;

    private String ifrecordlogjmeter;

    private String ifbetweenvalue;

    private String threadname;

    private String status;

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

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime == null ? null : runtime.trim();
    }

    public String getDelaytime() {
        return delaytime;
    }

    public void setDelaytime(String delaytime) {
        this.delaytime = delaytime == null ? null : delaytime.trim();
    }

    public String getIfretry() {
        return ifretry;
    }

    public void setIfretry(String ifretry) {
        this.ifretry = ifretry == null ? null : ifretry.trim();
    }

    public String getIfoutinterval() {
        return ifoutinterval;
    }

    public void setIfoutinterval(String ifoutinterval) {
        this.ifoutinterval = ifoutinterval == null ? null : ifoutinterval.trim();
    }

    public String getIfcustomlistener() {
        return ifcustomlistener;
    }

    public void setIfcustomlistener(String ifcustomlistener) {
        this.ifcustomlistener = ifcustomlistener == null ? null : ifcustomlistener.trim();
    }

    public String getIfrecordlogjtl() {
        return ifrecordlogjtl;
    }

    public void setIfrecordlogjtl(String ifrecordlogjtl) {
        this.ifrecordlogjtl = ifrecordlogjtl == null ? null : ifrecordlogjtl.trim();
    }

    public String getIfrecordlogjmeter() {
        return ifrecordlogjmeter;
    }

    public void setIfrecordlogjmeter(String ifrecordlogjmeter) {
        this.ifrecordlogjmeter = ifrecordlogjmeter == null ? null : ifrecordlogjmeter.trim();
    }

    public String getIfbetweenvalue() {
        return ifbetweenvalue;
    }

    public void setIfbetweenvalue(String ifbetweenvalue) {
        this.ifbetweenvalue = ifbetweenvalue == null ? null : ifbetweenvalue.trim();
    }

    public String getThreadname() {
        return threadname;
    }

    public void setThreadname(String threadname) {
        this.threadname = threadname == null ? null : threadname.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }


    public Interface_config(String threads, String runtime, String delaytime, String ifretry, String ifoutinterval, String ifcustomlistener, String ifrecordlogjtl, String ifrecordlogjmeter, String ifbetweenvalue, String threadname, String status) {
        this.threads = threads;
        this.runtime = runtime;
        this.delaytime = delaytime;
        this.ifretry = ifretry;
        this.ifoutinterval = ifoutinterval;
        this.ifcustomlistener = ifcustomlistener;
        this.ifrecordlogjtl = ifrecordlogjtl;
        this.ifrecordlogjmeter = ifrecordlogjmeter;
        this.ifbetweenvalue = ifbetweenvalue;
        this.threadname = threadname;
        this.status = status;
    }


    public Interface_config(){}


}