package com.rfdhd.scraper.model.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Configuration {

    @SerializedName("rootFolder")
    @Expose
    private String rootFolder;
    @SerializedName("pages")
    @Expose
    private Integer pages;
    @SerializedName("mailingList")
    @Expose
    private List<String> mailingList = null;
    @SerializedName("email Settings")
    @Expose
    private EmailSettings emailSettings;

    public String getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<String> getMailingList() {
        return mailingList;
    }

    public void setMailingList(List<String> mailingList) {
        this.mailingList = mailingList;
    }

    public EmailSettings getEmailSettings() {
        return emailSettings;
    }

    public void setEmailSettings(EmailSettings emailSettings) {
        this.emailSettings = emailSettings;
    }

}