package com.atguigu.gmall.Interface;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-9-26
 */
public interface RunScriptControllerIntel {

    public Map <String, Object> getRunmanyScript(HttpSession session, String[] ids, JSONObject json, HttpServletRequest request) throws Exception;


    public String getRunJmeterNode() throws UnsupportedEncodingException, InterruptedException;

}
