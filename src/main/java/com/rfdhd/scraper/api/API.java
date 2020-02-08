package com.rfdhd.scraper.api;

import com.google.gson.Gson;
import com.rfdhd.scraper.configuration.SpringConfiguration;
import com.rfdhd.scraper.model.FilePaths;
import com.rfdhd.scraper.model.ThreadInfo;
import com.rfdhd.scraper.services.GsonIO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spark.Route;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static java.time.temporal.ChronoUnit.HOURS;

public class API {

    private API() {
        throw new AssertionError("The API methods should be accessed statically");
    }

    private static Map<String, ThreadInfo> currentDeals;
    private static AtomicReference<LocalDateTime> lastFetched;
    private static boolean isFirstFetch;

    public static void initialize() {
        lastFetched = new AtomicReference<>(LocalDateTime.now());
        isFirstFetch = true;
    }

    public static Route getData() {
        return (req, res) -> {
            res.type("application/json");
            res.status(200);

            return new Gson().toJson(getLatest());
        };
    }

    private static Map<String, ThreadInfo> getLatest() {
        if ((Math.abs(ChronoUnit.MINUTES.between(lastFetched.get(), LocalDateTime.now())) > 20) || isFirstFetch) {
            isFirstFetch = false;
            lastFetched.set(LocalDateTime.now());

            GsonIO gsonIO = new GsonIO();
            ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
            FilePaths filePaths = context.getBean(FilePaths.class);
            final int HOURS_THRESHOLD = 24;

            Map<String, ThreadInfo> dailyDigestMap = gsonIO.read(filePaths.getDailyDigestJson());
            Map<String, ThreadInfo> archiveMap = gsonIO.read(filePaths.getArchiveJson());

            archiveMap.forEach((threadID, threadInfo) -> {
                if (Math.abs(HOURS.between(threadInfo.getLocalDateTime(), LocalDateTime.now())) < HOURS_THRESHOLD) {
                    dailyDigestMap.put(threadID, threadInfo);
                }
            });
            currentDeals = dailyDigestMap;
            return currentDeals;
        }
        return currentDeals;
    }
}
