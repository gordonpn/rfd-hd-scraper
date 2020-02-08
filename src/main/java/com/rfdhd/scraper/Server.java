package com.rfdhd.scraper;

import ch.qos.logback.classic.Logger;
import com.rfdhd.scraper.api.API;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static spark.Spark.*;

public class Server {

    private static Logger logger = (Logger) LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();
        int port = Integer.parseInt(dotenv.get("PORT", "7000"));
        port(port);

        staticFiles.location("/public");

        API.initialize();

        path("/api", () -> {
            before("/*", (req, res) -> logger.info("Received api call at {}", LocalDateTime.now()));
            get("/top24h", API.getData());
            get("/mailing-list/:email", API.signUp());
        });
    }
}
