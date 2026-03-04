package com.atguigu.gmall.Interface;

import java.util.Map;
import javax.servlet.http.HttpSession;
import org.dom4j.DocumentException;

/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2018-9-27
 */
public interface JmeterPostParameterIntel {

    public Map<String,Object> jmeterperslist(String configxml, HttpSession session) throws DocumentException ;

}
