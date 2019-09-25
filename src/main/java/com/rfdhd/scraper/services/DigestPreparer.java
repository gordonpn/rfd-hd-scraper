package com.rfdhd.scraper.services;

import com.rfdhd.scraper.model.ThreadInfo;
import org.pmw.tinylog.Logger;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.time.temporal.ChronoUnit.HOURS;

public class DigestPreparer {

    public Map removeOld(Map<String, ThreadInfo> dailyDigestMap) {
        Map newMap = new LinkedHashMap();
        final int HOURS_THRESHOLD = 120;

        if (dailyDigestMap == null || dailyDigestMap.isEmpty()) {
            return newMap;
        }

        Logger.info("Removing threads older than " + HOURS_THRESHOLD + " hours");
        Logger.info("Size before: " + dailyDigestMap.size());

        dailyDigestMap.forEach((threadID, threadInfo) -> {
            if (Math.abs(HOURS.between(threadInfo.getLocalDateTime(), LocalDateTime.now())) < HOURS_THRESHOLD) {
                newMap.put(threadID, threadInfo);
            }
        });

        Logger.info("Size after: " + newMap.size());

        return newMap;
    }
}
