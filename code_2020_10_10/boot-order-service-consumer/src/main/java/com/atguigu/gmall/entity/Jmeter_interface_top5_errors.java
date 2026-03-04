package com.atguigu.gmall.entity;

import java.util.List;

public class Jmeter_interface_top5_errors {



    private Integer id;

    private String scriptname;

    private String threads;

    private String runtime;

    private String sample;

    private String samples;

    private String errors;

    private String error1;

    private String errors1;

    private String error2;

    private String errors2;

    private String error3;

    private String errors3;

    private String error4;

    private String errors4;

    private String error5;

    private String errors5;

    private String error6;

    private String errors6;

    private String url;

    private String jplpath;

    private String indexpath;

    private Integer states;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getScriptname() {
        return scriptname;
    }

    public void setScriptname(String scriptname) {
        this.scriptname = scriptname;
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

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getSamples() {
        return samples;
    }

    public void setSamples(String samples) {
        this.samples = samples;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getError1() {
        return error1;
    }

    public void setError1(String error1) {
        this.error1 = error1;
    }

    public String getErrors1() {
        return errors1;
    }

    public void setErrors1(String errors1) {
        this.errors1 = errors1;
    }

    public String getError2() {
        return error2;
    }

    public void setError2(String error2) {
        this.error2 = error2;
    }

    public String getErrors2() {
        return errors2;
    }

    public void setErrors2(String errors2) {
        this.errors2 = errors2;
    }

    public String getError3() {
        return error3;
    }

    public void setError3(String error3) {
        this.error3 = error3;
    }

    public String getErrors3() {
        return errors3;
    }

    public void setErrors3(String errors3) {
        this.errors3 = errors3;
    }

    public String getError4() {
        return error4;
    }

    public void setError4(String error4) {
        this.error4 = error4;
    }

    public String getErrors4() {
        return errors4;
    }

    public void setErrors4(String errors4) {
        this.errors4 = errors4;
    }

    public String getError5() {
        return error5;
    }

    public void setError5(String error5) {
        this.error5 = error5;
    }

    public String getErrors5() {
        return errors5;
    }

    public void setErrors5(String errors5) {
        this.errors5 = errors5;
    }

    public String getError6() {
        return error6;
    }

    public void setError6(String error6) {
        this.error6 = error6;
    }

    public String getErrors6() {
        return errors6;
    }

    public void setErrors6(String errors6) {
        this.errors6 = errors6;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJplpath() {
        return jplpath;
    }

    public void setJplpath(String jplpath) {
        this.jplpath = jplpath;
    }

    public String getIndexpath() {
        return indexpath;
    }

    public void setIndexpath(String indexpath) {
        this.indexpath = indexpath;
    }

    public Integer getStates() {
        return states;
    }

    public void setStates(Integer states) {
        this.states = states;
    }

    public Jmeter_interface_top5_errors(){}

    public Jmeter_interface_top5_errors(Integer id, String scriptname, String threads, String runtime, String sample, String samples, String errors, String error1, String errors1, String error2, String errors2, String error3, String errors3, String error4, String errors4, String error5, String errors5, String error6, String errors6, String url, String jplpath, String indexpath, Integer states) {
        this.id = id;
        this.scriptname = scriptname;
        this.threads = threads;
        this.runtime = runtime;
        this.sample = sample;
        this.samples = samples;
        this.errors = errors;
        this.error1 = error1;
        this.errors1 = errors1;
        this.error2 = error2;
        this.errors2 = errors2;
        this.error3 = error3;
        this.errors3 = errors3;
        this.error4 = error4;
        this.errors4 = errors4;
        this.error5 = error5;
        this.errors5 = errors5;
        this.error6 = error6;
        this.errors6 = errors6;
        this.url = url;
        this.jplpath = jplpath;
        this.indexpath = indexpath;
        this.states = states;
    }

    public Jmeter_interface_top5_errors(Integer id, String scriptname, String threads, String runtime, String sample, String samples, String errors, String error1, String errors1, String error2, String errors2, String error3, String errors3, String error4, String errors4, String error5, String errors5, String error6, String errors6, String url) {
        this.id = id;
        this.runtime = runtime;
        this.scriptname = scriptname;
        this.threads = threads;
        this.url = url;
        this.sample = sample;
        this.samples = samples;
        this.errors = errors;
        this.error1 = error1;
        this.errors1 = errors1;
        this.error2 = error2;
        this.errors2 = errors2;
        this.error3 = error3;
        this.errors3 = errors3;
        this.error4 = error4;
        this.errors4 = errors4;
        this.error5 = error5;
        this.errors5 = errors5;
        this.error6 = error6;
        this.errors6 = errors6;

    }

    public Jmeter_interface_top5_errors(String scriptname, String threads, String runtime, List<String> list, String url, String jplpath, String indexpath, int states) {
        this.scriptname = scriptname;
        this.threads = threads;
        this.runtime = runtime;
        this.sample = list.get(0);
        this.samples = list.get(1);
        this.errors = list.get(2);
        this.error1 = list.get(3);
        this.errors1 = list.get(4);
        this.error2 = list.get(5);
        this.errors2 = list.get(6);
        this.error3 = list.get(7);
        this.errors3 = list.get(8);
        this.error4 = list.get(9);
        this.errors4 = list.get(10);
        this.error5 = list.get(11);
        this.errors5 = list.get(12);
        this.url = url;
        this.jplpath = jplpath;
        this.indexpath = indexpath;
        this.states = states;
    }


}