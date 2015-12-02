package lu.uni.fstc.proactivity.rules.activeDir.suggestion;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
			message.setFrom(new InternetAddress("ProActiveDirectory"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.to));
			message.setSubject(this.subject);
			message.setContent(this.body+"<br> <br>--- This is an automatic email, please <b>do not reply</b> --- ","text/html");
			
			Transport transport = session.getTransport("smtp");
			transport.connect("smtp.uni.lu", "", "");
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			
			System.out.println("Mail send to "+this.to);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
