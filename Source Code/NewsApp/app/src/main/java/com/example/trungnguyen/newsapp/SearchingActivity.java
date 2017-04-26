package com.example.trungnguyen.newsapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.example.trungnguyen.newsapp.adapter.NewsAdapter;
import com.example.trungnguyen.newsapp.helper.CheckForNetworkState;
import com.example.trungnguyen.newsapp.helper.SimpleDividerItemDecoration;
import com.example.trungnguyen.newsapp.model.Comment;
import com.example.trungnguyen.newsapp.model.News;
import com.example.trungnguyen.newsapp.model.User;
import com.thaonguyen.MongoDBConnectorClient.MongoDBConnectorClient;

import java.util.ArrayList;
import java.util.List;

import static com.example.trungnguyen.newsapp.fragment.FragmentCongNghe.NEWS_ID;


public class SearchingActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String COMMENT = "comment";
    private static final String COMMENT_DIALOG = "dialog";
    SearchView searchView;
    MongoDBConnectorClient mClient;
    private String mQueryString;
    RecyclerView rvSearch;
    RecyclerView.OnScrollListener mListener;
    private static NewsAdapter mAdapter;
    private static int mCurrent;
    private static boolean mIsFirstTime;
    private static boolean mIsLoading;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        mClient = MainActivity.getClient();
        mCurrent = 0;
        mIsFirstTime = true;
        mIsLoading = false;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        searchView = (SearchView) findViewById(R.id.searchView);
        rvSearch = (RecyclerView) findViewById(R.id.lvSearchResult);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);


        EditText editText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setTextColor(Color.parseColor("#CC37474F"));
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false); // TODO: auto focus to search view, do not clicking on search button
        mAdapter = new NewsAdapter(this);
        rvSearch.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        rvSearch.setLayoutManager(mLayoutManager);
        rvSearch.setHasFixedSize(true);
        rvSearch.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        mListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
//                    if (fabScrollTop.getVisibility() == View.VISIBLE && dy > 20) {
//                        fabScrollTop.startAnimation(mAnimBottomOut);
//                        fabScrollTop.setVisibility(View.INVISIBLE);
//                    }
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
                        Log.d("SEARCHACTIVITY", "ENDLIST");
                        mAdapter.loadingStart();
                    }
//                    mClient.GetNews(TOPIC, mCurrentNews + 1, GET_NEWS_COUNT);
//                    updateCurrentNews();
//                } else {
//                    if (fabScrollTop.getVisibility() == View.INVISIBLE && dy < -10 && dy != 0) {
//                        fabScrollTop.setVisibility(View.VISIBLE);
//                        fabScrollTop.startAnimation(mAnimBottomIn);
//                    }
//                }
                }
            }
        };

        mAdapter.setOnLoadMoreListener(new OnLoadMoreDataListener() {
            @Override
            public void onLoadMoreData() {
//              index+=1;
//                mNewsList.add(null);
//                mAdapter.notifyItemInserted(mNewsList.size() - 1);
                try {
                    mIsLoading = true;
                    mAdapter.addProgressItem(null); // add null news item for checking progress bar visibility
                    mClient.Search(mQueryString, mCurrent + 1, 20);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCommentClick(String newsID) {
//                List<Comment> comments = new ArrayList<>();
//                String url = "https://scontent.xx.fbcdn.net/v/t1.0-1/p50x50/16427427_590536551139263_444919452715704300_n.jpg?oh=12843c9c03620ffd91e9febaf2a7add8&oe=596DC86E";
//                User user = new User(url, "Duy Trung", "", "");
//                String content = "Bố anh hút rất nhiều thuốc, mẹ anh chửi ổng quá trời";
//                Comment comment = new Comment(user, content);
//                comments.add(comment);
//                comments.add(comment);
//                comments.add(comment);
//                comments.add(comment);
//                comments.add(comment);
//                comments.add(comment);
//                comments.add(comment);
//                comments.add(comment);
//                comments.add(comment);
//                comments.add(comment);
//                comments.add(comment);
//                comments.add(comment);
//                comments.add(comment);
//                comments.add(comment);
//                comments.add(comment);
//                comments.add(comment);
//                comments.add(comment);
                mClient.GetComment(newsID);
                CommentDialog dialogFragment = new CommentDialog();
                Bundle bundle = new Bundle();
//                bundle.putParcelableArrayList(COMMENT, (ArrayList<? extends Parcelable>) comments);
                bundle.putString(NEWS_ID, newsID);
                bundle.putBoolean(MainActivity.IS_LOGIN, MainActivity.getLoginStatus());
                dialogFragment.setArguments(bundle);
                if (CheckForNetworkState.isNetworkAvailable())
                    dialogFragment.show(getSupportFragmentManager(), COMMENT_DIALOG);
            }
        });
        rvSearch.setOnScrollListener(mListener);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAdapter.clearData();
        if (!query.equals("")) {
            mQueryString = query;
            searchView.clearFocus(); //TODO: fix onQueryTextSubmit called twice
//          On real device the method just called once
            try {
                mClient.Search(query, mCurrent + 1, 20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public static void updateSearchResults(ArrayList<News> list) {
        mCurrent = mCurrent + 20;
        if (mIsLoading) {
            mIsLoading = false;
            mAdapter.loadingfinish();
            if (list.size() >= 0) {
                mAdapter.addMoreItems(list);
                mAdapter.notifyItemRangeChanged(0, mCurrent - 1);
            }
        } else {
            mIsFirstTime = false;
            if (mAdapter.getItemCount() > 0)
                mAdapter.clearData();
            mAdapter.addNewList(list);
        }
//        if (mIsLoading) {
//            mIsLoading = false;
//            mAdapter.loadingfinish();
//            if (list.size() >= 0) {
//                mAdapter.addMoreItems(list);
//                mAdapter.notifyItemRangeChanged(0, mCurrent - 1);
//            }
//        } else {
//            Log.d("SEARCHINGACTIVITY", "Vao else");
//            mIsFirstTime = false;
//        }
    }

}
