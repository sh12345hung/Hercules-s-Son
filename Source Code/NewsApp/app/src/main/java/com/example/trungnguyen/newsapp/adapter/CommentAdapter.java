package com.example.trungnguyen.newsapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.trungnguyen.newsapp.R;
import com.example.trungnguyen.newsapp.helper.ImageHelper;
import com.example.trungnguyen.newsapp.model.Comment;
import com.example.trungnguyen.newsapp.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung Nguyen on 3/13/2017.
 */
public class CommentAdapter extends ArrayAdapter<Comment> {

    private List<Comment> mCommentList;
    private Context mContext;
    private CommentViewHolder holder;
//    private ImageHelper imageHelper;
//    private List<Bitmap> avatarList;
    private ArrayList<String> avatars;
    private ArrayList<String> names;
    private ArrayList<String> contents;
    private boolean isLogin = false;

    public CommentAdapter(Context context, int resource, List<Comment> objects) {
        super(context, resource, objects);
        mCommentList = objects;
        mContext = context;
//        imageHelper = new ImageHelper(mContext);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Log.d("CommentAdapter", "getView " + position);
        if (convertView == null) {
            holder = new CommentViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
            holder.imgCmtUserAvatar = (ImageView) convertView.findViewById(R.id.imgCmtUserAvatar);
            holder.tvCmtUserName = (TextView) convertView.findViewById(R.id.tvCmtUserName);
            holder.tvCmtContent = (TextView) convertView.findViewById(R.id.tvCmtContent);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBarCmt);
            this.configData();
            convertView.setTag(holder);
        } else
            holder = (CommentViewHolder) convertView.getTag();
//        holder.imgCmtUserAvatar.setImageBitmap(imageHelper.getBitmapFromUrl(
//                mCommentList.get(position).getmCmtUser().getmAvatarUrl())
//        );
//        holder.tvCmtUserName.setText(mCommentList.get(position).getmCmtUser().getmUserName());
//        holder.tvCmtContent.setText(mCommentList.get(position).getmCmtContent());
//        holder.imgCmtUserAvatar.setImageBitmap(imageHelper.getBitmapFromUrl(avatars.get(position), new ImageHelper.LoadSuccess() {
//            @Override
//            public void onLoadSuccess() {
//                Log.d("CMTADAP", "CALLBACK");
//                holder.progressBar.setVisibility(View.GONE);
//            }
//        }));

        Glide.with(mContext)
                .load(avatars.get(position))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .centerCrop()
                .into(holder.imgCmtUserAvatar);
        holder.tvCmtUserName.setText(names.get(position));
        holder.tvCmtContent.setText(contents.get(position));

        return convertView;
    }

    public void configData() {
        avatars = new ArrayList<>();
        names = new ArrayList<>();
        contents = new ArrayList<>();
        for (Comment cmt : mCommentList) {
            avatars.add(cmt.getmCmtUser().getmAvatarUrl());
            names.add(cmt.getmCmtUser().getmUserName());
            contents.add(cmt.getmCmtContent());
        }
    }

    public class CommentViewHolder {
        ImageView imgCmtUserAvatar;
        TextView tvCmtUserName;
        TextView tvCmtContent;
        ProgressBar progressBar;
    }
}
