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
    
    // Método centralizado para enviar qualquer e-mail simples
    private void sendEmail(String toEmail, String subject, String text) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        
        message.setFrom("paulogui702@gmail.com"); 
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);
        
        mailSender.send(message);
    }

    // Método público para redefinir a senha
    public void sendPasswordResetEmail(String toEmail, String token) throws MailException {
        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        String subject = "Redefinição de Senha";
        String text = "Para redefinir sua senha, clique no link a seguir: " + resetLink;
        
        sendEmail(toEmail, subject, text);
    }
    
    // Método público para enviar a notificação agendada
    public void sendNotificationEmail(String toEmail, String title, String content) throws MailException {
        sendEmail(toEmail, title, content);
    }
}