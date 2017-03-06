package com.example.trungnguyen.newsapp.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.trungnguyen.newsapp.ButtonExpandable;
import com.example.trungnguyen.newsapp.DetailActivity;
import com.example.trungnguyen.newsapp.R;
import com.example.trungnguyen.newsapp.adapter.ExpandableAdapter;
import com.example.trungnguyen.newsapp.model.News;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung Nguyen on 2/21/2017.
 */
public class FragmentTheGioi extends Fragment implements
        ButtonExpandable, ExpandableListView.OnGroupClickListener,
        SwipyRefreshLayout.OnRefreshListener {
    List<News> newsList;
    ExpandableListView expListView;
    SwipyRefreshLayout mSwipeLayout;
    AppBarLayout appBarLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mReturnView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_thegioi, container, false);
        newsList = new ArrayList<News>();
        newsList.add(new News(1, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des)));
        newsList.add(new News(2, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des)));
        newsList.add(new News(3, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des)));
        newsList.add(new News(4, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des)));
        newsList.add(new News(5, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des)));
        newsList.add(new News(6, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des)));
        newsList.add(new News(7, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des)));
        newsList.add(new News(8, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des)));
        newsList.add(new News(9, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des)));
        newsList.add(new News(10, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des)));
        newsList.add(new News(11, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des)));
        newsList.add(new News(12, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des)));
        newsList.add(new News(13, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des)));
        newsList.add(new News(14, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des)));
        newsList.add(new News(15, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des)));
        addControls(mReturnView);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            expListView.setNestedScrollingEnabled(true);
        } else {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mSwipeLayout.getLayoutParams();
            params.bottomMargin = appBarLayout.getHeight();
            mSwipeLayout.setLayoutParams(params);
        }
        return mReturnView;
    }

    private void addControls(View mReturnView) {
        mSwipeLayout = (SwipyRefreshLayout) mReturnView.findViewById(R.id.swipeToRefresh);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setDirection(SwipyRefreshLayoutDirection.BOTTOM);


        expListView = (ExpandableListView) mReturnView.findViewById(R.id.expTheGioi);
        expListView.setOnGroupClickListener(this);

        appBarLayout = (AppBarLayout) mReturnView.findViewById(R.id.appBar);

        ExpandableAdapter adapter = new ExpandableAdapter(getContext(), newsList, expListView);
        expListView.setAdapter(adapter);
        adapter.setOnButtonClickExpand(this);
    }

    @Override
    public void onButtonExpandableClick(int groupPosition) {
        Log.d("KIEMTRA", "Ban click button roi");
        if (expListView.isGroupExpanded(groupPosition))
            expListView.collapseGroup(groupPosition);
        else expListView.expandGroup(groupPosition);
    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int position, long l) {
        Log.d("KIEMTRA", "Ban click group roi");
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        getActivity().startActivity(intent);
        // Must return true to remove action expand or collapse when we click on Group (not button)
        return true;
    }

//    @Override
//    public void onRefresh() {

//    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        mSwipeLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("TEST", "CHẠY HANDLER");
                mSwipeLayout.setRefreshing(false);
            }
        }, 1000);
        Log.d("TEST", "ĐANG REFRESH RỒI KU");
    }
}
