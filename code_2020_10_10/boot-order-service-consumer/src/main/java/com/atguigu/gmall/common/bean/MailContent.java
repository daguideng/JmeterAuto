package com.atguigu.gmall.common.bean;

/**
 * 角色
 */
public class MailContent {

    /**
     * 发件服务器
     */
    private String host;

    /**
     * 发件人
     */
    private String from;

    /**
     * 发件人密码
     */
    private String password;

    /**
     * 收件人列表
     */
    private String[] to;

    /**
     * 主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MailContent{" +
                "host='" + host + '\'' +
                ", from='" + from + '\'' +
                ", password='" + password + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}