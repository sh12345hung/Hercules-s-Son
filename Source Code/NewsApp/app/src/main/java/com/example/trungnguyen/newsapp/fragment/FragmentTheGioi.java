package com.example.trungnguyen.newsapp.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.trungnguyen.newsapp.MainActivity;
import com.example.trungnguyen.newsapp.OnLoadMoreDataListener;
import com.example.trungnguyen.newsapp.R;
import com.example.trungnguyen.newsapp.adapter.NewsAdapter;
import com.example.trungnguyen.newsapp.helper.SimpleDividerItemDecoration;
import com.example.trungnguyen.newsapp.model.News;

import com.thaonguyen.MongoDBConnectorClient.MongoDBConnectorClient;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Trung Nguyen on 2/21/2017.
 */
public class FragmentTheGioi extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener {
    public static final String COMMENT = "comment";
    public static final String NEWS_URL = "news_url";
    public static final String TOPIC = "Thế giới";
    public static final String CHECK_NETWORK = "check_network";
    public static final int GET_NEWS_COUNT = 15;
    private ArrayList<News> mNewsList;
    private LinearLayout llBackground;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private AppBarLayout appBarLayout;
    private ProgressBar mProgressBar;
    private NewsAdapter mAdapter;
    private MongoDBConnectorClient mClient;
    private int mCurrentNews;
    private RecyclerView.OnScrollListener mLoadingMore;
    boolean isLogin = false;
    private LinearLayoutManager mLayoutManager;
    private boolean mIsLoading;
    private boolean mIsFirstTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mReturnView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_thegioi, container, false);
        isLogin = getArguments().getBoolean(MainActivity.IS_LOGIN);
//        checker = new CheckForNetworkState(getContext());
        mNewsList = new ArrayList<News>();
        mCurrentNews = 0;
        mIsFirstTime = true;
        mIsLoading = false;
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
                    try {
                        mNewsList.clear();
                        for (String item : list) {
                            try {
                                JSONObject object = new JSONObject(item);
                                JSONObject idObj = object.getJSONObject("_id");
                                String id = idObj.getString("$oid");
                                String title = object.getString("TITLE");
                                String url = object.getString("URL");
                                String imageUrl = object.getString("IMAGEURL");
                                String topic = object.getString("TOPIC");
                                String fullDes = object.getString("DESC");
                                String source = object.getString("SOURCE");
                                News news = new News(id, title, fullDes, url, topic, imageUrl, source);
                                mNewsList.add(news);
                                Log.d("GOI", "123");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        updateCurrentNewsPosition();
                        if (mIsLoading) {
                            mIsLoading = false;
                            mAdapter.loadingfinish();
                            mAdapter.addMoreItems(mNewsList);
                        } else {
                            mAdapter.addNewList(mNewsList);
                            mProgressBar.setVisibility(View.INVISIBLE);
                            llBackground.setVisibility(View.INVISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void GetComment_Callback(List<String> list) {

                }
            };
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    mClient.Login();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (NotYetConnectedException e) {
//                    Log.d("SERVER DISCONNECT", "SERVER DISCONNECT");
//                }
//            }
//        });
//        thread.start();

        try {
            mClient.Login();
        } catch (InterruptedException | NotYetConnectedException e) {
            e.printStackTrace();
        }

        mAdapter = new NewsAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
//        mAdapter.setOnRecyclerViewOnClickListener(this);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        mRecyclerView.addOnScrollListener(mLoadingMore);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mRecyclerView.setNestedScrollingEnabled(true);
        } else {
//            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mSwipeLayout.getLayoutParams();
//            params.bottomMargin = appBarLayout.getHeight();
//            mSwipeLayout.setLayoutParams(params);
            // TODO fix error on API < 20 devices
        }

        mAdapter.setOnLoadMoreListener(new OnLoadMoreDataListener() {
            @Override
            public void onLoadMoreData() {
//              index+=1;
//                mNewsList.add(null);
//                mAdapter.notifyItemInserted(mNewsList.size() - 1);
                mIsLoading = true;
                mAdapter.addProgressItem(null); // add null news item for checking progress bar visibility
                mClient.GetNews(TOPIC, mCurrentNews + 1, GET_NEWS_COUNT);

            }
        });


        mAdapter.setOnRefreshCompleted(new NewsAdapter.OnRefreshCompleted() {
            @Override
            public void oneRefreshCompleted() {
                Log.d("FragmentTheGioi", "HIHI truoc " + mSwipeLayout.isRefreshing());
                mSwipeLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                    }
                });
                Log.d("FragmentTheGioi", "HIHI sau " + mSwipeLayout.isRefreshing());
            }
        });

        return mReturnView;
    }

