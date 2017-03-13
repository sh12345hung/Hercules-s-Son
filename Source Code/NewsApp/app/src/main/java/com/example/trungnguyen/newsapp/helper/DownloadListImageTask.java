package com.example.trungnguyen.newsapp.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung Nguyen on 3/13/2017.
 */
public class DownloadListImageTask extends AsyncTask<ArrayList<String>, Void, List<Bitmap>> {

    @Override
    protected List<Bitmap> doInBackground(ArrayList<String>... arrayLists) {
        ArrayList<String> list = arrayLists[0];
        List<Bitmap> bitmaps = new ArrayList<>();
        for (String url : list) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
                bitmaps.add(bitmap);
            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }
        }
        return bitmaps;
    }

}
