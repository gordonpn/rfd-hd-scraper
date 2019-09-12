package com.rfdhd.scraper.model;

public class FilePaths {

    private String scrapingsJson;
    private String dailyDigestJson;
    private String archiveJson;
    private String templateHtml;

    public String getScrapingsJson() {
        return scrapingsJson;
    }

    public void setScrapingsJson(String scrapingsJson) {
        this.scrapingsJson = scrapingsJson;
    }

    public String getDailyDigestJson() {
        return dailyDigestJson;
    }

    public void setDailyDigestJson(String dailyDigestJson) {
        this.dailyDigestJson = dailyDigestJson;
    }

    public String getArchiveJson() {
        return archiveJson;
    }

    public void setArchiveJson(String archiveJson) {
        this.archiveJson = archiveJson;
    }

    public String getTemplateHtml() {
        return templateHtml;
    }

    public void setTemplateHtml(String templateHtml) {
        this.templateHtml = templateHtml;
    }
}
