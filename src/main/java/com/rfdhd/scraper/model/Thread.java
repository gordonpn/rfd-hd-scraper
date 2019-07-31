package com.rfdhd.scraper.model;

public class Thread {

    String threadID;

    String topicTitle;

    String link;

    String posts;

    String votes;

    String views;

    String threadCategory;

    public Thread() {
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

    public String getPosts() {
        return this.posts;
    }

    public void setPosts(String value) {
        this.posts = value;
    }

    public String getVotes() {
        return this.votes;
    }

    public int getVotesInt() {
//        return Integer.parseInt(votes.replaceAll("[+-]", ""));
        return Integer.parseInt(votes);
    }

    public void setVotes(String value) {
        this.votes = value;
    }

    public String getViews() {
        return this.views;
    }

    public int getViewsInt() {
        return Integer.parseInt(views.replaceAll(",", ""));
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