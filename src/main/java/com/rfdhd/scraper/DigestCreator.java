package com.rfdhd.scraper;

import com.rfdhd.scraper.configuration.SpringConfiguration;
import com.rfdhd.scraper.model.FilePaths;
import com.rfdhd.scraper.model.ThreadInfo;
import com.rfdhd.scraper.services.GsonIO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class DigestCreator {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        FilePaths filePaths = context.getBean(FilePaths.class);

        GsonIO gsonIO = new GsonIO();
        Map<String, ThreadInfo> dailyDigestMap;

        dailyDigestMap = gsonIO.read(filePaths.getDailyDigestJson(), new HashMap<>());

    }
}