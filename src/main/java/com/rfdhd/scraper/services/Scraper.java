package com.rfdhd.scraper.services;

import com.rfdhd.scraper.model.ThreadInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.util.*;

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
        threads.remove(null);
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

    private int calculateMedianVotes(Map<String, ThreadInfo> threadMap) {
        List<Integer> votes = new ArrayList<>();
        threadMap.values().forEach(threadInfo -> votes.add(threadInfo.getVotesInt()));
        votes.sort(Integer::compareTo);
//        System.out.println(Arrays.toString(votes.toArray()));
        return votes.get(votes.size() / 2);
    }

}
