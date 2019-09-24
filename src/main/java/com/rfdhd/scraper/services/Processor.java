package com.rfdhd.scraper.services;

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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class Processor {

    public Map filter(Map<String, ThreadInfo> map) {
        if (map == null || map.isEmpty()) {
            return new LinkedHashMap();
        }

        int votesThreshold = Calculate.POSTS.getMedian(map);
        Map filteredMap = new LinkedHashMap();

        Logger.info("Filtering threads");
        Logger.info("Votes threshold: " + votesThreshold);

        map.forEach((threadID, threadInfo) -> {
            if (threadInfo.getVotesInt() > votesThreshold) {
                filteredMap.put(threadID, threadInfo);
            }
        });

        return filteredMap;
    }

    public void loadThreads(Map<String, ThreadInfo> map) {
        map.forEach((id, thread) -> {
            Logger.info("Getting direct link for thread ID: " + thread.getThreadID());
            String url = thread.getLink();
            Optional<Document> threadPage;
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

    private void getDirectLinks(Document page, ThreadInfo thread) {
        Elements dealLinkElement = page.getElementsByClass("deal_link");
        Elements aLine = dealLinkElement.select("a");
        String affiliateUrl = aLine.attr("href");
        Logger.info("Getting affiliate link: " + affiliateUrl);
        String expandedUrl = expandUrl(affiliateUrl);
        thread.setDirectLink(expandedUrl);
    }

    private String expandUrl(String affiliateUrl) {
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

    private void getContent(Document page, ThreadInfo thread) {
        Logger.info("Getting content of thread " + thread.getThreadID());
        Elements posts = page.select("div.content");
        if (!posts.isEmpty()) {
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
