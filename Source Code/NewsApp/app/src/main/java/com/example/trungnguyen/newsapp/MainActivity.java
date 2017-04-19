package com.example.trungnguyen.newsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.trungnguyen.newsapp.adapter.ViewPagerAdapter;
import com.example.trungnguyen.newsapp.fragment.FragmentCongNghe;
import com.example.trungnguyen.newsapp.fragment.FragmentGiaiTri;
import com.example.trungnguyen.newsapp.fragment.FragmentKinhTe;
import com.example.trungnguyen.newsapp.fragment.FragmentPhapLuat;
import com.example.trungnguyen.newsapp.fragment.FragmentTheGioi;
import com.example.trungnguyen.newsapp.fragment.FragmentTheThao;
import com.example.trungnguyen.newsapp.fragment.FragmentXeCo;
import com.example.trungnguyen.newsapp.helper.CalculateTimesAgo;
import com.example.trungnguyen.newsapp.helper.NetworkStateReceiver;
import com.example.trungnguyen.newsapp.model.News;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.thaonguyen.MongoDBConnectorClient.MongoDBConnectorClient;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String IS_LOGIN = "is_login";
    private Toolbar toolbar;
    private TabLayout tab;
    private Button btSearch;
    private ImageView navAvatar;
    private TextView navName;
    private ViewPager viewPager;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private AccessToken mAccessToken;
    private String facebookUserName;
    private boolean isUserLogin = false;
    private String loginMenuTitle;
    private String facebookPictureUrl;
    private static int OFFSCREENS_PAGE = 7;
    private View mHeaderView;
    MongoDBConnectorClient mClient;
    //    OnGetNewsCompleted mListener;
    private ArrayList<News> mNewsList;
    private ViewPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d(TAG, "oncreate");
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        addControls();
        isUserLogin = getIntent().getBooleanExtra(WelcomeActivity.IS_LOGIN, false);
        if (isUserLogin) {
            AccessTokenTracker tracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                    mAccessToken = currentAccessToken; // get new access token the first time login
                }
            };

            mAccessToken = AccessToken.getCurrentAccessToken(); // need use this line, if AccessToken is saved on your app

            GraphRequest request = GraphRequest.newMeRequest(mAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
//                    Log.d(TAG, "complete Graph API");
                    try {
//                        Log.d(TAG, object.toString());
                        facebookUserName = object.getString("name");
                        JSONObject pictureObj = object.getJSONObject("picture");
                        JSONObject dataObj = pictureObj.getJSONObject("data");
                        facebookPictureUrl = dataObj.getString("url");

                        Glide.with(MainActivity.this).load(facebookPictureUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(navAvatar) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                navAvatar.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                        navName.setText(facebookUserName);

                        invalidateOptionsMenu();
//                        Log.d(TAG, facebookPictureUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link, email, picture");
            request.setParameters(parameters);
            request.executeAsync();
        }
        try {
            mClient = new MongoDBConnectorClient() {
                @Override
                public void Login_Callback(boolean b, String s, boolean b1) {

                }

                @Override
                public void GetNews_Callback(String titleTopic, long l, List<String> list) {
                    try {
                        mNewsList.clear();
                        for (String item : list) {
                            try {
                                JSONObject itemObj = new JSONObject(item);
                                JSONObject idObj = itemObj.getJSONObject("_id");
                                String id = idObj.getString("$oid");
                                String title = itemObj.getString("TITLE");
                                String url = itemObj.getString("URL");
                                String imageUrl = itemObj.getString("IMAGEURL");
                                String commentCount = itemObj.getString("COMMENTCOUNT");
                                String source = itemObj.getString("SOURCE");
                                String time = CalculateTimesAgo.calculate(itemObj.getString("TIME"));
//                                String time = tempTime != null ? tempTime : "Chua xác d?nh";
                                News news = new News(id, title, commentCount, url, imageUrl, source, time);
                                mNewsList.add(news);
//                                Log.d(TAG, id + " " + title);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // TODO: 4/18/2017 : fix bug after GetNews_Callback updated
                        // TODO: 4/18/2017 bug: list.size = 0 -> mTopic = null -> pushData method do not called -> loading more progress bar still visible
                        // TODO: 4/18/2017 how to fix: get topic from GetNews_Callback without json data received -> mTopic != null with list.size() = 0 -> pushData() will be called -> loading progress invisible

                        Fragment fragment = mPagerAdapter.getItem(viewPager.getCurrentItem());

                        switch (titleTopic) {
                            case "Thể thao":
                                ((FragmentTheThao) fragment).pushData(mNewsList);
                                break;
                            case "Thế giới":
                                ((FragmentTheGioi) fragment).pushData(mNewsList);
                                break;
                            case "Khoa học - Công nghệ":
                                ((FragmentCongNghe) fragment).pushData(mNewsList);
                                break;
                            case "Giải trí":
                                ((FragmentGiaiTri) fragment).pushData(mNewsList);
                                break;
                            case "Xe cộ":
                                ((FragmentXeCo) fragment).pushData(mNewsList);
                                break;
                            case "Pháp luật":
                                ((FragmentPhapLuat) fragment).pushData(mNewsList);
                                break;
                            case "Kinh tế":
                                ((FragmentKinhTe) fragment).pushData(mNewsList);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void GetComment_Callback(List<String> list) {

                }

                @Override
                public void GetTopic_Callback(List<String> list) {

                }

                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    if (isUserLogin) {
                        try {
                            if (mAccessToken != null) {
                                Log.d(TAG, "TOKEN " + mAccessToken.toString());
                                mClient.Login(mAccessToken.toString());
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {

                }

                @Override
                public void onError(Exception e) {

                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }


        mNewsList = new ArrayList<>();

        setSupportActionBar(toolbar); // Because we are using AppCompat, that is support library

        getSupportActionBar().

                setDisplayShowTitleEnabled(false);

//        Bundle bundle = new Bundle();
//        bundle.putBoolean(IS_LOGIN, isUserLogin);
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        tab.setupWithViewPager(viewPager);
        drawerToggle = new

                ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close); //drawerToggle phải đc khỏi tạo sau toolbar
        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().

                setHomeButtonEnabled(true);

        getSupportActionBar().

                setDisplayHomeAsUpEnabled(true);
        drawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menuLogin:
                if (item.getTitle() == loginMenuTitle) {
                    Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //todo: move to profile activity
                }
                break;
            case R.id.menuAbout:
                break;
            case R.id.menuLogout:
                LoginManager.getInstance().logOut();
                isUserLogin = false;
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
                invalidateOptionsMenu();
                break;
        }
        return true;
    }

    BroadcastReceiver updateUIReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isNetworkAvailable = intent.getBooleanExtra(NetworkStateReceiver.IS_NETWORK_AVAILABLE, false);
            Log.d(TAG, "receiver: " + isNetworkAvailable);
            if (!isNetworkAvailable)
                showAlertDialogNetworkStateChange();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "On Resume");
        IntentFilter filter = new IntentFilter(NetworkStateReceiver.UPDATE_UI_FROM_BROADCAST_CHANGE_NETWORK_STATE);
        registerReceiver(updateUIReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(updateUIReceiver);
    }

    public MongoDBConnectorClient getClient() {
        return mClient;
    }

//    public interface OnGetNewsCompleted {
//        void onGetNewsCompleted(List<String> list);
//    }

    private void addControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbarID);
        tab = (TabLayout) findViewById(R.id.tab);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mHeaderView = navigationView.getHeaderView(0);
        navAvatar = (ImageView) mHeaderView.findViewById(R.id.img_nav_header_avatar);
        navName = (TextView) mHeaderView.findViewById(R.id.tv_nav_header_name);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        btSearch = (Button) findViewById(R.id.btSearch);
        btSearch.setOnClickListener(this);
        loginMenuTitle = getString(R.string.log_in);
        viewPager.setOffscreenPageLimit(OFFSCREENS_PAGE);
//        Log.d(TAG, "AddControls");
    }

    private void changeViewPagerPage(final int position) {
        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(position, true);
            }
        }, 100);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.drawMenuTheGioi)
            changeViewPagerPage(0);
        else if (id == R.id.drawMenuTheThao)
            changeViewPagerPage(1);
        else if (id == R.id.drawMenuCongNghe)
            changeViewPagerPage(2);
        else if (id == R.id.drawMenuGiaiTri)
            changeViewPagerPage(3);
        else if (id == R.id.drawMenuThoiTrang)
            changeViewPagerPage(4);
        else if (id == R.id.drawMenuSucKhoe)
            changeViewPagerPage(5);
        else
            changeViewPagerPage(6);

//        Log.d("KIEMTRA", "onNavigationItemSelected " + viewPager.getCurrentItem());

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Thoát ứng dụng")
                    .setMessage("Bạn có muốn thoát ứng dụng?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dialog.show();
        }
    }

    private void showAlertDialogNetworkStateChange() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("NETWORK NOT AVAILABLE")
                .setMessage("You need to connect to network");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        Intent searchIntent = new Intent(MainActivity.this, SearchingActivity.class);
        startActivity(searchIntent);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item;
        if (isUserLogin) {
            item = menu.findItem(R.id.menuLogin);
            item.setTitle(facebookUserName);
            item = menu.findItem(R.id.menuLogout);
            item.setVisible(true);
        } else {
            item = menu.findItem(R.id.menuLogout);
            item.setVisible(false);
            item = menu.findItem(R.id.menuLogin);
            item.setTitle(loginMenuTitle);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
