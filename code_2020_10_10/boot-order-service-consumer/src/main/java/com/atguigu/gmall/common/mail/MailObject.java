package com.atguigu.gmall.common.mail;

import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: MailObject
 * @Description: 邮件对象
 * @date 2018年5月30日 下午3:49:23
 *
 */
public class MailObject {
	private String from;
	//可支持多个收件人，以;为分割符
	private String toAddress;
	private String mailTitle;
	// 简单文本邮件内容
	private String simpleMailText;
	// 富文本邮件内容(可以是text,也可以是html)
	private String mailText;	
	// 富文本邮件内嵌内容地址:key是richMailText的占位符,value是资源地址 
	private Map<String,String> richMailResouceMap;
	// 富文本邮件附件列表
	private List<String> attachmentsAbsolutePath;
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
    
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getMailTitle() {
		return mailTitle;
	}
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}
	public String getSimpleMailText() {
		return simpleMailText;
	}
	public void setSimpleMailText(String simpleMailText) {
		this.simpleMailText = simpleMailText;
	}
	
	
	public String getMailText() {
		return mailText;
	}
	public void setMailText(String mailText) {
		this.mailText = mailText;
	}
	public Map<String, String> getRichMailResouceMap() {
		return richMailResouceMap;
	}
	public void setRichMailResouceMap(Map<String, String> richMailResouceMap) {
		this.richMailResouceMap = richMailResouceMap;
	}
	public List<String> getAttachmentsAbsolutePath() {
		return attachmentsAbsolutePath;
	}
	public void setAttachmentsAbsolutePath(List<String> attachmentsAbsolutePath) {
		this.attachmentsAbsolutePath = attachmentsAbsolutePath;
	}
	@Override
	public String toString() {
		return "MailObject [from=" + from + ", toAddress=" + toAddress + ", mailTitle=" + mailTitle
				+ ", simpleMailText=" + simpleMailText + ", richMailText=" + mailText + ", richMailResouceMap="
				+ richMailResouceMap + ", attachmentsAbsolutePath=" + attachmentsAbsolutePath + "]";
	}

    

}
