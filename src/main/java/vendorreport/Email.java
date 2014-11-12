package vendorreport;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.ideas.utility.UtilFunctions;

public class Email {

	private Properties smtpHostProperties;

	public Email(Properties smtpHostProperties) {
		this.smtpHostProperties = smtpHostProperties;
	}

	public void sendEmail(String subject, String body,
			String toEmailAddress) throws MessagingException {

		Session session = Session.getInstance(smtpHostProperties);
		MimeMessage message = new MimeMessage(session);
		ArrayList<String> filePath = new ArrayList<String>();
		
		addFilePath(filePath);

		try {
			InternetAddress mailFromAddress = new InternetAddress();
			String fromInMail = "IDeaSTranportService@IDeaS.com";
			mailFromAddress.setAddress(fromInMail);

			String bToEmailAddress = toEmailAddress.lastIndexOf(";") == toEmailAddress
					.length() - 1 ? toEmailAddress.substring(0,
					toEmailAddress.length() - 1) : toEmailAddress;
			message.setFrom(mailFromAddress); 
			message.addRecipients(Message.RecipientType.TO,
					bToEmailAddress.replaceAll(";", ","));

			message.setSubject(subject, "utf-8");

			message.setReplyTo(message.getFrom()); 

			Multipart mp = new MimeMultipart();
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setText(body, "utf-8");
			mp.addBodyPart(mimeBodyPart);
			addAttachment(mp, filePath);
			message.setContent(mp);
			Transport.send(message);
		} catch (MessagingException ex) {
			throw new MessagingException("User.ForgotPassword.Email.Failed");
		} catch (Exception ae) {
			throw new MessagingException("User.ForgotPassword.Email.Failed");
		}
	}

	
	

	private void addFilePath(ArrayList<String> filePath) {
		filePath.add("C:/" + new UtilFunctions().generateFileName() + ".xlsx");
		filePath.add("C:/" + new UtilFunctions().generateFileName() + ".pdf");
	}

	private void addAttachment(Multipart multipart, ArrayList<String> filePath)
			throws MessagingException {
		for (int i = 0; i < filePath.size(); i++) {
			BodyPart messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(filePath.get(i));
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(new File(filePath.get(i)).getName());
			multipart.addBodyPart(messageBodyPart);
		}
	}
}