package com.rfdhd.scraper;

import com.rfdhd.scraper.services.Scraper;

import java.util.logging.Logger;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        Scraper scraper = new Scraper();

        // todo move this hardcoding into a config file to read from
        scraper.connect(1);
    }
}
