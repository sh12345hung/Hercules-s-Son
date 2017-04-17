package com.example.trungnguyen.newsapp.model;

/**
 * Created by Trung Nguyen on 2/27/2017.
 */
public class News {
    private String id;
    private String title;
    private String content;
    private String mainPicture;
    private String des;
    private String url;
    private String topic;
    public boolean hasFadedIn = false;
    private String source;
    private String commentCount;
    private String time;

    public News() {

    }

    public News(String id, String title, String commentCount, String url, String mainPicture, String source, String time) {
        this.id = id;
        this.title = title;
        this.commentCount = commentCount;
        this.url = url;
        this.mainPicture = mainPicture;
        this.source = source;
        this.time = time;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCommentCount() {
        return this.commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(String mainPicture) {
        this.mainPicture = mainPicture;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
