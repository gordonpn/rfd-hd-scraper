package com.rfdhd.scraper;

import ch.qos.logback.classic.Logger;
import com.rfdhd.scraper.configuration.SpringConfiguration;
import com.rfdhd.scraper.model.FilePaths;
import com.rfdhd.scraper.model.ThreadInfo;
import com.rfdhd.scraper.model.configuration.Configuration;
import com.rfdhd.scraper.report.DailyDigestEmailContent;
import com.rfdhd.scraper.services.*;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;
import java.util.*;

import static com.rfdhd.scraper.utility.MachineChecker.isProdMachine;

public class DigestCreator {

    private static Logger logger = (Logger) LoggerFactory.getLogger(DigestCreator.class);

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        Configuration configuration = context.getBean(Configuration.class);
        FilePaths filePaths = context.getBean(FilePaths.class);
        JavaMailSender mailSender = context.getBean(JavaMailSender.class);

        GsonIO gsonIO = new GsonIO();
        DigestPreparer digestPreparer = new DigestPreparer();

        Map dailyDigestMap = gsonIO.read(filePaths.getDailyDigestJson());

        dailyDigestMap = digestPreparer.removeOld(dailyDigestMap);
        dailyDigestMap = gsonIO.removeDuplicates(dailyDigestMap, filePaths.getArchiveJson());

        ArrayList sortedList = new ArrayList<>(dailyDigestMap.values());

        if (!sortedList.isEmpty()) {
            sortedList.sort((Comparator<ThreadInfo>) (thisThread, thatThread) -> (thisThread.getVotesInt() <= thatThread.getVotesInt()) ? 1 : -1);

            DailyDigestEmailContent emailContent = new DailyDigestEmailContent(sortedList);
            ContentBuilder contentBuilder = new ContentBuilder(emailContent);

            if (isProdMachine()) {

                MailClient mailClient = new MailClient(mailSender);
                Set<String> mailingList = NewsSignUp.read();
                String content = contentBuilder.getHtmlContent();

                mailingList.forEach(email -> mailClient.prepareAndSend(email, content));

            } else {
                try {
                    contentBuilder.write();
                } catch (IOException e) {
                    logger.error("Could not write email to file | {}", e.getMessage());
                }
            }
        } else {
            logger.info("dailyDigestJson was empty; no email sent.");
        }

        gsonIO.move(filePaths.getDailyDigestJson(), filePaths.getArchiveJson());
    }
}