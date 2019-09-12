package com.rfdhd.scraper;

import com.rfdhd.scraper.configuration.SpringConfiguration;
import com.rfdhd.scraper.model.FilePaths;
import com.rfdhd.scraper.model.configuration.Configuration;
import com.rfdhd.scraper.report.DailyDigestEmailContent;
import com.rfdhd.scraper.services.Scraper;
import org.pmw.tinylog.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;

public class Testing {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        Configuration configuration = context.getBean(Configuration.class);
        FilePaths filePaths = context.getBean(FilePaths.class);
        Scraper scraper = context.getBean(Scraper.class);
        JavaMailSender mailSender = context.getBean(JavaMailSender.class);

        DailyDigestEmailContent dailyDigestEmail = new DailyDigestEmailContent(filePaths);
        Logger.info(dailyDigestEmail.getTemplate());
    }
}
