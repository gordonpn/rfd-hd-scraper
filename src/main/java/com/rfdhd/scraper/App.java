package com.rfdhd.scraper;

import com.rfdhd.scraper.model.ThreadInfo;
import com.rfdhd.scraper.services.GsonIO;
import com.rfdhd.scraper.services.Scraper;

import java.util.Map;

public class App {

    private final static String SCRAPINGS_JSON = "target/resources/scrapings.json";
    private final static String CURRENT_LINKS = "target/resources/currentLinks.json";
    private final static String PAST_LINKS = "target/resources/pastLinks.json";

    public static void main(String[] args) {
        // todo move this hardcoded parameter into a config file to read from
        Scraper scraper = new Scraper(10);
        GsonIO gsonIO = new GsonIO();

        Map<String, ThreadInfo> rawThreadsMap;
        Map<String, ThreadInfo> filteredThreads;

        rawThreadsMap = scraper.getThreadsMap();
        gsonIO.write(SCRAPINGS_JSON, rawThreadsMap);

        filteredThreads = scraper.filter(rawThreadsMap);
        gsonIO.write(CURRENT_LINKS, filteredThreads);
    }
}
