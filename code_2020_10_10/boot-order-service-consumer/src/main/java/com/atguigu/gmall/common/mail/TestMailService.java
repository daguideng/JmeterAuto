package com.atguigu.gmall.common.mail;

public class TestMailService {

	/***
	private static String MAIL_HOST = "mail.qf.ucredit.com";
	private static String MAIL_USERNAME = "qa-auto@qf.ucredit.com";
	private static String MAIL_PASSWORD = "SEC00*urity";

	public static void main(String args[]) throws MessagingException {

		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(MAIL_HOST);
		sender.setUsername(MAIL_USERNAME);
		sender.setPassword(MAIL_PASSWORD);

		MailServiceImpl mailService = new MailServiceImpl();
		mailService.setMailSender(sender);

		// 创建邮件对象
		MailObject mailObj = new MailObject();
		// 邮件接收双方以及title
		mailObj.setFrom("shirui@ucredit.com");
	    // ;传入多个收件人
	    mailObj.setToAddress("shirui@ucredit.com;zhangxiaodan@ucredit.com");
		mailObj.setMailTitle("Ucredit API 测试报告");
		// 邮件附件列表
		ArrayList<String> attachList = new ArrayList<String>();
		attachList.add("E:/apitest/apache-jmeter-4.0/report/htmlReport/report1.html");
		mailObj.setAttachmentsAbsolutePath(attachList);
		
		// 设置邮件富文本html内容(占位符标志图片)
		String richText = "<TABLE Align='center'  width=600px  >\n" + "<tr> <td>" + "<h1></h1></td></tr>" + "    <tr>"
				+ "        <td Align='center'>\n"
				+ "            <img src='cid:passRate'' width=800 height=400  Align='center' alt=''>\n"
				+ "        </td>\n" + "    </tr>\n" + "</table>\n";
		mailObj.setMailText(richText);
		
		// 生成饼图，并返回饼图的绝对路径
		Map<String, String> richTextResource = new HashMap<String, String>();
		richTextResource.put("passRate", "E:/apitest/apache-jmeter-4.0/report/htmlReport/pieChart.jpg");
		mailObj.setRichMailResouceMap(richTextResource);
		mailService.sendMail(mailObj);
	}

	 ***/

}
