package com.rfdhd.scraper.services;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MailClient {

    private static Logger logger = (Logger) LoggerFactory.getLogger(MailClient.class);
    private static final String FROM_EMAIL = "deals@gordon-pn.com";
    private JavaMailSender mailSender;

    @Autowired
    public MailClient(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }

    public void prepareAndSend(String recipient, String content) {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(FROM_EMAIL);
            mimeMessageHelper.setTo(recipient);
            mimeMessageHelper.setSubject("Deals Daily Digest - " + LocalDate.now());
            mimeMessageHelper.setText(content, true);
        };

        try {
            mailSender.send(mimeMessagePreparator);
        } catch (MailException e) {
            logger.error("Error occurred while trying to send email | {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
