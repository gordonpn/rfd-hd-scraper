package com.rfdhd.scraper.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ThreadInfo {

    @SerializedName("ThreadID")
    @Expose
    String threadID;
    @SerializedName("Title")
    @Expose
    String topicTitle;
    @SerializedName("Forum link")
    @Expose
    String link;
    @SerializedName("Direct Link")
    @Expose
    String directLink;
    @SerializedName("Posts")
    @Expose
    String posts;
    @SerializedName("Votes")
    @Expose
    String votes;
    @SerializedName("Views")
    @Expose
    String views;
    @SerializedName("Category")
    @Expose
    String threadCategory;

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

    public int getPostsInt() {
        if (posts == null) {
            return 0;
        } else {
            return Integer.parseInt(posts.replaceAll(",", ""));
        }
    }

    public void setPosts(String value) {
        this.posts = value;
    }

    public String getVotes() {
        return this.votes;
    }

    public int getVotesInt() {
        if (votes == null) {
            return 0;
        } else {
            return Integer.parseInt(votes);
        }
    }

    public void setVotes(String value) {
        this.votes = value;
    }

    public String getViews() {
        return this.views;
    }

    public int getViewsInt() {
        if (views == null) {
            return 0;
        } else {
            return Integer.parseInt(views.replaceAll(",", ""));
        }
    }

    public void setViews(String value) {
        this.views = value;
    }

    public String getThreadCategory() {
        return this.threadCategory;
    }

    public void setThreadCategory(String value) {
        this.threadCategory = value;
    }
}