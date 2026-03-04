package com.atguigu.gmall.common.interceptor;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

public class LogTraceFiler implements Filter {


    @Override
    public void destroy() {
        MDCHolder.clear();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        String traceId = request.getHeader("traceId");

        if(StringUtils.isEmpty(traceId)) {
            traceId = UUID.randomUUID().toString();
        }

        MDCHolder.putTraceId(traceId);
        MDCHolder.putUserIp(RequestUtil.getIp(request));

        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

}