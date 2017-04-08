package com.example.trungnguyen.newsapp.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.trungnguyen.newsapp.R;
import com.example.trungnguyen.newsapp.fragment.FragmentTheGioi;
import com.example.trungnguyen.newsapp.helper.Help;
import com.example.trungnguyen.newsapp.helper.ImageHelper;
import com.example.trungnguyen.newsapp.helper.ObservableColorMatrix;
import com.example.trungnguyen.newsapp.model.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung Nguyen on 2/27/2017.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<News> mNewsList;
    private Context mContext;
    private static final int TYPE_LOADING_MORE = -1;
    private static final int NOMAL_ITEM = 1;
    boolean showLoadingMore;
    Resources res;

    private OnRecyclerViewOnClickListener mListener;
    public NewsAdapter(Context context, List<News> list) {
        mContext = context;
        mNewsList = list;
        res = mContext.getResources();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case NOMAL_ITEM:
                View view = LayoutInflater.from(mContext).inflate(R.layout.news_item, parent, false);
                view.setOnClickListener((View.OnClickListener) mContext);
                return new NewsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.news_item, parent, false));

            case TYPE_LOADING_MORE:
                return new LoadingMoreHolder(LayoutInflater.from(mContext).inflate(R.layout.loading_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case NOMAL_ITEM:
                bindViewHolderNormal((NewsViewHolder) holder, position);
                break;
            case TYPE_LOADING_MORE:
                bindLoadingViewHold((LoadingMoreHolder) holder, position);
                break;
        }

    }

    public void setOnRecyclerViewOnClickListener(OnRecyclerViewOnClickListener listener){
        mListener = listener;
    }

    private void bindLoadingViewHold(LoadingMoreHolder holder, int position) {
        holder.loadingBackground.setVisibility(showLoadingMore ? View.VISIBLE : View.INVISIBLE);
    }

    private void bindViewHolderNormal(final NewsViewHolder holder, final int position) {

         final News news = mNewsList.get(position);

//        holder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startDescribeActivity(news, holder);
//            }
//        });
//        holder.textView.setText("视频");
//
//        holder.textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startDescribeActivity(news,holder);
//            }
//        });

        holder.tvTitle.setText(news.getTitle());

        Glide.with(mContext)
                .load(news.getMainPicture())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (!news.hasFadedIn) {
                            holder.imageView.setHasTransientState(true);
                            final ObservableColorMatrix cm = new ObservableColorMatrix();
                            final ObjectAnimator animator = ObjectAnimator.ofFloat(cm, ObservableColorMatrix.SATURATION, 0f, 1f);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    holder.imageView.setColorFilter(new ColorMatrixColorFilter(cm));
                                }
                            });
                            animator.setDuration(2000L);
                            animator.setInterpolator(new AccelerateInterpolator());
                            animator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    holder.imageView.clearColorFilter();
                                    holder.imageView.setHasTransientState(false);
                                    animator.start();
                                    news.hasFadedIn = true;

                                }
                            });
                        }

                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .centerCrop()
                .into(holder.imageView);
    }


//    private void startDescribeActivity(News news, RecyclerView.ViewHolder holder) {
//
//        Intent intent = new Intent(mContext, MeiziPhotoDescribeActivity.class);
//        intent.putExtra("image", news.getUrl());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//            final android.support.v4.util.Pair<View, String>[] pairs = Help.createSafeTransitionParticipants
//                    ((Activity) mContext, false, new android.support.v4.util.Pair<>(((NewsViewHolder) holder).imageView, mContext.getString(R.string.meizi)));
//            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, pairs);
//            mContext.startActivity(intent, options.toBundle());
//        } else {
//            mContext.startActivity(intent);
//        }
//
//    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public void addItems(List<News> list) {
        mNewsList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        if (position < getDataItemCount()
                && getDataItemCount() > 0) {

            return NOMAL_ITEM;
        }
        return TYPE_LOADING_MORE;
    }

    private int getDataItemCount() {

        return mNewsList.size();
    }

    private int getLoadingMoreItemPosition() {
        return showLoadingMore ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

//    @Override
//    public void loadingStart() {
//        if (showLoadingMore) return;
//        showLoadingMore = true;
//        notifyItemInserted(getLoadingMoreItemPosition());
//    }
//
//    @Override
//    public void loadingfinish() {
//        if (!showLoadingMore) return;
//        final int loadingPos = getLoadingMoreItemPosition();
//        showLoadingMore = false;
//        notifyItemRemoved(loadingPos);
//    }


//    // TODO: 16/8/13  don't forget call fellow method
//    @Override
//    public void loadingStart() {
//        if (showLoadingMore) return;
//        showLoadingMore = true;
//        notifyItemInserted(getLoadingMoreItemPosition());
//    }
//
//    @Override
//    public void loadingfinish() {
//        if (!showLoadingMore) return;
//        final int loadingPos = getLoadingMoreItemPosition();
//        showLoadingMore = false;
//        notifyItemRemoved(loadingPos);
//    }

    public static class LoadingMoreHolder extends RecyclerView.ViewHolder {
        LinearLayout loadingBackground;

        public LoadingMoreHolder(View itemView) {
            super(itemView);
            loadingBackground = (LinearLayout) itemView.findViewById(R.id.loading_background);
        }
    }

    public interface OnRecyclerViewOnClickListener{
        void onRecyclerViewClickListener(int position);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView tvTitle;

        NewsViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.news_item_img);
            tvTitle = (TextView) itemView.findViewById(R.id.news_item_des);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onRecyclerViewClickListener(getAdapterPosition());
        }
    }
}
