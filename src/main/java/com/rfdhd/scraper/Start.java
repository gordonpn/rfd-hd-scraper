package com.rfdhd.scraper;

import com.google.gson.Gson;
import com.rfdhd.scraper.configuration.SpringConfiguration;
import com.rfdhd.scraper.model.FilePaths;
import com.rfdhd.scraper.services.GsonIO;
import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJson;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Start {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        FilePaths filePaths = context.getBean(FilePaths.class);
        Dotenv dotenv = Dotenv.load();
        int port = Integer.parseInt(dotenv.get("PORT", "7000"));

        AtomicReference<LocalDateTime> lastFetched = new AtomicReference<>(LocalDateTime.now());
        GsonIO gsonIO = new GsonIO();
        Gson gson = new Gson();
        AtomicReference<Map> dailyDigestMap = new AtomicReference<>(gsonIO.read(filePaths.getDailyDigestJson()));
        JavalinJson.setToJsonMapper(gson::toJson);

        Javalin app = Javalin.create(javalinConfig -> javalinConfig.addStaticFiles("/public")).start(port);

        app.before("/top24h", ctx -> {
            if (Math.abs(ChronoUnit.MINUTES.between(lastFetched.get(), LocalDateTime.now())) > 20) {
                dailyDigestMap.set(gsonIO.read(filePaths.getDailyDigestJson()));
                lastFetched.set(LocalDateTime.now());
            }
        });
        app.get("/top24h", ctx -> ctx.json(dailyDigestMap).status(200));
        app.get("/mailing-list", ctx -> {
            String userEmail = ctx.queryParam("email");
        });
    }

}
