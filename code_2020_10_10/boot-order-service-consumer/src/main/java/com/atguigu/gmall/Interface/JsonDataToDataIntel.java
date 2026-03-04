package com.atguigu.gmall.Interface;

import javax.servlet.http.HttpSession;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-9-28
 */
public interface JsonDataToDataIntel {

    public void reportIndex(String jmeterPathBin, String jsFile, String thread, StringBuffer nodeIp, String jtlReportfile, Integer runId, String currentTime, String jmeterScriptName, String scriptNamePath,String perfstarttime ,HttpSession session) throws Exception ;


    public  String formatDouble(double d) ;


    public  String getType(Object object) ;


    public StringBuffer runTime(String jtlReportfile) ;




}
