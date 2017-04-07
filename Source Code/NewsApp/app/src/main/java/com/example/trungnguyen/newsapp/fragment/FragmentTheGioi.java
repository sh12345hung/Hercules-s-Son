package com.example.trungnguyen.newsapp.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.trungnguyen.newsapp.CommentDialog;
import com.example.trungnguyen.newsapp.ExpandableListViewImp;
import com.example.trungnguyen.newsapp.DetailActivity;
import com.example.trungnguyen.newsapp.MainActivity;
import com.example.trungnguyen.newsapp.R;
import com.example.trungnguyen.newsapp.adapter.ExpandableAdapter;
import com.example.trungnguyen.newsapp.helper.CheckForNetworkState;
import com.example.trungnguyen.newsapp.helper.DownloadListImageTask;
import com.example.trungnguyen.newsapp.model.Comment;
import com.example.trungnguyen.newsapp.model.News;
import com.example.trungnguyen.newsapp.model.User;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung Nguyen on 2/21/2017.
 */
public class FragmentTheGioi extends Fragment implements
        ExpandableListViewImp, ExpandableListView.OnGroupClickListener,
        SwipyRefreshLayout.OnRefreshListener {
    public static final String COMMENT = "comment";
    public static final String NEWS_URL = "news_url";
    public static final String CHECK_NETWOK = "check_network";
    List<News> newsList;
    ExpandableListView expListView;
    SwipyRefreshLayout mSwipeLayout;
    AppBarLayout appBarLayout;
    CheckForNetworkState checker;
    boolean isLogin = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mReturnView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_thegioi, container, false);
        isLogin = getArguments().getBoolean(MainActivity.IS_LOGIN);
        checker = new CheckForNetworkState(getContext());
        newsList = new ArrayList<News>();
        newsList.add(new News(1, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(2, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(3, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(4, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(5, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(6, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(7, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(8, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(9, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(10, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(11, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(12, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(13, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(14, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(15, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(16, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(17, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(18, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(19, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(20, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(21, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(22, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        newsList.add(new News(23, getString(R.string.title), "Nguyen Duy Trung", getString(R.string.des), "http://thethao.vnexpress.net/photo/hau-truong/nhung-ong-chu-giau-nhat-cua-bong-da-anh-3563614.html"));
        addControls(mReturnView);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            expListView.setNestedScrollingEnabled(true);
        } else {
//            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mSwipeLayout.getLayoutParams();
//            params.bottomMargin = appBarLayout.getHeight();
//            mSwipeLayout.setLayoutParams(params);
            // TODO fix error on API < 20 devices
        }
        return mReturnView;
    }

    private void addControls(View mReturnView) {
        mSwipeLayout = (SwipyRefreshLayout) mReturnView.findViewById(R.id.swipeToRefresh);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);


        expListView = (ExpandableListView) mReturnView.findViewById(R.id.expTheGioi);
        expListView.setOnGroupClickListener(this);

        appBarLayout = (AppBarLayout) mReturnView.findViewById(R.id.appBar);

        ExpandableAdapter adapter = new ExpandableAdapter(getContext(), newsList, expListView);
        expListView.setAdapter(adapter);
        adapter.setOnButtonClickExpand(this);
    }

    @Override
    public void onButtonExpandableClick(int groupPosition) {
        Log.d("KIEMTRA", "button clicked");
        if (expListView.isGroupExpanded(groupPosition))
            expListView.collapseGroup(groupPosition);
        else expListView.expandGroup(groupPosition);
    }

    @Override
    public void onCommentsClick(int position) {
        List<Comment> comments = new ArrayList<>();
        String url = "https://scontent.xx.fbcdn.net/v/t1.0-1/p50x50/16427427_590536551139263_444919452715704300_n.jpg?oh=12843c9c03620ffd91e9febaf2a7add8&oe=596DC86E";
        User user = new User(url, "Duy Trung", "", "");
        String content = "Bố anh hút rất nhiều thuốc, mẹ anh chửi ổng quá trời";
        Comment comment = new Comment(user, content);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        CommentDialog dialogFragment = new CommentDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(COMMENT, (ArrayList<? extends Parcelable>) comments);
        bundle.putBoolean(MainActivity.IS_LOGIN, isLogin);
        dialogFragment.setArguments(bundle);
        if (checker.isNetworkAvailable())
            dialogFragment.show(getActivity().getSupportFragmentManager(), "COMMENT_DIALOG");
    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int position, long l) {
        Log.d("KIEMTRA", "group clicked");
        if (checker.isNetworkAvailable()) {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(NEWS_URL, newsList.get(position).getUrl());
//            intent.putExtra(CHECK_NETWOK, checker.isNetworkAvailable());
            getActivity().startActivity(intent);
        } else
            Toast.makeText(getActivity(), "Thiết bị hiện tại không kết nối internet", Toast.LENGTH_LONG).show();
        // Must return true to remove action expand or collapse when we click on Group (not button)
        return true;
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        mSwipeLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("TEST", "RUN HANDLER");
                mSwipeLayout.setRefreshing(false);
            }
        }, 3000);
        Log.d("TEST", "REFRESHING");
    }
}
