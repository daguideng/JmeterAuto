package com.atguigu.gmall.common.log;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.common.constant.RES_STATUS;
import com.atguigu.gmall.common.interceptor.RequestUtil;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;

/**
 * LoggerUtil ucredit logger
 *
 */
public class LoggerUtil {

    public static final String SEPARATOR = " ^| ";
	public static final CharSequence DEL = ".";
    
	public static final String IGNORE_MARK = "-";
	public static final String Ignore = "-";

    public static final String ERROR_LOG = "elk_error";

    public static final String APP_LOG = "elk_app";
    
    public static final String PHOENIX_LOG = "phoenix_log";


	public static final String DEFAULT_SYSTEM = "BC";
	public static final CharSequence Y = "Y";
	public static final CharSequence N = "N";

	public static final String clientIp = "clientIp";
	public static final String deviceInfo = "deviceInfo";
	public static final String deviceId = "deviceId";
	public static final String clientVersion = "clientVersion";
	public static final String platform = "platform";
	public static final String screenSize = "screenSize";
	public static final String network = "network";
	public static final String userId = "userId";
	public static final String systemId = "systemId";
	public static final String ZERO = "0";
	public static final String NULL_STRING = "null";

    public static String error(HttpServletRequest request, int code, long cost,
            String params) {
    	code = getSaCode(code);
		// 方法成功或失败
        String result = "N";
		// 客户端IP
        String clientIp = LoggerUtil.IGNORE_MARK;
		// 机型
        String deviceInfo = LoggerUtil.IGNORE_MARK;
		// 设备ID
        String deviceId = LoggerUtil.IGNORE_MARK;
		// 客户端版本号
        String clientVersion = LoggerUtil.IGNORE_MARK;
		// 操作系统类型
        String platform = LoggerUtil.IGNORE_MARK;
		// 屏幕尺寸
        String screenSize = LoggerUtil.IGNORE_MARK;
		// 网络环境
        String network = LoggerUtil.IGNORE_MARK;
		// 用户ID
        String userId = "";
		String systemId = "";

        if (request != null) {
            //clientIp = request.getParameter("clientIp");
            clientIp = RequestUtil.getIp(request);
            deviceInfo = request.getParameter("deviceInfo");
            deviceId = request.getParameter("deviceId");
            clientVersion = request.getParameter("clientVersion");
            platform = request.getParameter("platform");
            screenSize = request.getParameter("screenSize");
            network = request.getParameter("network");
            userId = request.getParameter("userId");
			systemId = StringUtils.isBlank(request.getParameter("systemId")) ? DEFAULT_SYSTEM : request
					.getParameter("systemId");
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.isEmpty(clientIp) ? LoggerUtil.IGNORE_MARK
            : clientIp);
        buffer.append(LoggerUtil.SEPARATOR);
        buffer.append(StringUtils.isEmpty(result) ? LoggerUtil.IGNORE_MARK : result);
        buffer.append(LoggerUtil.SEPARATOR);
        buffer.append(code == 0 ? LoggerUtil.IGNORE_MARK : code);
        buffer.append(LoggerUtil.SEPARATOR);
        buffer.append(cost == 0 ? LoggerUtil.IGNORE_MARK : cost);
        buffer.append(LoggerUtil.SEPARATOR);
        
        JSONObject object = new JSONObject();
        object.put("deviceInfo", deviceInfo);
        object.put("deviceId", deviceId);
        object.put("clientVersion", clientVersion);
        object.put("platform", platform);
        object.put("screenSize", screenSize);
        object.put("network", network);
        object.put("userId", userId);
		object.put("systemId", systemId);
        object.put("params", params);
        buffer.append(object.toJSONString());
        return buffer.toString();
    }

    public static String info(HttpServletRequest request, RES_STATUS status,
            long cost, Object... objects) {
        return LoggerUtil.info(request, status.code, cost, objects);
    }

    public static String info(String message) {
        return LoggerUtil.info(null, RES_STATUS.SUCCESS.code, 0, message);
    }

