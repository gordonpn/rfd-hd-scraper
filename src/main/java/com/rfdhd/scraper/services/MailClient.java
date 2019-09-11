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
    private final MailContentBuilder mailContentBuilder;

    @Autowired
    public MailClient(JavaMailSender javaMailSender, MailContentBuilder mailContentBuilder) {
        this.mailSender = javaMailSender;
        this.mailContentBuilder = mailContentBuilder;
    }

    public void prepareAndSend(String recipient, String content) {
        LocalDate dateToday = LocalDate.now();
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            String htmlContent = mailContentBuilder.build(content);
            mimeMessageHelper.setFrom(FROM_EMAIL);
            mimeMessageHelper.setTo(recipient);
            mimeMessageHelper.setSubject("Deals Daily Digest - " + dateToday);
            mimeMessageHelper.setText(htmlContent, true);
        };

        try {
            mailSender.send(mimeMessagePreparator);
        } catch (MailException e) {
            Logger.error("Error occurred while trying to send email | " + e.getMessage());
        }
    }
}
