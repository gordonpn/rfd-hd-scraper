package com.rfdhd.scraper;

import com.rfdhd.scraper.model.ThreadInfo;
import com.rfdhd.scraper.services.GsonIO;
import com.rfdhd.scraper.services.Scraper;

import java.util.Map;

public class App {

    // move this to configuration as well maybe
    private final static String RESOURCES = "target/resources/";
    private final static String SCRAPINGS_JSON = RESOURCES + "scrapings.json";
    private final static String CURRENT_LINKS = RESOURCES + "currentLinks.json";
    private final static String PAST_LINKS = RESOURCES + "pastLinks.json";

    public static void main(String[] args) {
        // todo move this hardcoded parameter into a config file to read from
        Scraper scraper = new Scraper(1);
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
