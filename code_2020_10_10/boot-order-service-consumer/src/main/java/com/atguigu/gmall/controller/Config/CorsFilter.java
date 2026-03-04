package com.atguigu.gmall.controller.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
/**
 * @Author: dengdagui
 * @Description:
 * @Date: Created in 2019-9-26
 */
@Configuration
public class CorsFilter implements Filter {  //implements Filter



    private final List<String> allowedOrigins = Arrays.asList("http://localhost:9528");

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
      //  HttpServletResponse res = (HttpServletResponse) response;
     //   HttpServletRequest req = (HttpServletRequest) request;
        // Access-Control-Allow-Origin

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;




         String origin = httpServletRequest.getHeader("Origin");
         httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
         httpServletResponse.setHeader("Access-Control-Allow-Origin",  origin);
         httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
         httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
         httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept,authorization,x-token");
         httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
         httpServletResponse.setHeader("Access-Control-Request-Headers", "content-type");
         httpServletResponse.setHeader("Access-Control-Request-Method", "POST");
         httpServletResponse.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Mobile Safari/537.36");



        // 从请求头中获取header
        /**
        String origin = httpServletRequest.getHeader("origin");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", origin);
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, HEAD, PUT,PATCH, DELETE");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin");
         **/
        // 接受跨域的cookie
        /**
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
         **/





        /*
        String origin = req.getHeader("Origin");
        res.setHeader("Access-Control-Request-Method",  "POST");
        res.setHeader("Access-Control-Request-Headers",  "content-type");
        res.setHeader("Accept-Encoding", "gzip, deflate, br");
        res.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        res.setHeader("Connection", "keep-alive");
        res.setHeader("Content-Type", "application/json;charset=UTF-8");
        res.setHeader("Origin", origin);
        res.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
       ***/



        /**
        String origin = req.getHeader("Origin");
        res.setHeader("Access-Control-Request-Headers",  "content-type");
        res.setHeader("Access-Control-Request-Method:", "POST");
        res.setHeader("Origin:", "http://localhost:8081");
        res.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
       ***/


        /**
        String origin = req.getHeader("Origin");

        res.setHeader("Accept-Encoding", "gzip, deflate, br");
        res.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        res.setHeader("Content-Type:", "application/json;charset=UTF-8");
        res.setHeader("Origin", "http://localhost:8081");
        res.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
       **/



        /**
        res.setHeader("Accept","application/json, text/plain, *");
        res.setHeader("Accept-Encoding","gzip, deflate, br");
        res.setHeader("Accept-Language","zh-CN,zh;q=0.9");
        res.setHeader("Connection","keep-alive");
        res.setHeader("Content-Length","18");
        res.setHeader("Content-Type","application/x-www-form-urlencoded");

       **/

        /**
        res.setHeader("Connection","Upgrade");
        res.setHeader("Upgrade","websocket");
        res.setHeader("Sec-WebSocket-Extensions", "permessage-deflate;client_max_window_bits");
        res.setHeader("Sec-WebSocket-Version", "13");
         **/


        /**
        res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        res.setHeader("Sec-WebSocket-Extensions", "permessage-deflate;client_max_window_bits");
        res.setHeader("Upgrade", "websocket");
        res.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        res.setHeader("Connection", "Upgrad");
        res.setHeader("Accept-Encoding", "gzip, deflate");
        res.setHeader("Connectio", "keep-alive");
        **/


        //请求之前都会发送一个OPTIONS来测试服务器是否支持跨域,直接return
        if (httpServletRequest.getMethod().equals("OPTIONS")) {
            httpServletResponse.setStatus(200);
            return;
        }

        chain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {

    }






}
