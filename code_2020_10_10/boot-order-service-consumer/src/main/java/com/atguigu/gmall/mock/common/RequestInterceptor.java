package com.atguigu.gmall.mock.common;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.gmall.entity.Mock_config;
import com.atguigu.gmall.mock.common.RequestType.RequestPostType.RequestPostBodyWrapper;
import com.atguigu.gmall.mock.common.RequestType.RequestPostType.RequestPostUrl;
import com.atguigu.gmall.mock.service.Impl.MockServiceConfigImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class RequestInterceptor implements HandlerInterceptor {
    //@Autowired
    //private MockServiceConfig mockServiceConfig ;

    @Autowired
    MockServiceConfigImpl mockServiceConfigImpl;

    @Autowired
    private RequestPostUrl requestPostUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        int count = 0;
        String RemoteHost = request.getRemoteHost();
        int RemotePort = request.getRemotePort();

        //map：参数
        HashMap<String, Object> paramMap = new LinkedHashMap<String, Object>();
        String requestURl = null;

        List<Mock_config> mock_configs = null;

        String method = request.getMethod();


        //一.根据请求的类型得到url,及请求参数:

        /**------------------------post类型------------------------**/


        if ("POST".equalsIgnoreCase(method)) {
            //1. 根据post请求类型得到相关的url,及请求参数
            requestURl = requestPostUrl.getRequestUrl(request);

            //1.根据用户请求的url到数据库中去匹配：
            mock_configs = mockServiceConfigImpl.interceptorList(requestURl);


            //2. post的body参数：
            RequestPostBodyWrapper requestWrapper = new RequestPostBodyWrapper(request);
            String jsonBody = requestWrapper.getBody();
            System.out.println("jsonBody----》" + jsonBody);
            // jsonString转换成Map
            if (!"".equals(jsonBody)) {

                //post请求中的有body类型的请求：
                paramMap = JSON.parseObject(jsonBody, new TypeReference<HashMap<String, Object>>() {
                });
                System.out.println("jsonMap: " + paramMap.toString());


                log.info("请求类型是:{},请求url地址是:{},请求参数是：{}", method, requestURl, jsonBody);
            } else {

                //post请求中from请求类型:
                Map<String, String[]> allMap = request.getParameterMap();
                System.out.println("allMap-->" + allMap.size());

                for (String key : allMap.keySet()) {
                    String[] strArr = (String[]) allMap.get(key);
                    for (String val : strArr) {
                        if (null != val && !"".equals(val)) {
                            System.out.println("key=" + key + " " + "value=" + val);
                            paramMap.put(key, val);
                        }
                    }
                }
                log.info("post请求中from请求类型参数值：{}", paramMap);


            }


        }


        /**-----------------------Get类型-------------------------**/
        if ("GET".equalsIgnoreCase(method)) {
            //1. 根据get请求类型得到相关的url,
            requestURl = requestPostUrl.getRequestUrl(request);

           

            //1.根据用户请求的url到数据库中去匹配：
            mock_configs = mockServiceConfigImpl.interceptorList(requestURl);

            //如果数据库中与输入的url没有对上则放行url：


            //2. 根据get请求参数
            Map<String, String[]> allMap = request.getParameterMap();

            System.out.println("allMap-->" + allMap.size());

            for (String key : allMap.keySet()) {
                String[] strArr = (String[]) allMap.get(key);
                for (String val : strArr) {
                    if (null != val && !"".equals(val)) {
                        System.out.println("key=" + key + " " + "value=" + val);
                        paramMap.put(key, val);
                    }
                }
            }


            log.info("请求类型是:{},请求url地址是:{},请求参数是：{}", method, requestURl, paramMap);

        }


        int result_compare = 0;

        String mockResult = null;
        String timeout = null;
        String tag = null;
        String prefixOption = null;


        for (Mock_config mock_value : mock_configs) {

            //如果没有参数的mock请求
            if (paramMap.size() != mock_value.getConfigRules().length()) {   //有参数的mock请求:

                //1.得到config_rules
                String[] config_rules = mock_value.getConfigRules().split(",");

                //2.根据config_rules数组长度与客户端获取长度比较
                if (config_rules.length == paramMap.size()) {

                    //3.长度一致再比较每一个参数及value： a=b
                    for (String param_rule : config_rules) {

                        for (Map.Entry<String, Object> m : paramMap.entrySet()) {
                            System.out.println("key:" + m.getKey() + " value:" + m.getValue());
                            String key = m.getKey();
                            String value = m.getValue().toString();

                            String map_compare = key + "=" + value;

                            if (param_rule.equals(map_compare)) {

                                result_compare++;
                                //4. 如果每个参数值都比较了,则取出这条对应的返回json:
                                if (config_rules.length == result_compare) {
                                    mockResult = mock_value.getMockResult();
                                    timeout = mock_value.getTimeout();
                                    tag = mock_value.getTag();
                                    prefixOption = mock_value.getPrefixOption();
                                    break;
                                }
                            }

                        }

                    }

                }

            } else {
                //5. 无参数的mocke请求,按优先级返回结果：
                mockResult = mock_value.getMockResult();
                timeout = mock_value.getTimeout();
                tag = mock_value.getTag();
                prefixOption = mock_value.getPrefixOption();
                break;

            }

        }


        //6.如果此条mock配置已禁用,返回给前端：
        if ("1".equals(tag)) {
            HashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("code", 10003);
            linkedHashMap.put("msg", "此条mock已被禁用,请检查mock是否是启用状态!!");
            linkedHashMap.put("data", "祝你开心,使用mock过程中有好建议请联系:贵哥");


            mockResult = new ObjectMapper().writeValueAsString(linkedHashMap);

            try {

                response.reset();
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.write(mockResult);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();

            }

            return true;

        }


        //7.把结果返回给前端：
        System.out.println("mockResult--->" + mockResult);
        System.out.println("timeout--->" + timeout);
        if (null != timeout && !"".equals(timeout)) {
            Thread.sleep(Long.valueOf((long) Double.parseDouble(timeout)) * 1000);
        }


        if (null != prefixOption && !"".equals(prefixOption)) {
            mockResult = prefix_option(prefixOption, mockResult);
            log.info("mock返回的结果：{}", mockResult);
        }

        try {
            response.reset();
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(mockResult.toString());
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return true;


    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //   System.out.println("====后处理/后置返回处理");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //   System.out.println("====完成处理/后置最终处理");
    }


    /***
     * 前置操作
     */

    public String prefix_option(String prefixOption, String mockResult) {

        String[] strArr = prefixOption.split(",");

        for (String val : strArr) {
            if (null != val && !"".equals(val)) {
                String key = val.split("=")[0];
                String value = val.split("=")[1];
                mockResult = mockResult.replace("$" + key, value);
            }
        }

        return mockResult;

    }


}
