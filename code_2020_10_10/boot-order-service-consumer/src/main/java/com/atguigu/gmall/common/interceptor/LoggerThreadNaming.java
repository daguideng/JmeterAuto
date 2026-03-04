package com.atguigu.gmall.common.interceptor;


import com.atguigu.gmall.common.log.LoggerUtil;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class LoggerThreadNaming implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6045324739862681554L;

	private static final String Default_UUID = "Default-userId";
	
	private final String threadName;
	private long current;
	private String ip;
	private String userId;

	private ThreadLocalRandom random = ThreadLocalRandom.current();
	
	public LoggerThreadNaming(String threadName, String ip, String userId) {
		this.threadName = threadName;
		this.current = System.currentTimeMillis();
		this.ip = ip;
		this.userId = userId;
	}

	public LoggerThreadNaming() {
		this.threadName = "Default-named-thread-" + random.nextInt();
		this.ip = LoggerUtil.Ignore;
		this.userId = Default_UUID + random.nextInt(10000);
		this.current = System.currentTimeMillis();
	}

	public String getThreadName() {
		return threadName;
	}


	public long getCurrent() {
		return current;
	}

	public String getIp() {
		return ip;
	}

	public String getUserId() {
		return userId;
	}

	public void setCurrent(long current) {
		this.current = current;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
