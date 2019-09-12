package com.rfdhd.scraper.report;

import com.rfdhd.scraper.model.FilePaths;
import com.rfdhd.scraper.model.ThreadInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyDigestEmailContent implements EmailContent {

    private final FilePaths filePaths;
    private Map<String, ThreadInfo> map;

    public DailyDigestEmailContent(FilePaths filePaths, Map<String, ThreadInfo> map) {
        this.filePaths = filePaths;
        this.map = map;
    }

    @Override
    public String getReportName() {
        return LocalDate.now() + "-DailyDigestEmail";
    }

    @Override
    public String getTemplate() {
        return "daily-digest-template.html";
    }

    @Override
    public Map<String, Object> getContent() {
        HashMap<String, Object> variables = new HashMap<>();
        List<ThreadInfo> values = new ArrayList(map.values());

        variables.put("date", LocalDate.now());
        variables.put("values", values);

        return variables;
    }
}
