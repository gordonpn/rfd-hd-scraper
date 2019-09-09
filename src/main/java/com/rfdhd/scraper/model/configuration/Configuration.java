package com.rfdhd.scraper.model.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Configuration {

    @SerializedName("rootFolder")
    @Expose
    private String rootFolder;

    @SerializedName("pages")
    @Expose
    private int pages;

    public String getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
