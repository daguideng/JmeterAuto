package com.atguigu.gmall.common.interceptor;

import org.slf4j.MDC;

public class MDCHolder {

	
    public static final String TRACE_ID = "traceId";
    public static final String USER_IP  = "userIp";
    public static final String SECRET_KEY  = "secretKey";
    
    public static final String USERNAME = "employeeNumber";

    public static void putTraceId(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }

    public  static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    public static void clear() {
        MDC.remove(TRACE_ID);
        MDC.remove(USER_IP);
        MDC.remove(USERNAME);
    }

    public static void putUserName(String userName) {
        MDC.put(USERNAME, userName);
    }

    public  static String getUserIp() {
        return MDC.get(USER_IP);
    }

    public static void putUserIp(String userIp) {
        MDC.put(USER_IP, userIp);
    }
    
    public static void putSecretKey(String secretKey) {
        MDC.put(SECRET_KEY, secretKey);
    }
	
    public static String getSecretKey() {
        return MDC.get(SECRET_KEY);
    }
	
}
