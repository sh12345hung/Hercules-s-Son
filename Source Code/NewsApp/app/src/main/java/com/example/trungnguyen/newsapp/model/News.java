package com.example.trungnguyen.newsapp.model;

/**
 * Created by Trung Nguyen on 2/27/2017.
 */
public class News {
    String id;
    String title;
    String content;
    String mainPicture;
    String des;
    String url;
    String topic;
    public boolean hasFadedIn = false;
    public News(String id, String title, String content, String des, String url, String topic, String mainPicture) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.des = des;
        this.url = url;
        this.topic = topic;
        this.mainPicture = mainPicture;
    }

    public String getTopic(){
        return topic;
    }
    public void setTopic(String topic){
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
