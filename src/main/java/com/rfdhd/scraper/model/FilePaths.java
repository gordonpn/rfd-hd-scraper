package com.rfdhd.scraper.model;

public class FilePaths {

    private String scrapingsJson;
    private String currentLinks;
    private String pastLinks;

    public String getScrapingsJson() {
        return scrapingsJson;
    }

    public void setScrapingsJson(String scrapingsJson) {
        this.scrapingsJson = scrapingsJson;
    }

    public String getCurrentLinks() {
        return currentLinks;
    }

    public void setCurrentLinks(String currentLinks) {
        this.currentLinks = currentLinks;
    }

    public String getPastLinks() {
        return pastLinks;
    }

    public void setPastLinks(String pastLinks) {
        this.pastLinks = pastLinks;
    }
}
