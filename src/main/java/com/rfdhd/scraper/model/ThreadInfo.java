package com.rfdhd.scraper.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.rfdhd.scraper.utility.MachineChecker.isProdMachine;
import static com.rfdhd.scraper.utility.MachineChecker.isWindowsMachine;

public class ThreadInfo {

    @SerializedName("ThreadID")
    @Expose
    private String threadID;
    @SerializedName("Title")
    @Expose
    private String topicTitle;
    @SerializedName("Forum link")
    @Expose
    private String link;
    @SerializedName("Direct Link")
    @Expose
    private String directLink;
    @SerializedName("Posts")
    @Expose
    private String posts;
    @SerializedName("Votes")
    @Expose
    private String votes;
    @SerializedName("Views")
    @Expose
    private String views;
    @SerializedName("Category")
    @Expose
    private String threadCategory;
    @SerializedName("Content")
    @Expose
    private String content;
    @SerializedName("Thread date")
    @Expose
    private String date;

    public ThreadInfo() {
    }

    public ThreadInfo(String threadID) {
        this.threadID = threadID;
    }

    public String getThreadID() {
        return this.threadID;
    }

    public void setThreadID(String value) {
        this.threadID = value;
    }

    public String getTopicTitle() {
        return this.topicTitle;
    }

    public void setTopicTitle(String value) {
        value = Arrays.stream(value.split("\\s+")).distinct().collect(Collectors.joining(" "));
        this.topicTitle = value;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String value) {
        this.link = value;
    }

    public String getDirectLink() {
        return directLink;
    }

    public void setDirectLink(String directLink) {
        this.directLink = directLink;
    }

    public String getPosts() {
        return this.posts;
    }

    public void setPosts(String value) {
        this.posts = value;
    }

    public int getPostsInt() {
        if (posts == null) {
            return 0;
        } else {
            return Integer.parseInt(posts.replace(",", ""));
        }
    }

    public String getVotes() {
        return this.votes;
    }

    public void setVotes(String value) {
        if (StringUtils.isEmpty(value)) {
            value = "0";
        }
        this.votes = value;
    }

    public int getVotesInt() {
        if (votes == null) {
            return 0;
        } else {
            return Integer.parseInt(votes);
        }
    }

    public String getViews() {
        return this.views;
    }

    public void setViews(String value) {
        this.views = value;
    }

    public int getViewsInt() {
        if (views == null) {
            return 0;
        } else {
            return Integer.parseInt(views.replace(",", ""));
        }
    }

    public String getThreadCategory() {
        return this.threadCategory;
    }

    public void setThreadCategory(String value) {
        this.threadCategory = value;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public LocalDateTime getLocalDateTime() {
        String pattern;

        if (isWindowsMachine() || isProdMachine()) {
            pattern = "MMM d['st']['nd']['rd']['th'], yyyy h:mm a";
        } else {
            pattern = "LLL d['st']['nd']['rd']['th'], yyyy h:mm a";
            date = date.replace("pm", "p.m.").replace("am", "a.m.");
        }

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern(pattern)
                .toFormatter(Locale.ENGLISH);

        return LocalDateTime.parse(date, formatter);
    }
}