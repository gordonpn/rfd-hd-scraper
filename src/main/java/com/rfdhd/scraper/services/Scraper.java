package com.rfdhd.scraper.services;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.rfdhd.scraper.model.ThreadInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Scraper {

    int pages;

    Map<String, ThreadInfo> threads;

    public Scraper(int pages) {
        this.pages = pages;
    }

    public Map<String, ThreadInfo> getThreadsMap() {

        if (threads == null) {
            threads = scrape();
        }

        Iterables.removeIf(threads.keySet(), Predicates.isNull());
        return threads;
    }

    private Map<String, ThreadInfo> scrape() {
        Map<String, ThreadInfo> threadsMap = new HashMap<>();
        Map<String, ThreadInfo> threadsMapPerPage = new HashMap<>();
        Elements threads;

        for (int i = 0; i < pages; i++) {
            threads = scrapePerPage(i);
            if (threads != null) {
                threadsMapPerPage = readThreads(threads, new HashMap<>());
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
            Logger.error(e.getMessage());
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
            threadInfo.setThreadID(id);
            threadInfo.setPosts(line.getElementsByClass("posts").text());
            threadInfo.setViews(line.getElementsByClass("views").text());
            threadInfo.setThreadCategory(line.getElementsByClass("thread_category").text());
            threadInfo.setTopicTitle(line.getElementsByClass("topic_title_link").text());
            String votes = line.getElementsByClass("total_count total_count_selector").text();
            if (!votes.equals("")) {
                threadInfo.setVotes(votes);
            } else {
                threadInfo.setVotes("0");
            }
            String prefix = "http://forums.redflagdeals.com";
            String link = line.getElementsByClass("topic_title_link").attr("href");
            threadInfo.setLink(prefix.concat(link));
        }

        threadsMap.put(threadInfo.getThreadID(), threadInfo);

        return threadsMap;
    }

    public Map<String, ThreadInfo> filter(Map<String, ThreadInfo> threadsMap) {
        return threadsMap;
    }
}
