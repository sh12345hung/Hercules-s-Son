package com.example.trungnguyen.newsapp.helper;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import java.util.concurrent.ExecutionException;

/**
 * Created by Trung Nguyen on 3/12/2017.
 */
public class ImageHelper {
    private Context mContext;
    String mUrl;
    Bitmap mImage;

    public ImageHelper(Context context, String url) {
        mContext = context;
        mUrl = url;
    }

    public Bitmap getBitmapFromUrl() {
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
        task.execute(mUrl);
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

    public Bitmap circleBitmap() {
        DrawCircleImage circleImage = new DrawCircleImage(mContext);
        Bitmap bitmap = getBitmapFromUrl();
        if (bitmap != null)
            return circleImage.getCroppedBitmap(bitmap, 400);
        else return bitmap;
    }
}
