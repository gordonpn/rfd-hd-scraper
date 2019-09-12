package com.rfdhd.scraper.model.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.jsoup.helper.Validate;

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

    public void updateUsernameAndPassword() {

        Authentication authentication = emailSettings.getAuthentication();

        if (authentication != null) {

            Validate.notNull(authentication, "Authentication should not be null");

            String password = authentication.getPassword();

            if (password != null && password.startsWith("${") && password.endsWith("}")) {

                String envVar = password.substring(2, password.length() - 1);

                String newPassword = (System.getProperty(envVar) != null) ? System.getProperty(envVar) : System.getenv(envVar);

                authentication.setPassword(newPassword);
            }

            String username = authentication.getUsername();

            if (username != null && username.startsWith("${") && username.endsWith("}")) {

                String envVar = username.substring(2, username.length() - 1);

                String newUsername = (System.getProperty(envVar) != null) ? System.getProperty(envVar) : System.getenv(envVar);

                authentication.setUsername(newUsername);
            }

        }

    }

}