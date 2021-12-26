package com.adt.ibp.Utils;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.*;

public class EmailManager {
	final static Logger logger = Logger.getLogger(EmailManager.class);
	public List<String> attachmentFiles = new ArrayList<>();
	
	public static void main(String[] args) {
		BasicConfigurator.configure();
		Calendar calendar = Calendar.getInstance();
		String months = String.valueOf(calendar.get(Calendar.MONTH));
		String dates = String.valueOf(calendar.get(Calendar.DATE));
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String fullDate = ""+year+"_"+months+"_"+dates;
		//EmailManager sender = new EmailManager();
		//sender.toAddress = "musabaytechcorp@gmail.com";
		//List<String> screenshots = new ArrayList<>();
		//sender.sendEmail(screenshots);

		EmailManager sender = new EmailManager();
//		sender.toAddress = "musabaytechcorp@gmail.com;training@musabaytechnologies.com";
		sender.toAddress = "qa.group.notes@gmail.com;";
		sender.ccAddress = "qa.group.notes@gmail.com;";

		List<String> screenshots = new ArrayList<>();
//		screenshots.add("target/logs/log4j-selenium.log");
//		screenshots.add("target/logs/Selenium-Report.html");
//		screenshots.add("target/screenshots/buy_TheAgingBrainCoursTest20190824100902222.png");
//		screenshots.add("target/screenshots/buy_TheAgingBrainCoursTest20190824101303778.png");
		screenshots.add("ADT_API_RestAssure/src/test/resources/reports/"+fullDate+".html");

		sender.sendEmail(screenshots);
	}
	
	public String toAddress = "";
	public String ccAddress = "";
	public String bccAddress = "";




	public void sendEmail(List<String> attachments){
		String emailBody = "Test email by JavaMail API example."
	+ "<br><br> Regards, <br>Test Automation Team<br>";

		sendEmail("smtp.gmail.com", "587", "mirzayev.mirali19@gmail.com", "rrpbskukayzcksxk",
				"Please find Privacy Monitor Test Report below!",	emailBody, attachments);
	}

	public void sendEmail(String host, String port, final String emailUserID, final String emailUserPassword,
			String subject, String emailBody, List<String> attachments) {
		BasicConfigurator.configure();
		try {
			// sets SMTP server properties
			Properties prop = new Properties();
			prop.put("mail.smtp.host", host);
			prop.put("mail.smtp.port", port);
			prop.put("mail.smtp.auth", "true");
			prop.put("mail.smtp.starttls.enable", "true");
			prop.put("mail.user", emailUserID);
			prop.put("mail.password", emailUserPassword);
			logger.info("Step1> preparing email configuration...");

			// creates a new session with an authenticator
			Authenticator auth = new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(emailUserID, emailUserPassword);
				}
			};

			Session session = Session.getInstance(prop, auth);
			// Creates a new e-mail message
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(emailUserID));

			msg.addRecipients(Message.RecipientType.TO, setMultipleEmails(toAddress));
			if (!ccAddress.isEmpty() && !ccAddress.equals(null)) {
				msg.addRecipients(Message.RecipientType.CC, setMultipleEmails(ccAddress));
			}
			if (!bccAddress.isEmpty() && !bccAddress.equals(null)) {
				msg.addRecipients(Message.RecipientType.BCC, setMultipleEmails(bccAddress));
			}

			msg.setSubject(subject);
			msg.setSentDate(new Date());

			// Creates message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(emailBody, "text/html");
			// Creates multi-part
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			// Adds Attachments
			if (attachments.size() > 0) {
				for (String singleAttachment : attachments) {
					MimeBodyPart attachPart = new MimeBodyPart();
					try {
						attachPart.attachFile(singleAttachment);
					} catch (Exception e) {
						logger.error("Attaching files to email failed ...");
						logger.error(e.getMessage());
					}
					multipart.addBodyPart(attachPart);
				}
			}
			logger.info("Step2> Attaching report files & error screenshots ...");

			msg.setContent(multipart);
			// sends email
			logger.info("Step3> Sending email in progress...");
			Transport.send(msg);
			logger.info("Step4> Sending email complete...");
		} catch (Exception e) {
			logger.error("Sending email failed...");
			logger.error("Error: ", e);
		}

	}

	private Address[] setMultipleEmails(String emailAddress) {
		String multipleEmails[] = emailAddress.split(";");
		InternetAddress[] addresses = new InternetAddress[multipleEmails.length];
		try {
			for (int i = 0; i < multipleEmails.length; i++) {
				addresses[i] = new InternetAddress(multipleEmails[i]);
			}
		} catch (Exception e) {
			logger.error("Adding multiple email addreses error!", e);
		}
		return addresses;
	}

}
