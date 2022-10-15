package io.shuvalov.test.reminder.service;

import io.shuvalov.test.reminder.domain.Reminder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
import java.util.Properties;

@Service
@Slf4j
public class EmailNotificationService implements NotificationService {
	@Value("${reminder.notifications.email.smtp.host}")
	private String smtpHost;

	@Value("${reminder.notifications.email.smtp.port}")
	private Integer smtpPort;

	@Value("${reminder.notifications.email.smtp.login}")
	private String smtpLogin;

	@Value("${reminder.notifications.email.smtp.password}")
	private String smtpPassword;

	@Value("${reminder.notifications.email.smtp.from}")
	private String from;

	@Override
	public void notifyUser(Reminder reminder) {
		assert reminder.getOwner() != null;
		log.info("Sending notification to " + reminder.getOwner().getEmail());
		Properties prop = new Properties();
		prop.put("mail.smtp.host", smtpHost);
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.port", smtpPort.toString());
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
		Session session = Session.getInstance(prop, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(smtpLogin, smtpPassword);
			}
		});
		session.setDebug(true);
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(from));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(reminder.getOwner().getEmail()));
			msg.setSubject(reminder.getName());
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(reminder.getDescription(), "text/plain; charset=utf-8");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);
			msg.setContent(multipart);
			Transport.send(msg);
			log.info("Notification has been sent to " + reminder.getOwner().getEmail());
		} catch (MessagingException e) {
			throw new IllegalStateException("Unable to send message", e);
		}
	}
}
