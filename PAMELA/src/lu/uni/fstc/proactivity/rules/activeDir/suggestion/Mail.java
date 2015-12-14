package lu.uni.fstc.proactivity.rules.activeDir.suggestion;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class Mail {
	
	private String to;
	private String subject;
	private String body;
	
	public Mail(String to,String subject,String body){
		this.to = to;
		this.subject = subject;
		this.body = body;
	}
	
	public void send(){
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.uni.lu");
		props.put("mail.smtp.port", "25");

		
		Session session = Session.getInstance(props, null);		
		
		try {
			Message message = new MimeMessage(session);
			try {
				message.setFrom(new InternetAddress("noreply@uni.lu","ProActiveDirectory"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.to));
			message.setSubject(this.subject);
			message.setContent("<font size =\"2\" face=\"arial\" >"+this.body+"<br> <br>--- This is an automatic email, please <b>do not reply</b> --- </font>","text/html");
			
			Transport transport = session.getTransport("smtp");
			transport.connect("smtp.uni.lu", "", "");
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void send(String manager, String location) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.uni.lu");
		props.put("mail.smtp.port", "25");

		
		Session session = Session.getInstance(props, null);		
		
		try {
			Message message = new MimeMessage(session);
			try {
				message.setFrom(new InternetAddress("noreply@uni.lu","ProActiveDirectory"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.to));
			if(!manager.equals(""))
				message.addRecipient(RecipientType.CC, new InternetAddress(manager));
			message.addRecipient(RecipientType.CC, new InternetAddress(location));
			message.setSubject(this.subject);
			message.setContent("<font size =\"2\" face=\"arial\" >"+this.body+"<br> <br>--- This is an automatic email, please <b>do not reply</b> --- </font>","text/html");
			
			Transport transport = session.getTransport("smtp");
			transport.connect("smtp.uni.lu", "", "");
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
