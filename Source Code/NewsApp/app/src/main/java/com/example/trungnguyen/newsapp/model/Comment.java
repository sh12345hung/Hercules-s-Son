package com.example.trungnguyen.newsapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Trung Nguyen on 3/13/2017.
 */
public class Comment implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(mCmtUser);
        parcel.writeString(mCmtContent);
    }

    private Comment(Parcel in) {
        mCmtUser = (User) in.readSerializable();
        mCmtContent = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
