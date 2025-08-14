package com.engeman.notify_server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendPasswordResetEmail(String toEmail, String token) throws MailException {
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setFrom("paulogui702@gmail.com"); 
        message.setTo(toEmail);
        message.setSubject("Redefinição de Senha");
        
        // URL frontend
        // http://...
        
        message.setText("Para redefinir sua senha, clique no link a seguir: " + token);
        
        mailSender.send(message);
	}

}
