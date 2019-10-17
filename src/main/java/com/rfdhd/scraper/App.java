package com.rfdhd.scraper;

import com.rfdhd.scraper.configuration.SpringConfiguration;
import com.rfdhd.scraper.model.FilePaths;
import com.rfdhd.scraper.model.ThreadInfo;
import com.rfdhd.scraper.services.GsonIO;
import com.rfdhd.scraper.services.Processor;
import com.rfdhd.scraper.services.Scraper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

public class App {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        FilePaths filePaths = context.getBean(FilePaths.class);
        Scraper scraper = context.getBean(Scraper.class);

        GsonIO gsonIO = new GsonIO();
        Processor process = new Processor();

        Map scrapings = gsonIO.read(filePaths.getScrapingsJson());
        Map dailyDigest = gsonIO.read(filePaths.getDailyDigestJson());

        Map<String, ThreadInfo> newScrapings = scraper.getThreadsMap();

        Map newDailyDigest = process.filter(newScrapings);
        newDailyDigest = gsonIO.removeDuplicates(newDailyDigest, filePaths.getArchiveJson());

        process.loadThreads(newDailyDigest);

        scrapings.putAll(newScrapings);
        dailyDigest.putAll(newDailyDigest);

        gsonIO.write(filePaths.getScrapingsJson(), scrapings);
        gsonIO.write(filePaths.getDailyDigestJson(), dailyDigest);
    }
}
