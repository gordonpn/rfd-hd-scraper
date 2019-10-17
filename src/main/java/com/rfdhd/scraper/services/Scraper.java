package com.rfdhd.scraper.services;

import com.google.common.collect.Iterables;
import com.rfdhd.scraper.model.ThreadInfo;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Scraper {

    private int pages;
    private Map<String, ThreadInfo> threads;

    public Scraper(int pages) {
        this.pages = pages;
    }

    public Map<String, ThreadInfo> getThreadsMap() {

        if (threads == null) {
            threads = scrape();
        }

        Iterables.removeIf(threads.keySet(), Objects::isNull);
        Logger.info("Size of scrapings after getThreadsMap: " + threads.size());
        return threads;
    }

    private Map<String, ThreadInfo> scrape() {
        Map<String, ThreadInfo> threadsMap = new LinkedHashMap<>();
        Map<String, ThreadInfo> threadsMapPerPage;
        Elements threadsElements;

        for (int i = 0; i < pages; i++) {
            threadsElements = scrapePerPage(i);
            if (threadsElements != null) {
                threadsMapPerPage = readThreads(threadsElements, new LinkedHashMap<>());
                threadsMap.putAll(threadsMapPerPage);
            }
        }
        return threadsMap;
    }

    private Elements scrapePerPage(int page) {
        String url = "http://forums.redflagdeals.com/hot-deals-f9/";
        try {
            Optional<Document> doc;
            if (page != 0) {
                url = url.concat(String.valueOf(page + 1));
            }
            doc = Optional.ofNullable(Jsoup.connect(url).get());
            Logger.info("Scraping: " + url);
            if (doc.isPresent()) {
                return doc.get().getElementsByClass("topiclist topics with_categories");
            }
        } catch (IOException e) {
            Logger.error("Error with scraping page | " + e.getMessage());
        }
        return null;
    }

    private Map<String, ThreadInfo> readThreads(Elements threads, Map<String, ThreadInfo> threadsMap) {
        Elements list = threads.select("li");

        for (Element line : list) {
            threadsMap.putAll(parse(line, threadsMap));
        }

        return threadsMap;
    }

    private Map<String, ThreadInfo> parse(Element line, Map<String, ThreadInfo> threadsMap) {
        ThreadInfo threadInfo = new ThreadInfo();

        String id = line.attr("data-thread-id");
        if (!id.equals("")) {
            String topicTitle = "";
            String retailer = line.getElementsByClass("topictitle_retailer").text();
            String unformattedRetailer = "";
            String rawTopicTitle = line.getElementsByClass("topic_title_link").text();
            String prefix = "http://forums.redflagdeals.com";
            String link = line.getElementsByClass("topic_title_link").attr("href");

            if (StringUtils.isEmpty(retailer)) {
                unformattedRetailer = line.getElementsByClass("topictitle")
                        .text()
                        .replace("\"", "")
                        .replace("[", "")
                        .replace("]", "")
                        .trim();
                topicTitle = unformattedRetailer;
            }
            if (StringUtils.isNotEmpty(retailer) && !rawTopicTitle.toLowerCase().contains(retailer.toLowerCase())) {
                topicTitle = retailer + " " + rawTopicTitle;
            }

            threadInfo.setThreadID(id);
            threadInfo.setPosts(line.getElementsByClass("posts").text());
            threadInfo.setViews(line.getElementsByClass("views").text());
            threadInfo.setThreadCategory(line.getElementsByClass("thread_category").text());
            threadInfo.setTopicTitle(topicTitle);
            threadInfo.setVotes(line.getElementsByClass("total_count total_count_selector").text());
            threadInfo.setLink(prefix.concat(link));
            threadInfo.setDirectLink("");
            threadInfo.setContent("");
            threadInfo.setDate(line.select("span.first-post-time").text());
        }

        threadsMap.put(threadInfo.getThreadID(), threadInfo);

        return threadsMap;
    }
}
