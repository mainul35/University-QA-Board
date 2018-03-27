package com.springprojects.config;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer {

    public static void sendMail(String to, String subject, String content) {
        java.util.Properties props = new java.util.Properties();
        props.put("mail.smtp.host", Properties.SMTP_HOST);
        props.put("mail.smtp.socketFactory.port", Properties.SMTP_PORT);
        props.put("mail.smtp.socketFactory.class", Properties.SMTP_CLASS);
        props.put("mail.smtp.auth", Properties.SMTP_AUTH);
        props.put("mail.smtp.port", Properties.SMTP_PORT);

        Session session = Session.getDefaultInstance(props, new Authenticator() {
		
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("teamg5.bit@gmail.com", "bit12345");

            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("Mainul"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
            System.out.println("Message sent successfully!");
        } catch (AuthenticationFailedException e) {
        	Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, e.getMessage());
        } catch (MessagingException ex) {
            Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
