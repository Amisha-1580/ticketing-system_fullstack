package com.example.ticketing.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    public EmailService(JavaMailSender mailSender){ this.mailSender = mailSender; }

    public void sendSimple(String to, String subject, String text){
        try{
            SimpleMailMessage m = new SimpleMailMessage();
            m.setTo(to);
            m.setSubject(subject);
            m.setText(text);
            mailSender.send(m);
        }catch(Exception ex){
            // in dev env, mail server may not be configured - log to console
            System.out.println("[EmailService] failed to send: " + ex.getMessage());
            System.out.println("To: " + to + " Subject: " + subject + " Text: " + text);
        }
    }
}
