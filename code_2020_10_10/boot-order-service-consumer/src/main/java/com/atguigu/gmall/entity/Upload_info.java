package com.atguigu.gmall.entity;

public class Upload_info {
    private Integer id;

    private String uploadtime;

    private String lastruntime;

    private String perflastruntime ;

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

    private String state;



    private String interstate ;


    public String getPerflastruntime() {
        return perflastruntime;
    }

    public void setPerflastruntime(String perflastruntime) {
        this.perflastruntime = perflastruntime;
    }

    public String getInterstate() {
        return interstate;
    }

    public void setInterstate(String interstate) {
        this.interstate = interstate;
    }

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
        this.uploadtime = uploadtime == null ? null : uploadtime.trim();
    }

    public String getLastruntime() {
        return lastruntime;
    }

    public void setLastruntime(String lastruntime) {
        this.lastruntime = lastruntime == null ? null : lastruntime.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getScriptname() {
        return scriptname;
    }

    public void setScriptname(String scriptname) {
        this.scriptname = scriptname == null ? null : scriptname.trim();
    }

    public String getInterfacename() {
        return interfacename;
    }

    public void setInterfacename(String interfacename) {
        this.interfacename = interfacename == null ? null : interfacename.trim();
    }

    public String getScripttype() {
        return scripttype;
    }

    public void setScripttype(String scripttype) {
        this.scripttype = scripttype == null ? null : scripttype.trim();
    }

    public String getOperationtype() {
        return operationtype;
    }

    public void setOperationtype(String operationtype) {
        this.operationtype = operationtype == null ? null : operationtype.trim();
    }

    public String getScriptpath() {
        return scriptpath;
    }

    public void setScriptpath(String scriptpath) {
        this.scriptpath = scriptpath == null ? null : scriptpath.trim();
    }

    public String getRunbutton() {
        return runbutton;
    }

    public void setRunbutton(String runbutton) {
        this.runbutton = runbutton == null ? null : runbutton.trim();
    }

    public String getScriptrunorder() {
        return scriptrunorder;
    }

    public void setScriptrunorder(String scriptrunorder) {
        this.scriptrunorder = scriptrunorder == null ? null : scriptrunorder.trim();
    }

    public String getTestname0() {
        return testname0;
    }

    public void setTestname0(String testname0) {
        this.testname0 = testname0 == null ? null : testname0.trim();
    }

    public String getTestname1() {
        return testname1;
    }

    public void setTestname1(String testname1) {
        this.testname1 = testname1 == null ? null : testname1.trim();
    }

    public String getTestname2() {
        return testname2;
    }

    public void setTestname2(String testname2) {
        this.testname2 = testname2 == null ? null : testname2.trim();
    }

    public String getTestname3() {
        return testname3;
    }

    public void setTestname3(String testname3) {
        this.testname3 = testname3 == null ? null : testname3.trim();
    }

    public String getUrl0() {
        return url0;
    }

    public void setUrl0(String url0) {
        this.url0 = url0 == null ? null : url0.trim();
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1 == null ? null : url1.trim();
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2 == null ? null : url2.trim();
    }

    public String getUrl3() {
        return url3;
    }

    public void setUrl3(String url3) {
        this.url3 = url3 == null ? null : url3.trim();
    }

    public String getUrl0replace() {
        return url0replace;
    }

    public void setUrl0replace(String url0replace) {
        this.url0replace = url0replace == null ? null : url0replace.trim();
    }

    public String getUrl1replace() {
        return url1replace;
    }

    public void setUrl1replace(String url1replace) {
        this.url1replace = url1replace == null ? null : url1replace.trim();
    }

    public String getUrl2replace() {
        return url2replace;
    }

    public void setUrl2replace(String url2replace) {
        this.url2replace = url2replace == null ? null : url2replace.trim();
    }

    public String getUrl3replace() {
        return url3replace;
    }

    public void setUrl3replace(String url3replace) {
        this.url3replace = url3replace == null ? null : url3replace.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }


    public Upload_info(){}




    public Upload_info( String uploadtime, String lastruntime, String username, String scriptname, String interfacename, String scripttype, String operationtype, String scriptpath, String runbutton, String scriptrunorder, String testname0, String testname1, String testname2, String testname3, String url0, String url1, String url2, String url3, String url0replace, String url1replace, String url2replace, String url3replace, String state) {

        this.uploadtime = uploadtime;
        this.lastruntime = lastruntime;
        this.username = username;
        this.scriptname = scriptname;
        this.interfacename = interfacename;
        this.scripttype = scripttype;
        this.operationtype = operationtype;
        this.scriptpath = scriptpath;
        this.runbutton = runbutton;
        this.scriptrunorder = scriptrunorder;
        this.testname0 = testname0;
        this.testname1 = testname1;
        this.testname2 = testname2;
        this.testname3 = testname3;
        this.url0 = url0;
        this.url1 = url1;
        this.url2 = url2;
        this.url3 = url3;
        this.url0replace = url0replace;
        this.url1replace = url1replace;
        this.url2replace = url2replace;
        this.url3replace = url3replace;
        this.state = state;
    }



    public Upload_info( String uploadtime, String lastruntime, String username, String scriptname, String interfacename, String scripttype, String operationtype, String scriptpath, String runbutton, String scriptrunorder, String testname0, String testname1, String testname2, String testname3, String url0, String url1, String url2, String url3, String url0replace, String url1replace, String url2replace, String url3replace, String state,String interstate) {

        this.uploadtime = uploadtime;
        this.lastruntime = lastruntime;
        this.username = username;
        this.scriptname = scriptname;
        this.interfacename = interfacename;
        this.scripttype = scripttype;
        this.operationtype = operationtype;
        this.scriptpath = scriptpath;
        this.runbutton = runbutton;
        this.scriptrunorder = scriptrunorder;
        this.testname0 = testname0;
        this.testname1 = testname1;
        this.testname2 = testname2;
        this.testname3 = testname3;
        this.url0 = url0;
        this.url1 = url1;
        this.url2 = url2;
        this.url3 = url3;
        this.url0replace = url0replace;
        this.url1replace = url1replace;
        this.url2replace = url2replace;
        this.url3replace = url3replace;
        this.state = state;
        this.interstate = interstate ;
    }





    public Upload_info(String uploadtime, String lastruntime, String username, String scriptname, String interfacename, String scripttype, String operationtype, String scriptpath, String runbutton, String scriptrunorder, String testname0, String testname1, String testname2, String testname3, String url0, String url1, String url2, String url3, String url0replace, String url1replace, String url2replace, String url3replace) {

        this.uploadtime = uploadtime;
        this.lastruntime = lastruntime;
        this.username = username;
        this.scriptname = scriptname;
        this.interfacename = interfacename;
        this.scripttype = scripttype;
        this.operationtype = operationtype;
        this.scriptpath = scriptpath;
        this.runbutton = runbutton;
        this.scriptrunorder = scriptrunorder;
        this.testname0 = testname0;
        this.testname1 = testname1;
        this.testname2 = testname2;
        this.testname3 = testname3;
        this.url0 = url0;
        this.url1 = url1;
        this.url2 = url2;
        this.url3 = url3;
        this.url0replace = url0replace;
        this.url1replace = url1replace;
        this.url2replace = url2replace;
        this.url3replace = url3replace;

    }



    public Upload_info(Integer id, String uploadtime, String lastruntime, String username, String scriptname, String interfacename, String scripttype, String operationtype, String scriptpath, String runbutton, String scriptrunorder, String testname0, String testname1, String testname2, String testname3, String url0, String url1, String url2, String url3, String url0replace, String url1replace, String url2replace, String url3replace) {
        this.id = id ;
        this.uploadtime = uploadtime;
        this.lastruntime = lastruntime;
        this.username = username;
        this.scriptname = scriptname;
        this.interfacename = interfacename;
        this.scripttype = scripttype;
        this.operationtype = operationtype;
        this.scriptpath = scriptpath;
        this.runbutton = runbutton;
        this.scriptrunorder = scriptrunorder;
        this.testname0 = testname0;
        this.testname1 = testname1;
        this.testname2 = testname2;
        this.testname3 = testname3;
        this.url0 = url0;
        this.url1 = url1;
        this.url2 = url2;
        this.url3 = url3;
        this.url0replace = url0replace;
        this.url1replace = url1replace;
        this.url2replace = url2replace;
        this.url3replace = url3replace;

    }






}