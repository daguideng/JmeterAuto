package com.atguigu.gmall.common.mail;


import org.springframework.context.annotation.Configuration;

/**
 * 
* @ClassName: MailConfig
* @Description: TODO(这里用一句话描述这个类的作用)
* @author shirui
* @date 2018年5月30日 下午3:43:55
*
 */
@Configuration
public class MailConfig {

	/**
	
	private static String MAIL_HOST="mail.qf.ucredit.com"; 
	private static String MAIL_USERNAME="qa-auto@qf.ucredit.com";
	private static String MAIL_PASSWORD="SEC00*urity";

	 **/
  
	/**
	 * 
	* @Title: getMailSender
	* @Description: Spring在SpringContextSupport包里提供了邮件发送的默认实现JavaMailSenderImpl,通过它来实现和邮件服务器的桥接
	* @param @return    设定文件
	* @return JavaMailSenderImpl    返回类型
	* @throws
	 */

	/**
	@Bean
	public JavaMailSender getMailSender(){
		JavaMailSenderImpl sender=new JavaMailSenderImpl();
		sender.setHost(MailConfig.MAIL_HOST);
		sender.setUsername(MailConfig.MAIL_USERNAME);
		sender.setPassword(MailConfig.MAIL_PASSWORD);
		return sender;
	}

	**/
	
}
