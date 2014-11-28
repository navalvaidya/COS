package vendorreport;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.TimerTask;


public class EmailDispatcher extends TimerTask {

	public void run() {

		String[] toEmail = {"ameet.shelar@ideas.com", "mallika.james@ideas.com"}; 
		String subject = "Ideas Cab Schedule";
		String body = "Ideas Cab Schedule";

		try {
			System.out.println("Creating report and sending...");
			HttpURLConnection con = (HttpURLConnection) new URL("http://localhost:8080/COS/routeOptimize").openConnection();
			con.setRequestMethod("GET");
			con.connect();
			con.getResponseCode();
			Properties smtpHostProperties = new Properties();
			String smtpHost = "mailhost.fyi.sas.com";
			smtpHostProperties.put("mail.smtp.host", smtpHost);
			Email email = new Email(smtpHostProperties);
			email.sendEmail(subject, body, toEmail);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new EmailDispatcher().run();
	}
	
}
