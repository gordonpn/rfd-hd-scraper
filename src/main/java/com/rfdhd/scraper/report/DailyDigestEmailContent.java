package com.rfdhd.scraper.report;

import com.rfdhd.scraper.model.ThreadInfo;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyDigestEmailContent implements EmailContent {

    private List<ThreadInfo> list;

    public DailyDigestEmailContent(List<ThreadInfo> list) {
        this.list = list;
    }

    @Override
    public String getReportName() {
        return LocalDate.now() + "-DailyDigestEmail.html";
    }

    @Override
    public String getTemplate() {
        return "daily-digest-template.html";
    }

    @Override
    public Map<String, Object> getContent() {
        HashMap<String, Object> variables = new HashMap<>();

        variables.put("date", LocalDate.now());
        variables.put("values", list);

        return variables;
    }
}
