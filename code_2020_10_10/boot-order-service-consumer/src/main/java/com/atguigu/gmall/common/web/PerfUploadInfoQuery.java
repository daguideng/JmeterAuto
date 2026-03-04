package com.atguigu.gmall.common.web;

import com.atguigu.gmall.common.page.Criterion;
import com.atguigu.gmall.common.page.QueryBase;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-8-14
 */
public class PerfUploadInfoQuery extends QueryBase implements Serializable {

    private Integer id;

    private String uploadtime;

    private String lastruntime;

    private String username;

    private String scriptname;

    private String interfacename;

    private String scripttype;

    private String operationtype;

    private String scriptpath;

    private String runbutton;

    private String scriptrunorder;

    private String testname0;

    private String testname1;

    private String testname2;

    private String testname3;

    private String url0;

    private String url1;

    private String url2;

    private String url3;

    private String url0replace;

    private String url1replace;

    private String url2replace;

    private String url3replace;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }

    public String getLastruntime() {
        return lastruntime;
    }

    public void setLastruntime(String lastruntime) {
        this.lastruntime = lastruntime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScriptname() {
        return scriptname;
    }

    public void setScriptname(String scriptname) {
        this.scriptname = scriptname;
    }

    public String getInterfacename() {
        return interfacename;
    }

    public void setInterfacename(String interfacename) {
        this.interfacename = interfacename;
    }

    public String getScripttype() {
        return scripttype;
    }

    public void setScripttype(String scripttype) {
        this.scripttype = scripttype;
    }

    public String getOperationtype() {
        return operationtype;
    }

    public void setOperationtype(String operationtype) {
        this.operationtype = operationtype;
    }

    public String getScriptpath() {
        return scriptpath;
    }

    public void setScriptpath(String scriptpath) {
        this.scriptpath = scriptpath;
    }

    public String getRunbutton() {
        return runbutton;
    }

    public void setRunbutton(String runbutton) {
        this.runbutton = runbutton;
    }

    public String getScriptrunorder() {
        return scriptrunorder;
    }

    public void setScriptrunorder(String scriptrunorder) {
        this.scriptrunorder = scriptrunorder;
    }

    public String getTestname0() {
        return testname0;
    }

    public void setTestname0(String testname0) {
        this.testname0 = testname0;
    }

    public String getTestname1() {
        return testname1;
    }

    public void setTestname1(String testname1) {
        this.testname1 = testname1;
    }

    public String getTestname2() {
        return testname2;
    }

    public void setTestname2(String testname2) {
        this.testname2 = testname2;
    }

    public String getTestname3() {
        return testname3;
    }

    public void setTestname3(String testname3) {
        this.testname3 = testname3;
    }

    public String getUrl0() {
        return url0;
    }

    public void setUrl0(String url0) {
        this.url0 = url0;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getUrl3() {
        return url3;
    }

    public void setUrl3(String url3) {
        this.url3 = url3;
    }

    public String getUrl0replace() {
        return url0replace;
    }

    public void setUrl0replace(String url0replace) {
        this.url0replace = url0replace;
    }

    public String getUrl1replace() {
        return url1replace;
    }

    public void setUrl1replace(String url1replace) {
        this.url1replace = url1replace;
    }

    public String getUrl2replace() {
        return url2replace;
    }

    public void setUrl2replace(String url2replace) {
        this.url2replace = url2replace;
    }

    public String getUrl3replace() {
        return url3replace;
    }

    public void setUrl3replace(String url3replace) {
        this.url3replace = url3replace;
    }

    @Override
    public String toString() {
        return "PerfUploadInfoQuery{" +
                "id=" + id +
                ", uploadtime='" + uploadtime + '\'' +
                ", lastruntime='" + lastruntime + '\'' +
                ", username='" + username + '\'' +
                ", scriptname='" + scriptname + '\'' +
                ", interfacename='" + interfacename + '\'' +
                ", scripttype='" + scripttype + '\'' +
                ", operationtype='" + operationtype + '\'' +
                ", scriptpath='" + scriptpath + '\'' +
                ", runbutton='" + runbutton + '\'' +
                ", scriptrunorder='" + scriptrunorder + '\'' +
                ", testname0='" + testname0 + '\'' +
                ", testname1='" + testname1 + '\'' +
                ", testname2='" + testname2 + '\'' +
                ", testname3='" + testname3 + '\'' +
                ", url0='" + url0 + '\'' +
                ", url1='" + url1 + '\'' +
                ", url2='" + url2 + '\'' +
                ", url3='" + url3 + '\'' +
                ", url0replace='" + url0replace + '\'' +
                ", url1replace='" + url1replace + '\'' +
                ", url2replace='" + url2replace + '\'' +
                ", url3replace='" + url3replace + '\'' +
                '}';
    }

    public PerfUploadInfoQuery(){

    }


    @Override
    public List<Criterion> getCriterion() {
        List<Criterion> criterion = new ArrayList<>();
        if (StringUtils.isNotEmpty(this.search)) {
            if (this.scriptname != null ) {
                Criterion c = new Criterion("scriptname like ", "%"+this.scriptname+"%");
                criterion.add(c);
            }
        }

        return criterion.isEmpty() ? null : criterion;
    }




    @Override
    public PageBounds getPB() {
        this.setProperty("id");
        this.setDirection(Order.Direction.DESC);
        return super.getPB();
    }


    public PageBounds getPBAsc() {
        this.setProperty("id");
        this.setDirection(Order.Direction.ASC);
        return super.getPB();
    }


}
