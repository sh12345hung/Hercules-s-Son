package com.example.trungnguyen.newsapp.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Trung Nguyen on 3/12/2017.
 */
public class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            String link = strings[0];
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(link).getContent());
            return bitmap;
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
        return null;
    }
}
