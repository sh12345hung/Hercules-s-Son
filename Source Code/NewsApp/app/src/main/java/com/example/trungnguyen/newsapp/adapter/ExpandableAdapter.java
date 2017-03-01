package com.example.trungnguyen.newsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trungnguyen.newsapp.ButtonExpandable;
import com.example.trungnguyen.newsapp.DetailActivity;
import com.example.trungnguyen.newsapp.R;
import com.example.trungnguyen.newsapp.model.News;

import java.util.List;

/**
 * Created by Trung Nguyen on 2/27/2017.
 */
public class ExpandableAdapter extends BaseExpandableListAdapter implements View.OnClickListener {

    ButtonExpandable mListener;
    Context mContext;
    List<News> mNewsList;
    NewsViewHolder holder;
    ExpandableListView expandableListView;

    public ExpandableAdapter(Context context, List<News> list, ExpandableListView exp) {
        mContext = context;
        mNewsList = list;
        expandableListView = exp;
    }


    public void setOnButtonClickExpand(ButtonExpandable listener) {
        mListener = listener;
    }

    public int getGroupCount() {
        return mNewsList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int parentPos) {
        return mNewsList.get(parentPos);
    }

    @Override
    public Object getChild(int parentPos, int childPos) {
        return null;
    }

    @Override
    public long getGroupId(int parentPos) {
        return mNewsList.get(parentPos).getId();
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int parentPos, boolean isExpanded, View view, ViewGroup viewGroup) {
        View viewGroupParent = view;
        NewsViewHolder holder;
        if (viewGroupParent == null) {
            holder = new NewsViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewGroupParent = inflater.inflate(R.layout.news_item, viewGroup, false);
            holder.tvTitle = (TextView) viewGroupParent.findViewById(R.id.tvTitle);
            holder.tvDes = (TextView) viewGroupParent.findViewById(R.id.tvDescription);
            holder.imgMiniPic = (ImageView) viewGroupParent.findViewById(R.id.imgMiniPic);
            holder.btMore = (ImageView) viewGroupParent.findViewById(R.id.btMore);
            viewGroupParent.setTag(holder);
        } else
            holder = (NewsViewHolder) viewGroupParent.getTag();

        holder.tvTitle.setText(mNewsList.get(parentPos).getTitle());
        if (isExpanded)
            holder.tvTitle.setTextColor(Color.parseColor("#183545"));
        else
            holder.tvTitle.setTextColor(Color.GRAY);
        holder.tvDes.setText(mNewsList.get(parentPos).getDes());

        holder.btMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onButtonExpandableClick(parentPos);
            }
        });
        // TODO: 2/27/2017 set more about img Mini picture using picasso library
        return viewGroupParent;
    }

    @Override
    public View getChildView(int parentPos, int childPos, boolean isExpanded, View view, ViewGroup viewGroup) {
        View viewGroupChild = view;
        if (viewGroupChild == null) {
            holder = new NewsViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewGroupChild = inflater.inflate(R.layout.news_item_child, viewGroup, false);
            holder.tvMoreContent = (TextView) viewGroupChild.findViewById(R.id.tvMoreContent);
            holder.tvSource = (TextView) viewGroupChild.findViewById(R.id.tvSource);
            holder.btLater = (ImageView) viewGroupChild.findViewById(R.id.btLater);
            viewGroupChild.setTag(holder);
        } else
            holder = (NewsViewHolder) viewGroupChild.getTag();
        holder.tvMoreContent.setText(mNewsList.get(parentPos).getContent());
        return viewGroupChild;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public void onClick(View view) {

    }


    public class NewsViewHolder {
        TextView tvTitle;
        TextView tvDes;
        ImageView imgMiniPic;
        ImageView btMore;
        TextView tvMoreContent;
        TextView tvSource;
        ImageView btLater;
    }
}
