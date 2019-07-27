package com.rfdhd.scraper;

import com.rfdhd.scraper.services.Batch;
import com.rfdhd.scraper.services.Connection;
import org.jsoup.select.Elements;

import java.util.logging.Logger;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        Connection connection = new Connection();
        Batch batch = new Batch();

        Elements threads = connection.connect(0);
        batch.loopThreads(threads);
    }
}