    public static String info(HttpServletRequest request, int code, long cost,
            Object... objects) {
    	code = getSaCode(code);
		// 方法成功或失败
        String result = "Y";
		// 客户端IP
        String clientIp = LoggerUtil.IGNORE_MARK;
		// 机型
        String deviceInfo = LoggerUtil.IGNORE_MARK;
		// 设备ID
        String deviceId = LoggerUtil.IGNORE_MARK;
		// 客户端版本号
        String clientVersion = LoggerUtil.IGNORE_MARK;
		// 操作系统类型
        String platform = LoggerUtil.IGNORE_MARK;
		// 屏幕尺寸
        String screenSize = LoggerUtil.IGNORE_MARK;
		// 网络环境
        String network = LoggerUtil.IGNORE_MARK;

		String systemId = "";

        if (request != null) {
            clientIp = request.getParameter("clientIp");
            deviceInfo = request.getParameter("deviceInfo");
            deviceId = request.getParameter("deviceId");
            clientVersion = request.getParameter("clientVersion");
            platform = request.getParameter("platform");
            screenSize = request.getParameter("screenSize");
            network = request.getParameter("network");
			systemId = StringUtils.isBlank(request.getParameter("systemId")) ? DEFAULT_SYSTEM : request
					.getParameter("systemId");
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.isBlank(clientIp) ? LoggerUtil.IGNORE_MARK
            : clientIp);
        buffer.append(LoggerUtil.SEPARATOR);
        buffer.append(result);
        buffer.append(LoggerUtil.SEPARATOR);
        buffer.append(code);
        buffer.append(LoggerUtil.SEPARATOR);
        buffer.append(cost == 0 ? "-" : cost);
        buffer.append(LoggerUtil.SEPARATOR);
        if (deviceInfo != null && !LoggerUtil.IGNORE_MARK.equals(deviceInfo)) {
            buffer.append("deviceInfo = ").append(deviceInfo);
            buffer.append(LoggerUtil.SEPARATOR);
        }
        if (deviceId != null && !LoggerUtil.IGNORE_MARK.equals(deviceId)) {
            buffer.append("deviceId = ").append(deviceId);
            buffer.append(LoggerUtil.SEPARATOR);
        }
        if (clientVersion != null
            && !LoggerUtil.IGNORE_MARK.equals(clientVersion)) {
            buffer.append("clientVersion = ").append(clientVersion);
            buffer.append(LoggerUtil.SEPARATOR);
        }
        if (platform != null && !LoggerUtil.IGNORE_MARK.equals(platform)) {
            buffer.append("platform = ").append(platform);
            buffer.append(LoggerUtil.SEPARATOR);
        }
        if (screenSize != null && !LoggerUtil.IGNORE_MARK.equals(screenSize)) {
            buffer.append("screenSize = ").append(screenSize);
            buffer.append(LoggerUtil.SEPARATOR);
        }
        if (network != null && !LoggerUtil.IGNORE_MARK.equals(network)) {
            buffer.append("network = ").append(network);
            buffer.append(LoggerUtil.SEPARATOR);
        }
		buffer.append("systemId = ").append(systemId);
		buffer.append(LoggerUtil.SEPARATOR);

        if (objects == null || objects.length == 0) {
            return buffer.toString();
        }
        for (Object obj : objects) {
            buffer.append(obj);
            buffer.append(LoggerUtil.SEPARATOR);
        }
        return buffer.toString();
    }

