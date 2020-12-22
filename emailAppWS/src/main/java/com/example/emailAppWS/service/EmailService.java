package com.example.emailAppWS.service;

import java.util.Properties;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.emailAppWS.model.EmailData;

@Service
public class EmailService {
	@SuppressWarnings("finally")
	public String sendEmail(EmailData emailData)
	{
	      // Recipient's email ID needs to be mentioned.
	      Address[] recepients = new Address[1];
	      String response = "Failure";

	      try {
	    	  recepients[0] = new InternetAddress(emailData.getRecepient());
		   
	      } catch (AddressException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
	      }

	      // Sender's email ID needs to be mentioned
	      String from = "EmailApp";

	         
	      final String username = "testemail.emailapp@gmail.com";
	      final String password = "EmailApp@123";
			

	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = "smtp.gmail.com";

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
	      props.put("mail.smtp.port", "587");

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
	            }
	         });

	      try {
	         // Create a default MimeMessage object.
	         Message message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
//	         message.setRecipients(Message.RecipientType.TO,
//	            InternetAddress.parse(to));
//	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); 
	         message.setRecipients(Message.RecipientType.TO, recepients);


	         // Set Subject: header field
	         message.setSubject(emailData.getSubject());

	         // Create the message part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Now set the actual message
	         //messageBodyPart.setText(emailData.getMessage());
	         messageBodyPart.setContent(emailData.getMessage(), "text/html");
	         // Create a multipar message
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Send the complete message parts
	         message.setContent(multipart);

	         // Send message
	         Transport.send(message);

	         System.out.println("Sent message successfully....");
	         response = "Success";
	  
	      } catch (MessagingException e) {
	    	  response = "Failure";
				e.printStackTrace();
//	         throw new RuntimeException(e);
	      } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = "Failure";
	      }
	      finally {
	    	  return response;
	      }
	   }
}
 
