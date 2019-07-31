package com.rfdhd.scraper.services;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Batch {

    public Batch() {
        Connection connection = new Connection();
    }

    public void readThreads(Elements threads) {
        for (Element thread : threads) {
            System.out.println(thread.getElementsByClass("topictitle"));
        }
    }
}
