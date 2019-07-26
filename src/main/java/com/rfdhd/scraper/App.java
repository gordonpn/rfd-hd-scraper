package com.rfdhd.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("http://forums.redflagdeals.com/hot-deals-f9").get();
            System.out.printf("Title: %s\n", doc.title());

            Elements threads = doc.getElementsByClass("thread_info_title");

            for (Element thread : threads) {
                System.out.println(thread.getElementsByClass("topictitle"));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
