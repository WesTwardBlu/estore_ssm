package com.blue.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class MailUtils {

	/*public static void sendMail(String email, String emailMsg)
			throws AddressException, MessagingException {
		// 1.创建一个程序与邮件服务器会话对象 Session

		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "SMTP");
		props.setProperty("mail.host", "smtp.sohu.com");
		props.setProperty("mail.smtp.auth", "true");// 指定验证为true
		props.setProperty("mail.debug", "true");// 指定验证为true
//		props.setProperty("mail.smtp.starttls.enable", "true");// 指定验证为true
		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.port", "465");
//		props.setProperty("mail.smtp.auth.mechanisms", "NTLM");// 指定验证为true
		

		// 创建验证器
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("westwardyao@sohu.com", "sohu_5222105605");
			}
		};

		Session session = Session.getInstance(props, auth);

		// 2.创建一个Message，它相当于是邮件内容
		Message message = new MimeMessage(session);

		message.setFrom(new InternetAddress("westwardyao@sohu.com")); // 设置发送者

		message.setRecipient(RecipientType.TO, new InternetAddress(email)); // 设置发送方式与接收者

		message.setSubject("用户激活");
		// message.setText("这是一封激活邮件，请<a href='#'>点击</a>");

		message.setContent(emailMsg, "text/html;charset=utf-8");

		// 3.创建 Transport用于将邮件发送

		Transport.send(message);
		
		
	}*/
	
	
	public static void sendMail(String email, String emailMsg)
			throws AddressException, MessagingException {
		// 1.创建一个程序与邮件服务器会话对象 Session

		Properties props = new Properties();
//		props.setProperty("mail.transport.protocol", "SMTP");
		props.put("mail.transport.protocol", "SMTP");
//		props.setProperty("mail.host", "smtp.sohu.com");
		props.put("mail.host", "smtp.163.com");
//		props.setProperty("mail.smtp.auth", "true");// 指定验证为true
		props.put("mail.smtp.auth", "true");
//		props.setProperty("mail.debug", "true");// 指定验证为true
		props.put("mail.debug", "true");
//		props.setProperty("mail.smtp.starttls.enable", "true");// 指定验证为true
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.ssl.trust", "smtp.sohu.com");
//		props.put("mail.smtp.port", "465");
//		props.setProperty("mail.smtp.auth.mechanisms", "NTLM");// 指定验证为true
		


		Session session = Session.getDefaultInstance(props);

		MimeMessage message= new MimeMessage(session);
		message.setFrom(new InternetAddress("18516885191@163.com"));
		message.setRecipient(Message.RecipientType.TO,new InternetAddress(email));
		message.setSentDate(new Date());
		message.setSubject("账号激活");
//		message.setText(emailMsg);
		message.setContent(emailMsg, "text/html;charset=utf-8");
		message.saveChanges();
		Transport transport= session.getTransport("smtp");
		transport.connect("18516885191@163.com", "alwaysblue1");//使用的是授权码，用于第三方客户端登录163服务器
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}
}
