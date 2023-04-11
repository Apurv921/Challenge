package com.dws.challenge.service;

import java.math.BigDecimal;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Component;

import com.dws.challenge.domain.Account;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmailNotificationService implements NotificationService {


	@Override
	public void notifyAboutTransfer(Account account, String transferDescription) {
		// THIS METHOD SHOULD NOT BE CHANGED - ASSUME YOUR COLLEAGUE WILL IMPLEMENT IT
		Session session = null;
		String to = account.getEmailId(); // to address. 
		final String from = "vikasv16.mishra@gmail.com"; // from address. As this is using Gmail SMTP.
		final String password = "jzcgnkskfinbnnru"; // password for from mail address.

		Properties prop = new Properties();
		prop.setProperty("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "465");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		session = Session.getInstance(prop, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});
		//session.setDebug(true);

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject("Fund Transfer done sucessfully");
			
			String msg= "Account id "+account.getAccountId() + " Amount has been credited "+ "Updated Balance "+account.getBalance();

			 

			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(msg, "text/html");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);

			message.setContent(multipart);
			
			Transport transport = session.getTransport();
			transport.connect();
			Transport.send(message);
			transport.close();

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	//This method is for notifing the sender about the transaction and updated balance will be reflected on the mail
	@Override
	public void notifydebitTransfer(Account fromAccount, String debited, BigDecimal Amount) {

		Session session = null;
		String to = fromAccount.getEmailId(); // to address. 
		final String from = "vikasv16.mishra@gmail.com"; // from address. As this is using Gmail SMTP.
		final String password = "jzcgnkskfinbnnru"; // password for from mail address.

		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "465");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		session = Session.getInstance(prop, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});
		session.setDebug(true);

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject("Amount debited");
			
			String debitMsg = "Account id "+ fromAccount.getAccountId() + debited+Amount+"Updated Balance " + fromAccount.getBalance();

			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(debitMsg, "text/html");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);

			message.setContent(multipart);
			
			Transport transport = session.getTransport();
			transport.connect();
			Transport.send(message);
			transport.close();

		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}

}
