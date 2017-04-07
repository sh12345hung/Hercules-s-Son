package com.example.trungnguyen.newsapp.model;

/**
 * Created by Trung Nguyen on 2/27/2017.
 */
public class News {
    int id;
    String title;
    String content;
    String mainPicture;
    String des;
    String url;

    public News(int id, String title, String content, String des, String url) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.des = des;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
