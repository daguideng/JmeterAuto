package com.atguigu.gmall.Impl.timerImpl;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-9-2
 */
import java.io.Serializable;

/**
 * @Author: dengdagui
 * @Description: post perfconfig
 * @Date: Created in 2018-10-25
 */
public class TimerModel implements Serializable {

    private String vuser ;
    private String runtime ;
    private String sleeptime  ;
    private String delaytime ;
    private String retry ;
    private String retryVal ;
    private String output ;
    private String outputInterval;
    private String customListener ;
    private String jtlListener ;
    private String logListener  ;
    private String betweenValue ;
    private String threadname ;
    private String status ;


    private String type ;
    private String timestatus ;
    private String ids ;
    private String timetask ;
    private String jobname ;
    private String triggername ;
    private String jobgroup ;
    private String jobdescribe ;
    private Integer deletestate ;


    public String getVuser() {
        return vuser;
    }

    public void setVuser(String vuser) {
        this.vuser = vuser;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getSleeptime() {
        return sleeptime;
    }

    public void setSleeptime(String sleeptime) {
        this.sleeptime = sleeptime;
    }

    public String getDelaytime() {
        return delaytime;
    }

    public void setDelaytime(String delaytime) {
        this.delaytime = delaytime;
    }

    public String getRetry() {
        return retry;
    }

    public void setRetry(String retry) {
        this.retry = retry;
    }

    public String getRetryVal() {
        return retryVal;
    }

    public void setRetryVal(String retryVal) {
        this.retryVal = retryVal;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getOutputInterval() {
        return outputInterval;
    }

    public void setOutputInterval(String outputInterval) {
        this.outputInterval = outputInterval;
    }

    public String getCustomListener() {
        return customListener;
    }

    public void setCustomListener(String customListener) {
        this.customListener = customListener;
    }

    public String getJtlListener() {
        return jtlListener;
    }

    public void setJtlListener(String jtlListener) {
        this.jtlListener = jtlListener;
    }

    public String getLogListener() {
        return logListener;
    }

    public void setLogListener(String logListener) {
        this.logListener = logListener;
    }

    public String getBetweenValue() {
        return betweenValue;
    }

    public void setBetweenValue(String betweenValue) {
        this.betweenValue = betweenValue;
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

    public Integer getDeletestate() {
        return deletestate;
    }

    public void setDeletestate(Integer deletestate) {
        this.deletestate = deletestate;
    }


    public TimerModel(){}

    public TimerModel(String vuser, String runtime, String sleeptime, String delaytime, String retry, String retryVal, String output, String outputInterval, String customListener, String jtlListener, String logListener, String betweenValue, String threadname, String status, String type, String timestatus, String ids, String timetask, String jobname, String triggername, String jobgroup, String jobdescribe, Integer deletestate) {
        this.vuser = vuser;
        this.runtime = runtime;
        this.sleeptime = sleeptime;
        this.delaytime = delaytime;
        this.retry = retry;
        this.retryVal = retryVal;
        this.output = output;
        this.outputInterval = outputInterval;
        this.customListener = customListener;
        this.jtlListener = jtlListener;
        this.logListener = logListener;
        this.betweenValue = betweenValue;
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
        this.deletestate = deletestate;
    }

    public TimerModel(String vuser, String runtime, String sleeptime, String delaytime, String retry, String retryVal, String output, String outputInterval, String customListener, String jtlListener, String logListener, String betweenValue, String threadname, String type, String ids, String timetask, String jobname) {
        this.vuser = vuser;
        this.runtime = runtime;
        this.sleeptime = sleeptime;
        this.delaytime = delaytime;
        this.retry = retry;
        this.retryVal = retryVal;
        this.output = output;
        this.outputInterval = outputInterval;
        this.customListener = customListener;
        this.jtlListener = jtlListener;
        this.logListener = logListener;
        this.betweenValue = betweenValue;
        this.threadname = threadname;
        this.type = type;
        this.ids = ids;
        this.timetask = timetask;
        this.jobname = jobname;
    }
}