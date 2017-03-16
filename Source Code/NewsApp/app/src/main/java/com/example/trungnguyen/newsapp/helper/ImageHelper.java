package com.example.trungnguyen.newsapp.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Trung Nguyen on 3/12/2017.
 */
public class ImageHelper {
    private Context mContext;
    static Bitmap mImage;
    List<Bitmap> list;

    public ImageHelper(Context context) {
        mContext = context;
    }

    public Bitmap getBitmapFromUrl(String url, LoadSuccess loadSuccess) {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Log.d("HELPER", "CALL DI");
//                    mImage = Picasso.with(mContext).load(mUrl).get();
//                } catch (IOException e) {
//                    Log.d("HELPER", e.toString());
//                }
//            }
//        });
//        thread.start();
//        Log.d("HELP", "getDrawableFromUrl");
//
//        if (mTarget == null) mTarget = new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                mImage = bitmap;
//                Log.d("HELP", "onBitmapLoaded");
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//                Log.d("HELP", "failed");
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//            }
//        };
//
//        Picasso.with(mContext).load(mUrl).resize(126, 126).into(mTarget);
//
        DownLoadImageTask task = new DownLoadImageTask();
        task.execute(url);
        try {
            mImage = task.get();
            if (mImage != null)
                loadSuccess.onLoadSuccess();
        } catch (InterruptedException e) {
//            Log.d("HELP", e.toString());
            e.printStackTrace();
        } catch (ExecutionException e) {
//            Log.d("HELP", e.toString());
            e.printStackTrace();
        }
        return mImage;
    }

    public Bitmap getBitmapFromUrl(String url) {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Log.d("HELPER", "CALL DI");
//                    mImage = Picasso.with(mContext).load(mUrl).get();
//                } catch (IOException e) {
//                    Log.d("HELPER", e.toString());
//                }
//            }
//        });
//        thread.start();
//        Log.d("HELP", "getDrawableFromUrl");
//
//        if (mTarget == null) mTarget = new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                mImage = bitmap;
//                Log.d("HELP", "onBitmapLoaded");
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//                Log.d("HELP", "failed");
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//            }
//        };
//
//        Picasso.with(mContext).load(mUrl).resize(126, 126).into(mTarget);
//
        DownLoadImageTask task = new DownLoadImageTask();
        task.execute(url);
        try {
            mImage = task.get();
        } catch (InterruptedException e) {
//            Log.d("HELP", e.toString());
            e.printStackTrace();
        } catch (ExecutionException e) {
//            Log.d("HELP", e.toString());
            e.printStackTrace();
        }
        return mImage;
    }

    public Bitmap circleBitmap(Bitmap bitmap) {
        DrawCircleImage circleImage = new DrawCircleImage(mContext);
        if (bitmap != null)
            return circleImage.getCroppedBitmap(bitmap, 400);
        else return bitmap;
    }

    public List<Bitmap> getListBitmap(ArrayList<String> stringArrayAvatar) {
        DownloadListImageTask task = new DownloadListImageTask();
        task.execute(stringArrayAvatar);
        try {
            list = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return list;
    }

    public interface LoadSuccess {
        void onLoadSuccess();
    }
}