//    public void updateNews() {
//        mAdapter.loadingfinish();
//        mIsLoading = false;
//        mAdapter.addItems(mNewsList);
//        mSwipeLayout.setRefreshing(false);
//        Log.d("GOI", "IS REFRESH AFTER "+ mSwipeLayout.isRefreshing());
//    }


    private void addControls(View mReturnView) {
        mSwipeLayout = (SwipeRefreshLayout) mReturnView.findViewById(R.id.swipeToRefresh);
        mSwipeLayout.setOnRefreshListener(this);

        mProgressBar = (ProgressBar) mReturnView.findViewById(R.id.progressBarTheGioi);

        mRecyclerView = (RecyclerView) mReturnView.findViewById(R.id.expTheGioi);
//        mRecyclerView.setOnGroupClickListener(this);
        intialListener();
        appBarLayout = (AppBarLayout) mReturnView.findViewById(R.id.appBar);

        llBackground = (LinearLayout) mReturnView.findViewById(R.id.ll_loading);
    }

    private void intialListener() {

        mLoadingMore = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                Log.d("KAKA", "scroll " + dy);
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
//                    Log.d("KAKA", "dy>0 roi " + dy);
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
//                    Log.d("KAKA", "dy>0 roi " + visibleItemCount);
//                    Log.d("KAKA", "dy>0 roi " + pastVisiblesItems);
//                    Log.d("KAKA", "dy>0 roi " + totalItemCount);
                    if (!mIsLoading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                        mIsLoading = true;
////                        index+=1;
//
//                        mAdapter.loadingStart();
//                        mNewsList.add(null);
//                        mAdapter.notifyDataSetChanged();
////                        mAdapter.notifyItemInserted(mNewsList.size() - 1);
//                        mClient.GetNews(TOPIC, mCurrentNews + 1, GET_NEWS_COUNT);
//                        updateCurrentNewsPosition();
                        mAdapter.loadingStart();
                    }
//                    mClient.GetNews(TOPIC, mCurrentNews + 1, GET_NEWS_COUNT);
//                    updateCurrentNews();
                }
            }
        };
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
////            intent.putExtra(CHECK_NETWORK, checker.isNetworkAvailable());
//            getActivity().startActivity(intent);
//        } else
//            Toast.makeText(getActivity(), "Thiết bị hiện tại không kết nối internet", Toast.LENGTH_LONG).show();
//        // Must return true to remove action expand or collapse when we click on Group (not button)
//        return true;
//    }

    private void updateCurrentNewsPosition() {
        mCurrentNews = GET_NEWS_COUNT + mCurrentNews;
    }

//    @Override
//    public void onRecyclerViewClickListener(int position) {
//        if (checker.isNetworkAvailable()) {
//            Intent intent = new Intent(getActivity(), DetailActivity.class);
//            intent.putExtra(NEWS_URL, newsList.get(position).getUrl());
//            intent.putExtra(CHECK_NETWORK, checker.isNetworkAvailable());
//            getActivity().startActivity(intent);
//        } else
//            Toast.makeText(getActivity(), "Thiết bị hiện tại không kết nối internet", Toast.LENGTH_LONG).show();
//    }

    @Override
    public void onRefresh() {
        Log.d("GOI", "REFRESH");
        mSwipeLayout.setRefreshing(true);
        mCurrentNews = 0;
//        if (mAdapter.getItemCount() > 0)
//            mAdapter.clearData();
        mClient.GetNews(TOPIC, mCurrentNews + 1, GET_NEWS_COUNT);
    }

    public interface LoadingMore {
        void loadingStart();

        void loadingfinish();
    }

    @Override
    public void onStart() {
        Log.d("FRAG", "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("FRAG", "onResume " + mCurrentNews);
        super.onResume();
        if (mIsFirstTime) {
            mIsFirstTime = false;
            mClient.GetNews(TOPIC, mCurrentNews + 1, GET_NEWS_COUNT);
        }
    }

    @Override
    public void onPause() {
        Log.d("FRAG", "onPause " + mCurrentNews);
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("FRAG", "onStop " + mCurrentNews);
        super.onStop();
    }
}
