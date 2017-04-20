package com.example.trungnguyen.newsapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.thaonguyen.MongoDBConnectorClient.MongoDBConnectorClient;

/**
 * Created by Trung Nguyen on 4/20/2017.
 */

public class AppNewsPreference {
    public static final String JSON_STRING = "json_string";
    public static MongoDBConnectorClient mCurrentClient;

    public static SharedPreferences getLastSongPreference(Context context) {
        return context.getSharedPreferences("NewsReader", Activity.MODE_PRIVATE);
    }

    public static void saveLastClient(Context context, MongoDBConnectorClient client) {
        SharedPreferences.Editor editor = getLastSongPreference(context).edit();
        Gson gson = new Gson();
        String jsonString = gson.toJson(client);
        editor.putString(JSON_STRING, jsonString);
        editor.commit();
    }

    public static MongoDBConnectorClient getCurrentClient(Context context) {
        SharedPreferences mSharedPreferences = getLastSongPreference(context);
        String lastClient = mSharedPreferences.getString(JSON_STRING, null);
        Gson mGson = new Gson();
        if (lastClient != null)
            mCurrentClient = mGson.fromJson(lastClient, MongoDBConnectorClient.class);
        return mCurrentClient;
    }

}
