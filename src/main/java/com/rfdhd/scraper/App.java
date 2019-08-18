package com.rfdhd.scraper;

import com.rfdhd.scraper.model.ThreadInfo;
import com.rfdhd.scraper.services.Scraper;

import java.util.Map;

public class App {

    public static void main(String[] args) {
        // todo move this hardcoded parameter into a config file to read from
        Scraper scraper = new Scraper(10);
        Map<String, ThreadInfo> rawThreadsMap;
        Map<String, ThreadInfo> filteredThreads;

        rawThreadsMap = scraper.getThreadsMap();
        filteredThreads = scraper.filter(rawThreadsMap);
    }
}
