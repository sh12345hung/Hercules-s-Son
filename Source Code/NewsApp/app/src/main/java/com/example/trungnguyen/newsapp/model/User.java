package com.example.trungnguyen.newsapp.model;

/**
 * Created by Trung Nguyen on 3/13/2017.
 */
public class User {
    String mAvatarUrl;
    String mUserName;
    String mUserEmail;
    String mUserFbLink;

    public User(String mAvatarUrl, String mUserName, String mUserEmail, String mUserFbLink) {
        this.mAvatarUrl = mAvatarUrl;
        this.mUserName = mUserName;
        this.mUserEmail = mUserEmail;
        this.mUserFbLink = mUserFbLink;
    }

    public String getmAvatarUrl() {
        return mAvatarUrl;
    }

    public void setmAvatarUrl(String mAvatarUrl) {
        this.mAvatarUrl = mAvatarUrl;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmUserEmail() {
        return mUserEmail;
    }

    public void setmUserEmail(String mUserEmail) {
        this.mUserEmail = mUserEmail;
    }

    public String getmUserFbLink() {
        return mUserFbLink;
    }

    public void setmUserFbLink(String mUserFbLink) {
        this.mUserFbLink = mUserFbLink;
    }
}
