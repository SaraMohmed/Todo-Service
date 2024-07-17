package com.example.userService.services;

import com.example.userService.dto.MailBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendSimpleMessage(MailBody mailBody){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBody.getTo());
        message.setSubject(mailBody.getSubject());
        message.setText(mailBody.getText());

        javaMailSender.send(message);
    }
}
