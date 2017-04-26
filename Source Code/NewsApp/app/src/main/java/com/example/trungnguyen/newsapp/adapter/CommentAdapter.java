package com.example.trungnguyen.newsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.example.trungnguyen.newsapp.MainActivity;
import com.example.trungnguyen.newsapp.OnLoadMoreDataListener;
import com.example.trungnguyen.newsapp.R;
import com.example.trungnguyen.newsapp.fragment.FragmentTheGioi;
import com.example.trungnguyen.newsapp.helper.CheckForNetworkState;
import com.example.trungnguyen.newsapp.helper.ImageHelper;
import com.example.trungnguyen.newsapp.model.Comment;
import com.example.trungnguyen.newsapp.model.News;
import com.example.trungnguyen.newsapp.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung Nguyen on 3/13/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FragmentTheGioi.LoadingMore {

    private ArrayList<Comment> mCmtList;
    private Context mContext;
    private static final int TYPE_LOADING_MORE = -1;
    private static final int NOMAL_ITEM = 1;
    private boolean showLoadingMore;
    private NewsAdapter.OnRefreshCompleted mListener;
    private OnLoadMoreDataListener mLoadMoreListener;


    public CommentAdapter(Context context) {
        mContext = context;
        mCmtList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("ADAPTER", "onCreateViewHolder");
        switch (viewType) {
            case NOMAL_ITEM:
                return new CommentAdapter.CommentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false));

            case TYPE_LOADING_MORE:
                return new CommentAdapter.CommentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.loading, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
//        Log.d("ADAPTER", "onBindViewHolder " + type + " ");
        switch (type) {
            case NOMAL_ITEM:
                bindViewHolderNormal((CommentAdapter.CommentViewHolder) holder, position);
                break;
            case TYPE_LOADING_MORE:
                bindLoadingViewHold((CommentAdapter.LoadingMoreHolder) holder, position);
                break;
        }

    }


    private void bindLoadingViewHold(CommentAdapter.LoadingMoreHolder holder, int position) {
        holder.Loading.setVisibility(showLoadingMore ? View.VISIBLE : View.INVISIBLE);
    }

    private void bindViewHolderNormal(final CommentAdapter.CommentViewHolder holder, final int position) {

        try {
            final Comment cmt = mCmtList.get(position);

            holder.tvName.setText(cmt.getmCmtUser().getmUserName());
            holder.tvCotent.setText(cmt.getmCmtContent());
//             TODO: set text for comment count

            Glide.with(mContext)
                    .load(cmt.getmCmtUser().getmAvatarUrl())
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            if (!news.hasFadedIn) {
//                                holder.imageView.setHasTransientState(true);
//                                final ObservableColorMatrix cm = new ObservableColorMatrix();
//                                final ObjectAnimator animator = ObjectAnimator.ofFloat(cm, ObservableColorMatrix.SATURATION, 0f, 1f);
//                                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                                    @Override
//                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                                        holder.imageView.setColorFilter(new ColorMatrixColorFilter(cm));
//                                    }
//                                });
//                                animator.setDuration(2000L);
//                                animator.setInterpolator(new AccelerateInterpolator());
//                                animator.addListener(new AnimatorListenerAdapter() {
//                                    @Override
//                                    public void onAnimationEnd(Animator animation) {
//                                        super.onAnimationEnd(animation);
//                                        holder.imageView.clearColorFilter();
//                                        holder.imageView.setHasTransientState(false);
//                                        animator.start();
//                                        news.hasFadedIn = true;
//
//                                    }
//                                });
//                            }

//                            return false;
//                        }
                    /*})*/.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .fitCenter()
                    .centerCrop()
                    .into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void addMoreItems(ArrayList<Comment> list) {
        try {
//            Log.d("ADAP", "addItems");
            mCmtList.addAll(list);
            notifyDataSetChanged();
//            mListener.oneRefreshCompleted();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNewList(ArrayList<Comment> list) {
        try {
            mCmtList.addAll(list);
            notifyItemRangeChanged(0, mCmtList.size() - 1); // must notify range of adapter to load new data
            // if do not notify range of adapter, the results will be incorrect, 0 is start
            // position mNewsList.size() - 1 is end position, that is a new range if u refresh and load new data
            notifyDataSetChanged();
//            mListener.oneRefreshCompleted();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addProgressItem(Comment progressItem) {
        try {
            mCmtList.add(progressItem);
            notifyItemInserted(getLoadingMoreItemPosition());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void clearData() {
        try {
            mCmtList.clear();
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

        return mCmtList.get(position) != null ? NOMAL_ITEM : TYPE_LOADING_MORE;
    }

    private int getDataItemCount() {
        Log.d("HOHO", mCmtList.size() + "");
        return mCmtList.size();
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
//        Log.d("MainActivity", "loadingfinish");

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!showLoadingMore) return;
        final int removePosition = getLoadingMoreItemPosition();
        showLoadingMore = false;

        try {
            mCmtList.remove(removePosition);
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

    private class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvName;
        TextView tvCotent;

        CommentViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imgCmtUserAvatar);
            tvName = (TextView) itemView.findViewById(R.id.tvCmtUserName);
            tvCotent = (TextView) itemView.findViewById(R.id.tvCmtContent);
        }

//        @Override
//        public void onClick(View view) {
//            if (view == llComment) {
//                mLoadMoreListener.onCommentClick(mNewsList.get(getAdapterPosition()).getId());
//            } else {
//                try {
//                    if (CheckForNetworkState.isNetworkAvailable()) {
//                        Intent intent = new Intent(mContext, DetailActivity.class);
//                        intent.putExtra(FragmentTheGioi.NEWS_URL, mNewsList.get(getAdapterPosition()).getUrl());
//                        intent.putExtra(FragmentTheGioi.CHECK_NETWORK, CheckForNetworkState.isNetworkAvailable());
//                        mContext.startActivity(intent);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}

//public class CommentAdapter extends ArrayAdapter<Comment> {
//
//    private List<Comment> mCommentList;
//    private Context mContext;
//    private CommentViewHolder holder;
////    private ImageHelper imageHelper;
////    private List<Bitmap> avatarList;
//    private ArrayList<String> avatars;
//    private ArrayList<String> names;
//    private ArrayList<String> contents;
////    private boolean isLogin = false;
//
//    public CommentAdapter(Context context, int resource, List<Comment> objects) {
//        super(context, resource, objects);
//        mCommentList = objects;
//        mContext = context;
////        imageHelper = new ImageHelper(mContext);
//    }
//
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
////        Log.d("CommentAdapter", "getView " + position);
//        if (convertView == null) {
//            holder = new CommentViewHolder();
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
//            holder.imgCmtUserAvatar = (ImageView) convertView.findViewById(R.id.imgCmtUserAvatar);
//            holder.tvCmtUserName = (TextView) convertView.findViewById(R.id.tvCmtUserName);
//            holder.tvCmtContent = (TextView) convertView.findViewById(R.id.tvCmtContent);
//            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBarCmt);
//            this.configData();
//            convertView.setTag(holder);
//        } else
//            holder = (CommentViewHolder) convertView.getTag();
////        holder.imgCmtUserAvatar.setImageBitmap(imageHelper.getBitmapFromUrl(
////                mCommentList.get(position).getmCmtUser().getmAvatarUrl())
////        );
////        holder.tvCmtUserName.setText(mCommentList.get(position).getmCmtUser().getmUserName());
////        holder.tvCmtContent.setText(mCommentList.get(position).getmCmtContent());
////        holder.imgCmtUserAvatar.setImageBitmap(imageHelper.getBitmapFromUrl(avatars.get(position), new ImageHelper.LoadSuccess() {
////            @Override
////            public void onLoadSuccess() {
////                Log.d("CMTADAP", "CALLBACK");
////                holder.progressBar.setVisibility(View.GONE);
////            }
////        }));
//
//        Glide.with(mContext)
//                .load(avatars.get(position))
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        holder.progressBar.setVisibility(View.GONE);
//                        return false;
//                    }
//                }).diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .fitCenter()
//                .centerCrop()
//                .into(holder.imgCmtUserAvatar);
//        holder.tvCmtUserName.setText(names.get(position));
//        holder.tvCmtContent.setText(contents.get(position));
//
//        return convertView;
//    }
//
//    public void configData() {
//        avatars = new ArrayList<>();
//        names = new ArrayList<>();
//        contents = new ArrayList<>();
//        for (Comment cmt : mCommentList) {
//            avatars.add(cmt.getmCmtUser().getmAvatarUrl());
//            names.add(cmt.getmCmtUser().getmUserName());
//            contents.add(cmt.getmCmtContent());
//        }
//    }
//
//    public class CommentViewHolder {
//        ImageView imgCmtUserAvatar;
//        TextView tvCmtUserName;
//        TextView tvCmtContent;
//        ProgressBar progressBar;
//    }
//}
//
//
