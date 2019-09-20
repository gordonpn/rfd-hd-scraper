package com.rfdhd.scraper.services;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.rfdhd.scraper.model.ThreadInfo;
import com.rfdhd.scraper.utility.Calculate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
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
            Logger.info("Parsing thread ID: " + id);
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
            threadInfo.setDirectLink("");
            threadInfo.setContent("");
        }

        threadsMap.put(threadInfo.getThreadID(), threadInfo);

        return threadsMap;
    }

    public Map<String, ThreadInfo> filter(Map<String, ThreadInfo> threadsMap) {
        int votesThreshold = Calculate.POSTS.getMedian(threadsMap);

        Logger.info("Filtering threads");
        threadsMap.values().removeIf(threadInfo -> threadInfo.getVotesInt() < votesThreshold);

        return threadsMap;
    }

    public Map<String, ThreadInfo> filter(Map<String, ThreadInfo> threadsMap, String filePath) {
        GsonIO gsonIO = new GsonIO();

        Map<String, ThreadInfo> mapFromJson = gsonIO.read(filePath, new HashMap<String, ThreadInfo>());
        int votesThreshold = Calculate.POSTS.getMedian(mapFromJson);

        Logger.info("Filtering threads based on " + filePath);
        threadsMap.values().removeIf(threadInfo -> threadInfo.getVotesInt() < votesThreshold);

        return threadsMap;
    }

    public void loadThreads(Map<String, ThreadInfo> map) {
        map.forEach((id, thread) -> {
            Logger.info("Getting direct link for thread ID: " + thread.getThreadID());
            String url = thread.getLink();
            Optional<Document> threadPage = null;
            try {
                threadPage = Optional.ofNullable(Jsoup.connect(url).get());
            } catch (IOException e) {
                Logger.error("Could not connect to link | " + e.getMessage());
                threadPage = Optional.empty();
            }
            threadPage.ifPresent(page -> {
                getDirectLinks(page, thread);
                getContent(page, thread);
            });
        });
    }

    public void getDirectLinks(Document page, ThreadInfo thread) {
        Elements dealLinkElement = page.getElementsByClass("deal_link");
        Elements aLine = dealLinkElement.select("a");
        String affiliateUrl = aLine.attr("href");
        Logger.info("Getting affiliate link: " + affiliateUrl);
        String expandedUrl = expandUrl(affiliateUrl);
        thread.setDirectLink(expandedUrl);
    }

    public String expandUrl(String affiliateUrl) {
        String expandedUrl = "";
        try {
            URL url = new URL(affiliateUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
            httpURLConnection.setInstanceFollowRedirects(false);
            expandedUrl = httpURLConnection.getHeaderField("Location");
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            Logger.error("Could not get affiliate URL | " + e.getMessage());
        } catch (IOException e) {
            Logger.error("Could not connect to affiliate URL | " + e.getMessage());
        }
        Logger.info("Got direct link: " + expandedUrl);
        return expandedUrl;
    }

    public void getContent(Document page, ThreadInfo thread) {
        Logger.info("Getting content of thread " + thread.getThreadID());
        Elements posts = page.select("div.content");
        if (posts.size() != 0) {
            Element firstPost = posts.get(0);
            String content = firstPost.text();
            String patternRegex = "(?i)<br */?>";
            content = content.replaceAll(patternRegex, " ").replaceAll("\"", "");
            if (content.length() > 100) {
                content = content.substring(0, 100);
            }
            content = content.concat("...");
            thread.setContent(content);
        }
    }
}
