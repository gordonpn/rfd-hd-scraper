package com.rfdhd.scraper.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection {
    private static final Logger LOGGER = Logger.getLogger(Connection.class.getName());

    private String url;

    public Connection() {
        this.url = "http://forums.redflagdeals.com/hot-deals-f9/";
        ;
    }

    public Elements connect(int page) {
        try {
            Optional<Document> doc;
            if (page != 0) {
                url = url.concat(String.valueOf(page));
            }
            doc = Optional.ofNullable(Jsoup.connect(url).get());

            if (doc.isPresent()) {
                return doc.get().getElementsByClass("row-item");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

        return null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