    public static String info(HttpServletRequest request, int code, long cost,
    		String params) {
    	code = getSaCode(code);
		// 方法成功或失败
        String result = "Y";
		// 客户端IP
        String clientIp = LoggerUtil.IGNORE_MARK;
		// 机型
        String deviceInfo = LoggerUtil.IGNORE_MARK;
		// 设备ID
        String deviceId = LoggerUtil.IGNORE_MARK;
		// 客户端版本号
        String clientVersion = LoggerUtil.IGNORE_MARK;
		// 操作系统类型
        String platform = LoggerUtil.IGNORE_MARK;
		// 屏幕尺寸
        String screenSize = LoggerUtil.IGNORE_MARK;
		// 网络环境
        String network = LoggerUtil.IGNORE_MARK;
		// 用户ID
        String userId = "";
		String systemId = "";

        if (request != null) {
            //clientIp = request.getParameter("clientIp");
        	clientIp = RequestUtil.getIp(request);
            deviceInfo = request.getParameter("deviceInfo");
            deviceId = request.getParameter("deviceId");
            clientVersion = request.getParameter("clientVersion");
            platform = request.getParameter("platform");
            screenSize = request.getParameter("screenSize");
            network = request.getParameter("network");
            userId = request.getParameter("userId");
			systemId = StringUtils.isBlank(request.getParameter("systemId")) ? DEFAULT_SYSTEM : request
					.getParameter("systemId");
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(StringUtils.isEmpty(clientIp) ? LoggerUtil.IGNORE_MARK
            : clientIp);
        buffer.append(LoggerUtil.SEPARATOR);
        buffer.append(StringUtils.isEmpty(result) ? LoggerUtil.IGNORE_MARK : result);
        buffer.append(LoggerUtil.SEPARATOR);
        buffer.append(code == 0 ? LoggerUtil.IGNORE_MARK : code);
        buffer.append(LoggerUtil.SEPARATOR);
        buffer.append(cost == 0 ? LoggerUtil.IGNORE_MARK : cost);
        buffer.append(LoggerUtil.SEPARATOR);
        
        JSONObject object = new JSONObject();
        object.put("deviceInfo", deviceInfo);
        object.put("deviceId", deviceId);
        object.put("clientVersion", clientVersion);
        object.put("platform", platform);
        object.put("screenSize", screenSize);
        object.put("network", network);
        object.put("userId", userId);
		object.put("systemId", systemId);
        object.put("params", params);
        buffer.append(object.toJSONString());
        return buffer.toString();
    }
    
    /*
	 * 获取运维监控验证码,如果为0返回本来的错误码
	 */
    private static int getSaCode(int code){
    	if(0 == code){
    		return code;
    	}
    	return RES_STATUS.findSaCodeByCode(code);
    }
    
    public static String info(HttpServletRequest request) {
		// 方法成功或失败
        String result = "Y";
		// 客户端IP
        String clientIp = request.getParameter("clientIp");
		// 机型
        String deviceInfo = request.getParameter("deviceInfo");
		// 设备ID
        String deviceId = request.getParameter("deviceId");
		// 客户端版本号
        String clientVersion = request.getParameter("clientVersion");
		// 操作系统类型
        String platform = request.getParameter("platform");
		// 屏幕尺寸
        String screenSize = request.getParameter("screenSize");
		// 网络环境
        String network = request.getParameter("network");
        String systemId = StringUtils.isBlank(request.getParameter("systemId")) ? DEFAULT_SYSTEM : request
				.getParameter("systemId");

        StringBuffer buffer = new StringBuffer();
        buffer.append(LoggerUtil.SEPARATOR);
        buffer.append(clientIp);
        buffer.append(LoggerUtil.SEPARATOR);
        buffer.append(result);
        buffer.append(LoggerUtil.SEPARATOR);
        buffer.append(deviceInfo);
        buffer.append(LoggerUtil.SEPARATOR);
        buffer.append(deviceId);
        buffer.append(LoggerUtil.SEPARATOR);
        buffer.append(clientVersion);
        buffer.append(LoggerUtil.SEPARATOR);
        buffer.append(platform);
        buffer.append(LoggerUtil.SEPARATOR);
        buffer.append(screenSize);
        buffer.append(LoggerUtil.SEPARATOR);
        buffer.append(network);
        buffer.append(LoggerUtil.SEPARATOR);
		buffer.append(systemId);
        return buffer.toString();
    }

}
