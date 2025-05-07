package com.ajeet.docManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void send(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            message.setFrom("ajimpdoc2016@gmail.com"); // use SMTP sender

            mailSender.send(message);
            System.out.println("Email sent to " + to);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
