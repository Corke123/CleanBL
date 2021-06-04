package org.unibl.etf.ps.cleanbl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private static final String NO_REPLY_ADDRESS = "clean.bl.service@gmail.com";

    private final JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Async
    @Override
    public void sendMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NO_REPLY_ADDRESS);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            emailSender.send(message);
            log.info("Activation mail sent");
        } catch (MailException exception) {
            log.warn("Unable to send an email to: " + to);
        }
    }
}
