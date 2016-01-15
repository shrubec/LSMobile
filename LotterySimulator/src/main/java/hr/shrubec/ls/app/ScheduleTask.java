package hr.shrubec.ls.app;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTask {

	@Autowired 
	private InfoCollector infoCollector;
	
	final String username = "lotosimulator@gmail.com ";
	final String password = "simulator0810";
	
	@Scheduled(cron="0 0 20 1/1 * ?")
    public void trigger() {
		StringBuilder sb = new StringBuilder();
		for (String s:infoCollector.getInfo()) {
			sb.append(s);
			sb.append(System.getProperty("line.separator"));
		}
		sendMail(sb.toString());
		infoCollector.reset();
		System.out.println("Mail poslan!");
    }
    
	
	private boolean sendMail(String poruka) {

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("shrubec@gmail.com"));
			message.setSubject("Lottery Simulator Info");
			message.setText(poruka);
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
    
}
