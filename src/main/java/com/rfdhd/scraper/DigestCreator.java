package com.rfdhd.scraper;

import com.rfdhd.scraper.configuration.SpringConfiguration;
import com.rfdhd.scraper.model.FilePaths;
import com.rfdhd.scraper.model.ThreadInfo;
import com.rfdhd.scraper.model.configuration.Configuration;
import com.rfdhd.scraper.report.DailyDigestEmailContent;
import com.rfdhd.scraper.services.ContentBuilder;
import com.rfdhd.scraper.services.DigestPreparer;
import com.rfdhd.scraper.services.GsonIO;
import com.rfdhd.scraper.services.MailClient;
import org.pmw.tinylog.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.rfdhd.scraper.utility.MachineChecker.isProdMachine;

public class DigestCreator {

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
                List<String> mailingList = configuration.getMailingList();
                String content = contentBuilder.getHtmlContent();

                mailClient.prepareAndSend(mailingList, content);

            } else {
                try {
                    contentBuilder.write();
                } catch (IOException e) {
                    Logger.error("Could not write email to file | " + e.getMessage());
                }
            }
        } else {
            Logger.info("dailyDigestJson was empty; no email sent.");
        }

        gsonIO.move(filePaths.getDailyDigestJson(), filePaths.getArchiveJson());
    }
}