package com.rfdhd.scraper.services;

import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MailClient {

    private JavaMailSender mailSender;
    private final String FROM_EMAIL = "gordon.pn6@gmail.com";

    @Autowired
    public MailClient(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }

    public void prepareAndSend(String recipient, String content) {
        LocalDate dateToday = LocalDate.now();
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(FROM_EMAIL);
            mimeMessageHelper.setTo(recipient);
            mimeMessageHelper.setSubject("Deals Daily Digest - " + dateToday);
            mimeMessageHelper.setText(content, true);
        };

        try {
            mailSender.send(mimeMessagePreparator);
        } catch (MailException e) {
            Logger.error("Error occurred while trying to send email | " + e.getMessage());
        }
    }
}
