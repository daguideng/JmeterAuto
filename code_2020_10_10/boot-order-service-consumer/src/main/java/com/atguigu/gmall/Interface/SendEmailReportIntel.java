package com.atguigu.gmall.Interface;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-9-28
 */
public interface SendEmailReportIntel {

    public StringBuffer writeEmailReport() throws Exception ;

    public StringBuffer writeEmailReport(String emailReportDir, String reportHtml, String scriptName, int scriptCount, int scriptSumSize, int X, int lastScriptNum) throws Exception  ;

    public String createHtml(String DirFilePath, String emailReportFile) ;

  //  public JSONArray getReportList(String SingleType, String scriptName) throws Exception ;

    public StringBuffer sendSingTyleModel(String emailReportPath) ;

    public StringBuffer writeLinkReport(String id,String  time) throws Exception ;


    public StringBuffer toDetailReport(String lastruntime, String scriptName, StringBuffer sbb) throws Exception ;



    public StringBuffer toErrorDetailReport(String lastruntime, String transactionName,String scriptname,StringBuffer sbb) throws Exception;

    public StringBuffer selectProgressReport(Integer state ,StringBuffer sbf) throws Exception ;

    public StringBuffer writeLinkHistoryReport(String scriptName) throws Exception;

}
