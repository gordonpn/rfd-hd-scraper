package com.rfdhd.scraper.services;

import ch.qos.logback.classic.Logger;
import com.rfdhd.scraper.model.ThreadInfo;
import com.rfdhd.scraper.utility.Calculate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class Processor {

    private static Logger logger = (Logger) LoggerFactory.getLogger(Scraper.class);

    public Map filter(Map<String, ThreadInfo> map) {
        if (map == null || map.isEmpty()) {
            return new LinkedHashMap();
        }

        int votesThreshold = Calculate.POSTS.getMedian(map);
        Map filteredMap = new LinkedHashMap();

        logger.info("Filtering threads");
        logger.info("Votes threshold: {}", votesThreshold);

        map.forEach((threadID, threadInfo) -> {
            if (threadInfo.getVotesInt() > votesThreshold) {
                filteredMap.put(threadID, threadInfo);
            }
        });

        logger.info("Size of scrapings after filtering: {}", filteredMap.size());
        return filteredMap;
    }

    public void loadThreads(Map<String, ThreadInfo> map) {
        map.forEach((id, thread) -> {
            logger.info("Getting direct link for thread ID: {}", thread.getThreadID());
            String url = thread.getLink();
            Optional<Document> threadPage;
            try {
                threadPage = Optional.ofNullable(Jsoup.connect(url).get());
            } catch (IOException e) {
                logger.error("Could not connect to link | {}", e.getMessage());
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
        logger.info("Getting affiliate link: {}", affiliateUrl);
        String expandedUrl = expandUrl(affiliateUrl);
        thread.setDirectLink(expandedUrl);
    }

    private void getContent(Document page, ThreadInfo thread) {
        final int MAX_CONTENT_LENGTH = 140;
        logger.info("Getting content of thread {}", thread.getThreadID());
        Elements posts = page.select("div.content");
        if (!posts.isEmpty()) {
            Element firstPost = posts.get(0);
            String content = firstPost.text();
            String patternRegex = "(?i)<br */?>";
            content = content
                    .replaceAll(patternRegex, " ")
                    .replace("\"", "");
            if (content.length() > MAX_CONTENT_LENGTH) {
                content = content.substring(0, MAX_CONTENT_LENGTH);
            }
            content = content.concat("...");
            thread.setContent(content);
        }
    }

    private String expandUrl(String affiliateUrl) {
        String expandedUrl = "";
        try {
            URL url = new URL(affiliateUrl);
            boolean redirect = false;

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
            httpURLConnection.addRequestProperty("User-Agent", "Mozilla");
            httpURLConnection.addRequestProperty("Referer", "google.com");
            httpURLConnection.setInstanceFollowRedirects(false);

            int status = httpURLConnection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK && (status == HttpURLConnection.HTTP_MOVED_TEMP
                    || status == HttpURLConnection.HTTP_MOVED_PERM
                    || status == HttpURLConnection.HTTP_SEE_OTHER)) {
                redirect = true;
            }

            if (redirect) {
                expandedUrl = httpURLConnection.getHeaderField("Location");
                logger.info("Got direct link: {}", expandedUrl);
            }

            httpURLConnection.disconnect();

        } catch (MalformedURLException e) {
            logger.error("Could not get affiliate URL | {}", e.getMessage());
        } catch (IOException e) {
            logger.error("Could not connect to affiliate URL | {}", e.getMessage());
        }

        return expandedUrl;
    }
}
