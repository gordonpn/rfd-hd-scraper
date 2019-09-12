package com.rfdhd.scraper.report;

import com.rfdhd.scraper.model.FilePaths;

import java.time.LocalDate;
import java.util.Map;

public class DailyDigestEmail implements EmailContent {

    private final FilePaths filePaths;

    public DailyDigestEmail(FilePaths filePaths) {
        this.filePaths = filePaths;
    }

    @Override
    public String getReportName() {
        return LocalDate.now() + "-DailyDigestEmail";
    }

    @Override
    public String getTemplate() {
        return filePaths.getTemplateHtml() + "daily-digest-template.html";
    }

    @Override
    public Map<String, Object> getContent() {

        return null;
    }
}
