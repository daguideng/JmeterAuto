package com.atguigu.gmall.entity;

public class Timer_type_config {
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

    private String type;

    private String timestatus ;

    private String ids ;

    private String timetask ;

    private String jobname;

    private String triggername;

    private String jobgroup;

    private String jobdescribe ;

    private Integer deletestate ;

    public Integer getDeletestate() {
        return deletestate;
    }

    public void setDeletestate(Integer deletestate) {
        this.deletestate = deletestate;
    }

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
        this.threads = threads;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getDelaytime() {
        return delaytime;
    }

    public void setDelaytime(String delaytime) {
        this.delaytime = delaytime;
    }

    public String getIfretry() {
        return ifretry;
    }

    public void setIfretry(String ifretry) {
        this.ifretry = ifretry;
    }

    public String getIfoutinterval() {
        return ifoutinterval;
    }

    public void setIfoutinterval(String ifoutinterval) {
        this.ifoutinterval = ifoutinterval;
    }

    public String getIfcustomlistener() {
        return ifcustomlistener;
    }

    public void setIfcustomlistener(String ifcustomlistener) {
        this.ifcustomlistener = ifcustomlistener;
    }

    public String getIfrecordlogjtl() {
        return ifrecordlogjtl;
    }

    public void setIfrecordlogjtl(String ifrecordlogjtl) {
        this.ifrecordlogjtl = ifrecordlogjtl;
    }

    public String getIfrecordlogjmeter() {
        return ifrecordlogjmeter;
    }

    public void setIfrecordlogjmeter(String ifrecordlogjmeter) {
        this.ifrecordlogjmeter = ifrecordlogjmeter;
    }

    public String getIfbetweenvalue() {
        return ifbetweenvalue;
    }

    public void setIfbetweenvalue(String ifbetweenvalue) {
        this.ifbetweenvalue = ifbetweenvalue;
    }

    public String getThreadname() {
        return threadname;
    }

    public void setThreadname(String threadname) {
        this.threadname = threadname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestatus() {
        return timestatus;
    }

    public void setTimestatus(String timestatus) {
        this.timestatus = timestatus;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getTimetask() {
        return timetask;
    }

    public void setTimetask(String timetask) {
        this.timetask = timetask;
    }

    public String getJobname() {
        return jobname;
    }

    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

    public String getTriggername() {
        return triggername;
    }

    public void setTriggername(String triggername) {
        this.triggername = triggername;
    }

    public String getJobgroup() {
        return jobgroup;
    }

    public void setJobgroup(String jobgroup) {
        this.jobgroup = jobgroup;
    }

    public String getJobdescribe() {
        return jobdescribe;
    }

    public void setJobdescribe(String jobdescribe) {
        this.jobdescribe = jobdescribe;
    }



    public Timer_type_config(){}


    public Timer_type_config(String threads, String runtime, String delaytime, String ifretry, String ifoutinterval, String ifcustomlistener, String ifrecordlogjtl, String ifrecordlogjmeter, String ifbetweenvalue, String threadname, String status, String type, String timestatus, String ids, String timetask) {
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
        this.type = type;
        this.timestatus = timestatus;
        this.ids = ids;
        this.timetask = timetask;
    }



    public Timer_type_config(String threads, String runtime, String delaytime, String ifretry, String ifoutinterval, String ifcustomlistener, String ifrecordlogjtl, String ifrecordlogjmeter, String ifbetweenvalue, String threadname, String status, String type, String timestatus, String ids, String timetask, String jobname, String triggername, String jobgroup, String jobdescribe, Integer deletestate) {
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
        this.type = type;
        this.timestatus = timestatus;
        this.ids = ids;
        this.timetask = timetask;
        this.jobname = jobname;
        this.triggername = triggername;
        this.jobgroup = jobgroup;
        this.jobdescribe = jobdescribe;
        this.deletestate = deletestate ;

    }



    //新增
    public Timer_type_config(Integer id, String threads, String runtime, String delaytime, String ifretry, String ifoutinterval, String ifcustomlistener, String ifrecordlogjtl, String ifrecordlogjmeter, String ifbetweenvalue, String threadname, String status, String type, String timestatus, String ids, String timetask, String jobname, String triggername, String jobgroup, String jobdescribe, Integer deletestate) {
        this.id = id;
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
        this.type = type;
        this.timestatus = timestatus;
        this.ids = ids;
        this.timetask = timetask;
        this.jobname = jobname;
        this.triggername = triggername;
        this.jobgroup = jobgroup;
        this.jobdescribe = jobdescribe;
        this.deletestate = deletestate ;

    }






    //展示
    public Timer_type_config(Integer id, String threads, String runtime, String delaytime, String status, String type, String timestatus, String ids, String timetask, String jobname, String jobgroup, String jobdescribe, String Integer) {
        this.id = id;
        this.threads = threads;
        this.runtime = runtime;
        this.delaytime = delaytime;
        this.status = status;
        this.type = type;
        this.timestatus = timestatus;
        this.ids = ids;
        this.timetask = timetask;
        this.jobname = jobname;
        this.jobgroup = jobgroup;
        this.jobdescribe = jobdescribe;
        this.deletestate = deletestate;
    }

    @Override
    public String toString() {
        return "Timer_type_config{" +
                "threads='" + threads + '\'' +
                ", runtime='" + runtime + '\'' +
                ", delaytime='" + delaytime + '\'' +
                ", ifretry='" + ifretry + '\'' +
                ", ifoutinterval='" + ifoutinterval + '\'' +
                ", ifcustomlistener='" + ifcustomlistener + '\'' +
                ", ifrecordlogjtl='" + ifrecordlogjtl + '\'' +
                ", ifrecordlogjmeter='" + ifrecordlogjmeter + '\'' +
                ", ifbetweenvalue='" + ifbetweenvalue + '\'' +
                ", threadname='" + threadname + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", timestatus='" + timestatus + '\'' +
                ", ids='" + ids + '\'' +
                ", timetask='" + timetask + '\'' +
                '}';
    }
}