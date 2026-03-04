package com.atguigu.gmall.Interface;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpSession;
import java.util.SortedMap;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-9-27
 */
public interface JmeterMainIntel {

    public void jmeterMainEnter(String jmeterControlNode, String scriptJmeterZip, String jmeterScriptName, int scriptCount, int scriptSumSize, String resultReportDir, String reportHtmlName, SortedMap <Integer, String> scriptNameMap, Integer runId, JSONObject json , HttpSession session) throws Exception ;

    }
