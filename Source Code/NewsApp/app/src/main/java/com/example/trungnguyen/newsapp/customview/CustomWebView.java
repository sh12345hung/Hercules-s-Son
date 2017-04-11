package com.example.trungnguyen.newsapp.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by Trung Nguyen on 4/7/2017.
 */

public class CustomWebView extends WebView {

    public interface OnPageContentDisplayed {
        public void onPageContentDisplayed();
    }

    OnPageContentDisplayed mListener;

    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnPageContentDisplayed(OnPageContentDisplayed listener) {
        mListener = listener;
    }

    @Override
    public void invalidate() {
        super.invalidate();

        if (getContentHeight() > 1000) {
            Log.d("TEST", getContentHeight()+"");
            mListener.onPageContentDisplayed();
        }
    }

}
