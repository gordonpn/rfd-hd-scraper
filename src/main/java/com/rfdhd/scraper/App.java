package com.rfdhd.scraper;

import com.rfdhd.scraper.configuration.SpringConfiguration;
import com.rfdhd.scraper.model.FilePaths;
import com.rfdhd.scraper.model.ThreadInfo;
import com.rfdhd.scraper.model.configuration.Configuration;
import com.rfdhd.scraper.services.GsonIO;
import com.rfdhd.scraper.services.Scraper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

public class App {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        Configuration configuration = context.getBean(Configuration.class);
        FilePaths filePaths = context.getBean(FilePaths.class);

        Scraper scraper = new Scraper(configuration.getPages());
        GsonIO gsonIO = new GsonIO();

        Map<String, ThreadInfo> rawThreadsMap;
        Map<String, ThreadInfo> filteredThreads;

        rawThreadsMap = scraper.getThreadsMap();
        gsonIO.write(filePaths.getScrapingsJson(), rawThreadsMap);

        filteredThreads = scraper.filter(rawThreadsMap);
        scraper.getDirectLinks(filteredThreads);
        gsonIO.write(filePaths.getCurrentLinks(), filteredThreads);
    }
}
