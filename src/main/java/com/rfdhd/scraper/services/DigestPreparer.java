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

        dailyDigestMap.forEach((threadID, threadInfo) -> {
            if (Math.abs(HOURS.between(threadInfo.getLocalDateTime(), LocalDateTime.now())) < 72) {
                newMap.put(threadID, threadInfo);
            }
        });

        return newMap;
    }

    public Map removeDuplicates(Map<String, ThreadInfo> mapGiven, String filePathCompareWith) {
        GsonIO gsonIO = new GsonIO();
        Map newMap = new LinkedHashMap();
        Map mapCompareWith = gsonIO.read(filePathCompareWith);

        if (mapGiven == null || mapGiven.isEmpty()) {
            return newMap;
        }

        Logger.info("Removing duplicates when comparing with " + filePathCompareWith);

        return newMap;
    }
}
