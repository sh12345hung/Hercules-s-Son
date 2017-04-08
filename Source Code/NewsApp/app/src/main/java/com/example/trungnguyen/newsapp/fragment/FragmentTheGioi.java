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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.trungnguyen.newsapp.adapter.NewsAdapter;
import com.example.trungnguyen.newsapp.helper.CheckForNetworkState;
import com.example.trungnguyen.newsapp.helper.DownloadListImageTask;
import com.example.trungnguyen.newsapp.helper.SimpleDividerItemDecoration;
import com.example.trungnguyen.newsapp.model.Comment;
import com.example.trungnguyen.newsapp.model.News;
import com.example.trungnguyen.newsapp.model.User;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.thaonguyen.MongoDBConnectorClient.MongoDBConnectorClient;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Trung Nguyen on 2/21/2017.
 */
public class FragmentTheGioi extends Fragment implements
        SwipyRefreshLayout.OnRefreshListener, NewsAdapter.OnRecyclerViewOnClickListener {
    public static final String COMMENT = "comment";
    public static final String NEWS_URL = "news_url";
    public static final String CHECK_NETWOK = "check_network";
    List<News> newsList;
    RecyclerView mRecyclerView;
    SwipyRefreshLayout mSwipeLayout;
    AppBarLayout appBarLayout;
    NewsAdapter mAdapter;
    CheckForNetworkState checker;
    MongoDBConnectorClient mClient;
    boolean isLogin = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mReturnView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_thegioi, container, false);
        isLogin = getArguments().getBoolean(MainActivity.IS_LOGIN);
        checker = new CheckForNetworkState(getContext());
        newsList = new ArrayList<News>();
        addControls(mReturnView);
        try {
            mClient = new MongoDBConnectorClient("Duy Trung") {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {

                }

                @Override
                public void onClose(int i, String s, boolean b) {

                }

                @Override
                public void onError(Exception e) {

                }

                @Override
                public void Login_Callback(boolean b) {

                }

                @Override
                public void GetNews_Callback(long l, List<String> list) {
                    for (String item : list) {
                        try {
                            JSONObject object = new JSONObject(item);
                            JSONObject idObj = object.getJSONObject("_id");
                            String id = idObj.getString("$oid");
                            String title = object.getString("TITLE");
                            String url = object.getString("URL");
                            String imageUrl = object.getString("IMAGEURL");
                            String topic = object.getString("TOPIC");
                            String sortDes = object.getString("SHORTDESC");
                            String fullDes = object.getString("FULLDESC");
                            News news = new News(id, title, fullDes, sortDes, url, topic, imageUrl);
                            newsList.add(news);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
//                    mAdapter.addItems(newsList);
                    mAdapter.notifyDataSetChanged();
//                    mAdapter.loadingStart();
                }

                @Override
                public void GetComment_Callback(List<String> list) {

                }
            };
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        try {
            mClient.Login();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mClient.GetNews("Thời sự", 1, 15);

        mAdapter = new NewsAdapter(getContext(), newsList);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter.setOnRecyclerViewOnClickListener(this);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
//        mAdapter.setOnButtonClickExpand(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mRecyclerView.setNestedScrollingEnabled(true);
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


        mRecyclerView = (RecyclerView) mReturnView.findViewById(R.id.expTheGioi);
//        mRecyclerView.setOnGroupClickListener(this);

        appBarLayout = (AppBarLayout) mReturnView.findViewById(R.id.appBar);


    }

//    @Override
//    public void onButtonExpandableClick(int groupPosition) {
//        Log.d("KIEMTRA", "button clicked");
//        if (expListView.isGroupExpanded(groupPosition))
//            expListView.collapseGroup(groupPosition);
//        else expListView.expandGroup(groupPosition);
//    }

//    @Override
//    public void onCommentsClick(int position) {
//        List<Comment> comments = new ArrayList<>();
//        String url = "https://scontent.xx.fbcdn.net/v/t1.0-1/p50x50/16427427_590536551139263_444919452715704300_n.jpg?oh=12843c9c03620ffd91e9febaf2a7add8&oe=596DC86E";
//        User user = new User(url, "Duy Trung", "", "");
//        String content = "Bố anh hút rất nhiều thuốc, mẹ anh chửi ổng quá trời";
//        Comment comment = new Comment(user, content);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        CommentDialog dialogFragment = new CommentDialog();
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList(COMMENT, (ArrayList<? extends Parcelable>) comments);
//        bundle.putBoolean(MainActivity.IS_LOGIN, isLogin);
//        dialogFragment.setArguments(bundle);
//        if (checker.isNetworkAvailable())
//            dialogFragment.show(getActivity().getSupportFragmentManager(), "COMMENT_DIALOG");
//    }

//    @Override
//    public boolean onGroupClick(ExpandableListView expandableListView, View view, int position, long l) {
//        Log.d("KIEMTRA", "group clicked");
//        if (checker.isNetworkAvailable()) {
//            Intent intent = new Intent(getActivity(), DetailActivity.class);
//            intent.putExtra(NEWS_URL, newsList.get(position).getUrl());
////            intent.putExtra(CHECK_NETWOK, checker.isNetworkAvailable());
//            getActivity().startActivity(intent);
//        } else
//            Toast.makeText(getActivity(), "Thiết bị hiện tại không kết nối internet", Toast.LENGTH_LONG).show();
//        // Must return true to remove action expand or collapse when we click on Group (not button)
//        return true;
//    }

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

    @Override
    public void onRecyclerViewClickListener(int position) {
        if (checker.isNetworkAvailable()) {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(NEWS_URL, newsList.get(position).getUrl());
            intent.putExtra(CHECK_NETWOK, checker.isNetworkAvailable());
            getActivity().startActivity(intent);
        } else
            Toast.makeText(getActivity(), "Thiết bị hiện tại không kết nối internet", Toast.LENGTH_LONG).show();
    }

    public interface LoadingMore {
        void loadingStart();

        void loadingfinish();
    }
}
