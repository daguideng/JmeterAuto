package com.atguigu.gmall.common.utils;

import com.atguigu.gmall.service.impl.EmailConfigServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.*;
import java.io.*;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

@Component
public class SendEmailUtil {

    @Autowired
    private EmailConfigServer emailConfigServer;

    // 邮件配置相关字段
    private String PROTOCOL = null;
    private String HOST = null;
    private String PORT = null;
    private String IS_AUTH = null;
    private String from = null;
    private String to = null;
    private String cc = null;
    private String bcc = null;
    private String password = null; // 存储授权码
    private Boolean sendReport = true; // 是否发送报告邮件，默认true
    private Properties props = null;

    /**
     * 处理邮箱地址字符串，支持多种分隔符
     * @param addresses 邮箱地址字符串
     * @return 用逗号分隔的标准邮箱地址
     */
    private String normalizeEmailAddresses(String addresses) {
        if (addresses == null || addresses.trim().isEmpty()) {
            return "";
        }
        
        // 替换所有可能的分隔符为逗号
        String normalized = addresses.trim()
            .replace(';', ',')    // 英文分号
            .replace('；', ',')   // 中文分号
            .replace('，', ',')   // 中文逗号
            .replace(' ', ',')    // 空格
            .replace('\n', ',')   // 换行
            .replace('\r', ',')   // 回车
            .replace('|', ',');   // 竖线
        
        // 处理多个连续分隔符
        normalized = normalized.replaceAll(",{2,}", ",");
        
        // 去除开头和结尾的逗号
        if (normalized.startsWith(",")) {
            normalized = normalized.substring(1);
        }
        if (normalized.endsWith(",")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        
        return normalized;
    }

    /**
     * 初始化邮件配置
     */
    public void initSendEmailUtil() {
        try {
            Map<String, Object> emailconfig = emailConfigServer.getByPrimaryKeyServer(1);

            // 调试：打印所有从数据库获取的字段
            System.out.println("=== 数据库配置信息 ===");
            for (Map.Entry<String, Object> entry : emailconfig.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
            System.out.println("===================");

            // 读取基本配置
            PROTOCOL = (String) emailconfig.get("mail_transport_protocol");
            HOST = (String) emailconfig.get("mail_smtp_host");
            PORT = (String) emailconfig.get("mail_smtp_port");
            IS_AUTH = (String) emailconfig.get("mail_smtp_auth");
            String mail_debug = (String) emailconfig.get("mail_debug");

            // 读取邮箱地址
            from = (String) emailconfig.get("mail_from");
            to = (String) emailconfig.get("mail_to");
            cc = (String) emailconfig.get("mail_cc");
            bcc = (String) emailconfig.get("mail_bcc");
            
            // 读取是否发送报告配置
            Object sendReportObj = emailconfig.get("send_report");
            if (sendReportObj != null) {
                sendReport = "1".equals(sendReportObj.toString()) || Boolean.parseBoolean(sendReportObj.toString());
            } else {
                sendReport = true; // 默认发送
            }

            // ⭐⭐⭐ 关键：读取密码/授权码 ⭐⭐⭐
            password = (String) emailconfig.get("password");
            if (password == null || password.isEmpty()) {
                password = "svsacwzyffipcaef"; // 使用硬编码的授权码
                System.out.println("警告：从数据库未读取到密码，使用默认授权码");
            }

            // ⭐⭐⭐ 预处理邮箱地址，统一分隔符 ⭐⭐⭐
            to = normalizeEmailAddresses(to);
            cc = normalizeEmailAddresses(cc);
            bcc = normalizeEmailAddresses(bcc);

            // 创建Properties配置
            props = new Properties();
            props.setProperty("mail.transport.protocol", PROTOCOL != null ? PROTOCOL : "smtp");
            props.setProperty("mail.smtp.host", HOST != null ? HOST : "smtp.qq.com");
            props.setProperty("mail.smtp.port", PORT != null ? PORT : "465");
            props.setProperty("mail.smtp.auth", IS_AUTH != null ? IS_AUTH : "true");
            props.setProperty("mail.debug", mail_debug != null ? mail_debug : "false");

            // ⭐⭐⭐ QQ邮箱必需的SSL配置 ⭐⭐⭐
            props.setProperty("mail.smtp.ssl.enable", "true");
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.port", PORT != null ? PORT : "465");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");

            // 设置连接超时
            props.setProperty("mail.smtp.connectiontimeout", "10000"); // 10秒
            props.setProperty("mail.smtp.timeout", "10000"); // 10秒

            // 打印最终配置
            System.out.println("=== 最终邮件配置 ===");
            System.out.println("发件人: " + from);
            System.out.println("收件人: " + to);
            System.out.println("抄送: " + cc);
            System.out.println("密送: " + bcc);
            System.out.println("主机: " + props.getProperty("mail.smtp.host"));
            System.out.println("端口: " + props.getProperty("mail.smtp.port"));
            System.out.println("是否认证: " + props.getProperty("mail.smtp.auth"));
            System.out.println("密码长度: " + (password != null ? password.length() : 0));
            System.out.println("===================");

        } catch (Exception e) {
            System.err.println("初始化邮件配置失败: " + e.getMessage());
            e.printStackTrace();
            
            // 如果数据库读取失败，使用默认配置
            setupDefaultConfig();
        }
    }

    /**
     * 默认配置（数据库读取失败时使用）
     */
    private void setupDefaultConfig() {
        System.out.println("使用默认QQ邮箱配置");
        
        from = "294332968@qq.com";
        to = "294332968@qq.com";
        cc = "294332968@qq.com";
        bcc = "294332968@qq.com";
        password = "svsacwzyffipcaef";
        
        // 预处理邮箱地址
        to = normalizeEmailAddresses(to);
        cc = normalizeEmailAddresses(cc);
        bcc = normalizeEmailAddresses(bcc);
        
        props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", "smtp.qq.com");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.debug", "true");
        props.setProperty("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
    }



public boolean  sendEmailReportOk(){


    Map<String, Object> emailconfig = emailConfigServer.getByPrimaryKeyServer(1);

    boolean sendSuccess = emailconfig.get("send_report") != null && (boolean) emailconfig.get("send_report");

      return sendSuccess;
        


}



    /**
     * 发送HTML邮件（带主题）
     */
    public boolean sendHtmlEmail(String sendContent, String subject) {

        

        this.initSendEmailUtil();

        

        try {
            System.out.println("开始发送HTML邮件...");
            System.out.println("主题: " + subject);
            System.out.println("内容长度: " + (sendContent != null ? sendContent.length() : 0));

            // 创建Session实例对象（使用认证）
            Session session = Session.getInstance(props, new MyAuthenticator());

            // 创建MimeMessage实例对象
            MimeMessage message = new MimeMessage(session);
            message.setSubject(subject);
            message.setSentDate(new Date());
            
            // ⭐⭐⭐ 设置收件人 ⭐⭐⭐
            if (to != null && !to.trim().isEmpty()) {
                System.out.println("设置收件人: " + to);
                message.setRecipients(RecipientType.TO, InternetAddress.parse(to.trim(), true));
            } else {
                System.out.println("警告：收件人地址为空");
            }
            
            // ⭐⭐⭐ 设置抄送 ⭐⭐⭐
            if (cc != null && !cc.trim().isEmpty()) {
                System.out.println("设置抄送: " + cc);
                message.setRecipients(RecipientType.CC, InternetAddress.parse(cc.trim(), true));
            }
            
            // ⭐⭐⭐ 设置密送 ⭐⭐⭐
            if (bcc != null && !bcc.trim().isEmpty()) {
                System.out.println("设置密送: " + bcc);
                message.setRecipients(RecipientType.BCC, InternetAddress.parse(bcc.trim(), true));
            }

            // 设置html内容为邮件正文
            message.setContent(sendContent, "text/html;charset=utf-8");

            // 设置自定义发件人昵称
            String nick = "Jmeter测试平台";
            try {
                nick = MimeUtility.encodeText(nick);
            } catch (UnsupportedEncodingException e) {
                System.err.println("编码发件人昵称失败: " + e.getMessage());
            }
            message.setFrom(new InternetAddress(nick + " <" + from + ">"));
            
            // 保存并生成最终的邮件内容
            message.saveChanges();

            // 发送邮件
            Transport.send(message);
            System.out.println("HTML邮件发送成功！");
            return true;
        } catch (Exception e) {
            System.err.println("发送HTML邮件失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 发送HTML邮件（不带主题，使用默认主题）
     */
    public boolean sendHtmlEmail(String sendContent) {
        this.initSendEmailUtil();

        // 检查是否允许发送报告邮件
        if (!sendReport) {
            System.out.println("邮件发送被禁用（send_report = false），跳过发送");
            return true; // 返回true表示"成功"，但实际不发送
        }

        return sendHtmlEmail(sendContent, "测试结果");
    }

    /**
     * 发送简单的文本邮件
     */
    public boolean sendTextEmail(String toAddress, int code) {
        this.initSendEmailUtil();

        // 检查是否允许发送报告邮件
        if (!sendReport) {
            System.out.println("邮件发送被禁用（send_report = false），跳过发送");
            return true; // 返回true表示"成功"，但实际不发送
        }

        try {
            // 创建Session实例对象（使用认证）
            Session session = Session.getInstance(props, new MyAuthenticator());

            // 创建MimeMessage实例对象
            MimeMessage message = new MimeMessage(session);
            // 设置发件人
            message.setFrom(new InternetAddress(from));
            // 设置邮件主题
            message.setSubject("内燃机注册验证码");
            // 设置收件人
            message.setRecipient(RecipientType.TO, new InternetAddress(toAddress));
            // 设置发送时间
            message.setSentDate(new Date());
            // 设置纯文本内容为邮件正文
            message.setText("您的验证码是：" + code + "!验证码有效期是10分钟，过期后请重新获取！"
                    + "中国内燃机学会");
            // 保存并生成最终的邮件内容
            message.saveChanges();

            // 发送邮件
            Transport.send(message);
            System.out.println("文本邮件发送成功！");
            return true;
        } catch (Exception e) {
            System.err.println("发送文本邮件失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 发送带内嵌图片的HTML邮件
     */
    public void sendHtmlWithInnerImageEmail(String imagePath) throws MessagingException {
        this.initSendEmailUtil();
        
        // 创建Session实例对象（使用认证）
        Session session = Session.getInstance(props, new MyAuthenticator());

        // 创建邮件内容
        MimeMessage message = new MimeMessage(session);
        message.setSubject("带内嵌图片的HTML邮件", "utf-8");
        message.setFrom(new InternetAddress(from));
        message.setSentDate(new Date());
        
        // 设置收件人
        if (to != null && !to.trim().isEmpty()) {
            message.setRecipients(RecipientType.TO, InternetAddress.parse(to.trim(), true));
        }
        
        // 设置抄送
        if (cc != null && !cc.trim().isEmpty()) {
            message.setRecipients(RecipientType.CC, InternetAddress.parse(cc.trim(), true));
        }
        
        // 设置密送
        if (bcc != null && !bcc.trim().isEmpty()) {
            message.setRecipients(RecipientType.BCC, InternetAddress.parse(bcc.trim(), true));
        }

        // 创建一个MIME子类型为"related"的MimeMultipart对象
        MimeMultipart mp = new MimeMultipart("related");
        MimeBodyPart htmlPart = new MimeBodyPart();
        mp.addBodyPart(htmlPart);
        MimeBodyPart imagePart = new MimeBodyPart();
        mp.addBodyPart(imagePart);

        message.setContent(mp);

        // 设置内嵌图片邮件体
        DataSource ds = new FileDataSource(new File(imagePath));
        DataHandler dh = new DataHandler(ds);
        imagePart.setDataHandler(dh);
        imagePart.setContentID("embeddedImage");

        // 创建一个MIME子类型为"alternative"的MimeMultipart对象
        MimeMultipart htmlMultipart = new MimeMultipart("alternative");
        MimeBodyPart htmlBodypart = new MimeBodyPart();
        htmlBodypart.setContent("<span style='color:red;'>这是带内嵌图片的HTML邮件哦！！！<img src=\"cid:embeddedImage\" /></span>", 
                               "text/html;charset=utf-8");
        htmlMultipart.addBodyPart(htmlBodypart);
        htmlPart.setContent(htmlMultipart);

        message.saveChanges();

        // 发送邮件
        Transport.send(message);
        System.out.println("带图片的HTML邮件发送成功！");
    }

    /**
     * 发送带附件和HTML内容的邮件
     */
    public boolean sendEmailWithAttachment(String subject, String content, String[] attachmentPaths) {
        this.initSendEmailUtil();

        // 检查是否允许发送报告邮件
        if (!sendReport) {
            System.out.println("邮件发送被禁用（send_report = false），跳过发送");
            return true; // 返回true表示"成功"，但实际不发送
        }

        try {
            // 创建Session实例对象（使用认证）
            Session session = Session.getInstance(props, new MyAuthenticator());

            // 创建MimeMessage实例对象
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setSubject(subject);
            message.setSentDate(new Date());
            
            // 设置收件人
            if (to != null && !to.trim().isEmpty()) {
                message.setRecipients(RecipientType.TO, InternetAddress.parse(to.trim(), true));
            }

            // 创建Multipart对象
            Multipart multipart = new MimeMultipart();

            // 添加HTML内容部分
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(content, "text/html;charset=utf-8");
            multipart.addBodyPart(messageBodyPart);

            // 添加附件
            for (String filePath : attachmentPaths) {
                if (filePath != null && !filePath.trim().isEmpty()) {
                    File file = new File(filePath);
                    if (file.exists()) {
                        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                        DataSource source = new FileDataSource(file);
                        attachmentBodyPart.setDataHandler(new DataHandler(source));
                        attachmentBodyPart.setFileName(MimeUtility.encodeText(file.getName()));
                        multipart.addBodyPart(attachmentBodyPart);
                    }
                }
            }

            message.setContent(multipart);

            // 发送邮件
            Transport.send(message);
            System.out.println("带附件的邮件发送成功！");
            return true;
        } catch (Exception e) {
            System.err.println("发送带附件邮件失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 认证器内部类
     */
    class MyAuthenticator extends Authenticator {
        
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            System.out.println("正在进行邮件服务器认证...");
            System.out.println("认证用户名: " + from);
            System.out.println("认证密码长度: " + (password != null ? password.length() : 0));
            
            if (from == null || from.isEmpty()) {
                from = "294332968@qq.com";
            }
            if (password == null || password.isEmpty()) {
                password = "svsacwzyffipcaef";
            }
            
            return new PasswordAuthentication(from, password);
        }
    }

    /**
     * 获取邮箱地址数组（用于外部调用）
     */
    public String[] getToAddresses() {
        if (to == null || to.trim().isEmpty()) {
            return new String[0];
        }
        return to.split(",");
    }
    
    public String[] getCcAddresses() {
        if (cc == null || cc.trim().isEmpty()) {
            return new String[0];
        }
        return cc.split(",");
    }
    
    public String[] getBccAddresses() {
        if (bcc == null || bcc.trim().isEmpty()) {
            return new String[0];
        }
        return bcc.split(",");
    }
    
    public String getFrom() {
        return from;
    }
    
    public String getTo() {
        return to;
    }
    
    public String getHost() {
        return HOST;
    }
    
    public String getPort() {
        return PORT;
    }
}