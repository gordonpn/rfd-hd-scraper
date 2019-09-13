package com.rfdhd.scraper;

import com.rfdhd.scraper.configuration.SpringConfiguration;
import com.rfdhd.scraper.model.FilePaths;
import com.rfdhd.scraper.model.ThreadInfo;
import com.rfdhd.scraper.model.configuration.Configuration;
import com.rfdhd.scraper.report.DailyDigestEmailContent;
import com.rfdhd.scraper.services.ContentBuilder;
import com.rfdhd.scraper.services.GsonIO;
import com.rfdhd.scraper.services.MailClient;
import org.pmw.tinylog.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DigestCreator {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        Configuration configuration = context.getBean(Configuration.class);
        FilePaths filePaths = context.getBean(FilePaths.class);
        JavaMailSender mailSender = context.getBean(JavaMailSender.class);

        GsonIO gsonIO = new GsonIO();
        Map<String, ThreadInfo> dailyDigestMap;

        dailyDigestMap = gsonIO.read(filePaths.getDailyDigestJson(), new HashMap<>());
        gsonIO.move(filePaths.getDailyDigestJson(), filePaths.getArchiveJson());

        if (dailyDigestMap != null) {
            if (dailyDigestMap.size() > 0) {
                MailClient mailClient = new MailClient(mailSender);
                DailyDigestEmailContent emailContent = new DailyDigestEmailContent(filePaths, dailyDigestMap);
                ContentBuilder contentBuilder = new ContentBuilder(emailContent);

                List<String> mailingList = configuration.getMailingList();
                String content = contentBuilder.getHtmlContent();

                mailClient.prepareAndSend(mailingList, content);
            }
        } else {
            Logger.info("dailyDigestJson was empty; no email sent.");
        }

    }
}