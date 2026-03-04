package com.atguigu.gmall.common.utils.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailSender {

    private static Logger logger = LoggerFactory.getLogger(MailSender.class);



    /**
    public static void sendHTMLMail(MailContent mailContent) throws GeneralSecurityException, MessagingException {
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        senderImpl.setHost(mailContent.getHost());
        senderImpl.setUsername(mailContent.getFrom());
        senderImpl.setPassword(mailContent.getPassword());
        senderImpl.setDefaultEncoding("UTF-8");
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.timeout", "20000");
        senderImpl.setJavaMailProperties(prop);
        MimeMessage mail = senderImpl.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setFrom(mailContent.getFrom());
        helper.setTo(mailContent.getTo());
        helper.setSubject(mailContent.getSubject());
        StringBuilder html = new StringBuilder();
        html.append(mailContent.getContent());
        helper.setText(html.toString(), true);
        senderImpl.send(mail);
        logger.info("HTMLMAIL SENDED");
    }

    public static void main(String[] args) throws GeneralSecurityException, MessagingException {

        MailContent mailContent = new MailContent();

        String MAIL_HOST="mail.qf.ucredit.com";
        String MAIL_USERNAME="qa-auto@qf.ucredit.com";
        String MAIL_PASSWORD="SEC00*urity";


        String Q_HOST = "owa.ucredit.com";
        String Q_UNAME = "qa-auto@qf.ucredit.com";
        String Q_PSW = "SEC00*urity";
        String[] T_UNAME = new String[]{"wangzhen02@ucredit.com"};
        String Q_SUBJECT = "测试标题HTML";
        String M_CONTENT = "<html><body><h2>Goser,你好</h2><hr><p><h3>王学森</h3>发布最新&nbsp;V2.9.4&nbsp;-->iOS版本--><a style='color:red'>线上</a>&nbsp;包  </p><hr>请扫码安装<img src='https://test-wit.ucredit.com/images/qrcode/2018-01-29/919740f2-5ad2-435f-8fd9-5518da6489f2.png'/><hr><p>THANKS</p></body></html>";

        mailContent.setHost(MAIL_HOST);
        mailContent.setFrom(MAIL_USERNAME);
        mailContent.setTo(T_UNAME);
        mailContent.setPassword(MAIL_PASSWORD);
        mailContent.setSubject(Q_SUBJECT);
        mailContent.setContent(M_CONTENT);


        sendHTMLMail(mailContent);
    }

    **/

}
