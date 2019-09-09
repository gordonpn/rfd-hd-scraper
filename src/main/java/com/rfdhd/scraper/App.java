package com.rfdhd.scraper;

import com.rfdhd.scraper.configuration.SpringConfiguration;
import com.rfdhd.scraper.model.ThreadInfo;
import com.rfdhd.scraper.model.configuration.Configuration;
import com.rfdhd.scraper.services.GsonIO;
import com.rfdhd.scraper.services.Scraper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

public class App {

    // move this to configuration as well maybe
    // in prod change RESOURCES for "./"
    private final static String RESOURCES = "target/resources/";
    private final static String SCRAPINGS_JSON = RESOURCES + "scrapings.json";
    private final static String CURRENT_LINKS = RESOURCES + "currentLinks.json";
    private final static String PAST_LINKS = RESOURCES + "pastLinks.json";

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        Configuration configuration = context.getBean(Configuration.class);

        Scraper scraper = new Scraper(configuration.getPages());
        GsonIO gsonIO = new GsonIO();

        Map<String, ThreadInfo> rawThreadsMap;
        Map<String, ThreadInfo> filteredThreads;

        rawThreadsMap = scraper.getThreadsMap();
        gsonIO.write(SCRAPINGS_JSON, rawThreadsMap);

        filteredThreads = scraper.filter(rawThreadsMap);
        scraper.getDirectLinks(filteredThreads);
        gsonIO.write(CURRENT_LINKS, filteredThreads);
    }
}
