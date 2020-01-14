package com.rfdhd.scraper;

import com.google.gson.Gson;
import com.rfdhd.scraper.configuration.SpringConfiguration;
import com.rfdhd.scraper.model.FilePaths;
import com.rfdhd.scraper.services.GsonIO;
import com.rfdhd.scraper.services.NewsSignUp;
import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;
import com.rfdhd.scraper.model.ThreadInfo;
import io.javalin.plugin.json.JavalinJson;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static java.time.temporal.ChronoUnit.HOURS;

public class Start {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        int port = Integer.parseInt(dotenv.get("PORT", "7000"));

        AtomicReference<LocalDateTime> lastFetched = new AtomicReference<>(LocalDateTime.now());
        Gson gson = new Gson();
        AtomicReference<Map<String, ThreadInfo>> currentDeals = new AtomicReference<>(getLatest());
        JavalinJson.setToJsonMapper(gson::toJson);

        Javalin app = Javalin.create(javalinConfig -> javalinConfig.addStaticFiles("/public")).start(port);

        app.before("/top24h", ctx -> {
            if (Math.abs(ChronoUnit.MINUTES.between(lastFetched.get(), LocalDateTime.now())) > 20) {
                currentDeals.set(getLatest());
                lastFetched.set(LocalDateTime.now());
            }
        });
        app.get("/top24h", ctx -> ctx.json(currentDeals).status(200));
    }

    private static Map<String, ThreadInfo> getLatest() {
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
        return dailyDigestMap;
    }
}
