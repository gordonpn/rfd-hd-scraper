package com.rfdhd.scraper.services;

import com.rfdhd.scraper.model.ThreadInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Scraper {

    private static final Logger LOGGER = Logger.getLogger(Scraper.class.getName());

    private String url;

    public Scraper() {
        this.url = "http://forums.redflagdeals.com/hot-deals-f9/";
    }

    public void connect(int pages){
        Elements threads;
        for (int i = 0; i < pages; i++) {
            threads = this.scrapePage(i);
            if (threads != null) {
                this.readThreads(threads);
            }
        }
    }

    private Map<String, ThreadInfo> readThreads(Elements threads) {
        Map<String, ThreadInfo> threadMap = new HashMap<>();

        Elements list = threads.select("li");
        for (Element line : list) {
            parse(line, threadMap);
        }
        System.out.println(calculateAvgPosts(threadMap));
        System.out.println(calculateAvgViews(threadMap));
        return threadMap;
    }

    private Elements scrapePage(int page) {
        try {
            Optional<Document> doc;
            if (page != 0 && page != 1) {
                url = url.concat(String.valueOf(page));
            }
            doc = Optional.ofNullable(Jsoup.connect(url).get());

            if (doc.isPresent()) {
                return doc.get().getElementsByClass("topiclist topics with_categories");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }

        return null;
    }

    private void parse(Element line, Map<String, ThreadInfo> threadMap) {
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
        filter(threadInfo, threadMap);
    }

    private void filter(ThreadInfo threadInfo, Map<String, ThreadInfo> threadMap) {
        // todo filter uninteresting posts
        threadMap.put(threadInfo.getThreadID(), threadInfo);

    }

    private int calculateAvgViews(Map<String, ThreadInfo> threadMap) {
        int sum = threadMap.values().stream().mapToInt(ThreadInfo::getViewsInt).sum();
        return sum / threadMap.size();
    }

    private int calculateAvgPosts(Map<String, ThreadInfo> threadMap) {
        int sum = threadMap.values().stream().mapToInt(ThreadInfo::getPostsInt).sum();
        return sum / threadMap.size();
    }

    private int calculateMedianVotes() {
        // todo calculate
        return 0;
    }

}
