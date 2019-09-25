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

        if (dailyDigestMap == null || dailyDigestMap.isEmpty()) {
            return newMap;
        }

        Logger.info("Removing threads older than 72 hours");
        Logger.info("Size before: " + dailyDigestMap.size());

        dailyDigestMap.forEach((threadID, threadInfo) -> {
            if (Math.abs(HOURS.between(threadInfo.getLocalDateTime(), LocalDateTime.now())) < 72) {
                newMap.put(threadID, threadInfo);
            }
        });

        Logger.info("Size after: " + newMap.size());

        return newMap;
    }
}
