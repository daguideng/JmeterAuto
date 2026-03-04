package com.atguigu.gmall.common.mail;

import org.springframework.stereotype.Service;


/**
 * 
 * @ClassName: MailServiceImpl
 * @Description: 邮件服务
 * @author shirui
 * @date 2018年5月30日 下午3:52:07
 *
 */
@Service
public class MailServiceImpl {   // implements MailService

	/***
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	JavaMailSender mailSender;
	
	

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	****/

	/**
	 * 发送邮件(支持多附件传输,以及富文本传输)
	 */
	/***
	@Override
	public void sendMail(MailObject obj) throws MessagingException {
		// MIME 表示多类型网络传输邮件,它支持附件,HTML等非纯文本邮件的传输
		MimeMessage msg = mailSender.createMimeMessage();

		// true表示采用了Multipart协议,utf-8表示文件内容的编码方式
		MimeMessageHelper msgHelper = new MimeMessageHelper(msg, true, "UTF-8");
		msgHelper.setSubject(obj.getMailTitle());
		// 设置发件人，发件人只有1个
		msgHelper.setFrom(obj.getFrom());	
		// 以;为分隔符，解析String为多个收件人存入到InternetAddress[] addresses数组
		String tos=obj.getToAddress();
		String[] toArray=tos.split(";");
		InternetAddress[] addresses=new InternetAddress[toArray.length];
		for(int i=0;i<toArray.length;i++){
		    InternetAddress address=new InternetAddress(toArray[i]);
		    addresses[i]=address;
		}
		msgHelper.setTo(addresses);
		
		// 循环添加邮件附件,带后缀名确保windows上邮箱可直接打开
		if (obj.getAttachmentsAbsolutePath() != null && obj.getAttachmentsAbsolutePath().size() > 0) {
			for (String path : obj.getAttachmentsAbsolutePath()) {
				File file = new File(path);
				// 根据path解析出文件名
				String filename = path.substring(path.lastIndexOf("/") + 1);
				msgHelper.addAttachment(filename, file);
			}
		}

		// 添加富文本内容,true表示为html而非txt
		msgHelper.setText(obj.getMailText(), true);
		
		// 添加富文本内嵌资源:addInline一定要在setText后面;MailHelper先解析html，根据cid占位，再通过addInLine替换富文本资源
		if (obj.getRichMailResouceMap() != null && obj.getRichMailResouceMap().size() > 0) {
			Set<String> keys = obj.getRichMailResouceMap().keySet();
			for (String key : keys) {
				File innerFile = new File(obj.getRichMailResouceMap().get(key));
				msgHelper.addInline(key, innerFile);
				
			}
		}
		mailSender.send(msg);
	}

	***/
}
