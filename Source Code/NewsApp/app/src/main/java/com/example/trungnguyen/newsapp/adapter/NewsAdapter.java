package com.example.trungnguyen.newsapp.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.trungnguyen.newsapp.DetailActivity;
import com.example.trungnguyen.newsapp.OnLoadMoreDataListener;
import com.example.trungnguyen.newsapp.R;
import com.example.trungnguyen.newsapp.fragment.FragmentTheGioi;
import com.example.trungnguyen.newsapp.helper.CheckForNetworkState;
import com.example.trungnguyen.newsapp.helper.ObservableColorMatrix;
import com.example.trungnguyen.newsapp.model.News;

import java.util.ArrayList;

/**
 * Created by Trung Nguyen on 2/27/2017.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FragmentTheGioi.LoadingMore {
    private ArrayList<News> mNewsList;
    private Context mContext;
    private static final int TYPE_LOADING_MORE = -1;
    private static final int NOMAL_ITEM = 1;
    private boolean showLoadingMore;
    private OnRefreshCompleted mListener;
    private OnLoadMoreDataListener mLoadMoreListener;


    public NewsAdapter(Context context) {
        mContext = context;
        mNewsList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d("ADAPTER", "onCreateViewHolder");
        switch (viewType) {
            case NOMAL_ITEM:
                return new NewsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.news_item, parent, false));

            case TYPE_LOADING_MORE:
                return new LoadingMoreHolder(LayoutInflater.from(mContext).inflate(R.layout.loading, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
//        Log.d("ADAPTER", "onBindViewHolder " + type + " ");
        switch (type) {
            case NOMAL_ITEM:
                bindViewHolderNormal((NewsViewHolder) holder, position);
                break;
            case TYPE_LOADING_MORE:
                bindLoadingViewHold((LoadingMoreHolder) holder, position);
                break;
        }

    }

    public void setOnLoadMoreListener(OnLoadMoreDataListener onLoadMoreListener) {
        this.mLoadMoreListener = onLoadMoreListener;
    }

    public void setOnRefreshCompleted(OnRefreshCompleted listener) {
        mListener = listener;
    }

    private void bindLoadingViewHold(LoadingMoreHolder holder, int position) {
        holder.Loading.setVisibility(showLoadingMore ? View.VISIBLE : View.INVISIBLE);
    }

    private void bindViewHolderNormal(final NewsViewHolder holder, final int position) {

        try {
            final News news = mNewsList.get(position);

            holder.tvTime.setText(news.getTime());
            holder.tvSource.setText(news.getSource());
            holder.tvTitle.setText(news.getTitle());
            holder.tvComment.setText(news.getCommentCount());
            // TODO: set text for comment count

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
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void addMoreItems(ArrayList<News> list) {
        try {
            Log.d("ADAP", "addItems");
            mNewsList.addAll(list);
            notifyDataSetChanged();
//            mListener.oneRefreshCompleted();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNewList(ArrayList<News> list) {
        try {
            mNewsList.addAll(list);
            notifyItemRangeChanged(0, mNewsList.size() - 1); // must notify range of adapter to load new data
            // if do not notify range of adapter, the results will be incorrect, 0 is start
            // position mNewsList.size() - 1 is end position, that is a new range if u refresh and load new data
            notifyDataSetChanged();
            mListener.oneRefreshCompleted();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addProgressItem(News progressItem) {
        try {
            mNewsList.add(progressItem);
            notifyItemInserted(getLoadingMoreItemPosition());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void clearData() {
        try {
            mNewsList.clear();
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemViewType(int position) {
//        Log.d("ADAPTER", "getItemViewType "+ position);
//        if (position < getDataItemCount()
//                && getDataItemCount() > 0) {
//            Log.d("ADAPTER", "getItemViewType "+ "NOMAL");
//            return NOMAL_ITEM;
//        }
//        Log.d("ADAPTER", "getItemViewType "+ "LOADING");
//        return TYPE_LOADING_MORE;
//        return mNewsList.get(position) != null ? NOMAL_ITEM : TYPE_LOADING_MORE;

//        if (position < getDataItemCount()
//                && getDataItemCount() > 0) {
//
//            return NOMAL_ITEM;
//        }
//        return TYPE_LOADING_MORE;

        return mNewsList.get(position) != null ? NOMAL_ITEM : TYPE_LOADING_MORE;
    }

    private int getDataItemCount() {
        Log.d("HOHO", mNewsList.size() + "");
        return mNewsList.size();
    }

    private int getLoadingMoreItemPosition() {
        return showLoadingMore ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    @Override
    public void loadingStart() {
        if (showLoadingMore) return;
        showLoadingMore = true;
//        notifyItemInserted(getLoadingMoreItemPosition());
        if (mLoadMoreListener != null) {
            mLoadMoreListener.onLoadMoreData();
        }
    }

    @Override
    public void loadingfinish() {
        Log.d("MainActivity", "loadingfinish");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!showLoadingMore) return;
        final int removePosition = getLoadingMoreItemPosition();
        showLoadingMore = false;

        try {
            mNewsList.remove(removePosition);
            notifyItemRemoved(removePosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //add items one by one
        //When you've added the items call the setLoaded()
//        notifyItemRemoved(pos);
    }

    public static class LoadingMoreHolder extends RecyclerView.ViewHolder {
        ProgressBar Loading;

        public LoadingMoreHolder(View itemView) {
            super(itemView);
            Loading = (ProgressBar) itemView.findViewById(R.id.footer);
        }
    }

    public interface OnRefreshCompleted {
        void oneRefreshCompleted();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView tvTitle;
        TextView tvSource;
        TextView tvComment;
        LinearLayout llComment;
        TextView tvTime;

        NewsViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.news_item_img);
            tvTitle = (TextView) itemView.findViewById(R.id.news_item_des);
            tvSource = (TextView) itemView.findViewById(R.id.news_item_source);
            tvComment = (TextView) itemView.findViewById(R.id.news_item_comment);
            llComment = (LinearLayout) itemView.findViewById(R.id.ll_comment);
            tvTime = (TextView) itemView.findViewById(R.id.news_item_time);
            llComment.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == llComment) {
                mLoadMoreListener.onCommentClick(getAdapterPosition());
            } else {
                try {
                    if (CheckForNetworkState.isNetworkAvailable()) {
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra(FragmentTheGioi.NEWS_URL, mNewsList.get(getAdapterPosition()).getUrl());
                        intent.putExtra(FragmentTheGioi.CHECK_NETWORK, CheckForNetworkState.isNetworkAvailable());
                        mContext.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
