package com.example.trungnguyen.newsapp.model;

/**
 * Created by Trung Nguyen on 3/13/2017.
 */
public class Comment {
    User mCmtUser;
    String mCmtContent;

    public Comment(User mCmtUser, String mCmtContent) {
        this.mCmtUser = mCmtUser;
        this.mCmtContent = mCmtContent;
    }

    public User getmCmtUser() {
        return mCmtUser;
    }

    public void setmCmtUser(User mCmtUser) {
        this.mCmtUser = mCmtUser;
    }

    public String getmCmtContent() {
        return mCmtContent;
    }

    public void setmCmtContent(String mCmtContent) {
        this.mCmtContent = mCmtContent;
    }
}
