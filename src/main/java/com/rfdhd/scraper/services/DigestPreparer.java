package com.rfdhd.scraper.services;

import ch.qos.logback.classic.Logger;
import com.rfdhd.scraper.model.ThreadInfo;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.time.temporal.ChronoUnit.HOURS;

public class DigestPreparer {

    private static Logger logger = (Logger) LoggerFactory.getLogger(Scraper.class);

    public Map removeOld(Map<String, ThreadInfo> dailyDigestMap) {
        Map newMap = new LinkedHashMap();
        final int HOURS_THRESHOLD = 120;

        if (dailyDigestMap == null || dailyDigestMap.isEmpty()) {
            return newMap;
        }

        logger.info("Removing threads older than {} hours", HOURS_THRESHOLD);
        logger.info("Size before: {}", dailyDigestMap.size());

        dailyDigestMap.forEach((threadID, threadInfo) -> {
            if (Math.abs(HOURS.between(threadInfo.getLocalDateTime(), LocalDateTime.now())) < HOURS_THRESHOLD) {
                newMap.put(threadID, threadInfo);
            }
        });

        logger.info("Size after: {}", newMap.size());

        return newMap;
    }
}
