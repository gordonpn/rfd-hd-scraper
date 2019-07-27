package com.rfdhd.scraper.model;

public class Threads {

    private String topicTitle;
    private int posts;
    private String votes;
    private String views;

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public String getVotes() {
        return votes;
    }

    public int getRawVotes() {
        return Integer.parseInt(votes.replaceAll("[+-]", ""));
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getViews() {
        return views;
    }

    public int getRawViews() {
        return Integer.parseInt(views.replaceAll(",", ""));
    }

    public void setViews(String views) {
        this.views = views;
    }
}
