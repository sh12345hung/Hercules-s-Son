package com.example.trungnguyen.newsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.trungnguyen.newsapp.adapter.ViewPagerAdapter;
import com.example.trungnguyen.newsapp.helper.ImageHelper;
import com.example.trungnguyen.newsapp.helper.NetworkStateReceiver;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String IS_LOGIN = "is_login";
    private Toolbar toolbar;
    private TabLayout tab;
    private Button btSearch;
//    private ImageView navAvatar;
    private ViewPager viewPager;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private AccessToken mAccessToken;
    private String facebookUserName;
    private boolean isUserLogin = false;
    private String loginMenuTitle;
    private String facebookPictureUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d(TAG, "oncreate");
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);
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
                        invalidateOptionsMenu();
                        Log.d(TAG, facebookPictureUrl);
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
        addControls();
//        Glide.with(this).load(facebookPictureUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(navAvatar) {
//            @Override
//            protected void setResource(Bitmap resource) {
//                RoundedBitmapDrawable circularBitmapDrawable =
//                        RoundedBitmapDrawableFactory.create(getResources(), resource);
//                circularBitmapDrawable.setCircular(true);
//                navAvatar.setImageDrawable(circularBitmapDrawable);
//            }
//        });
        setSupportActionBar(toolbar); // Because we are using AppCompat, that is support library
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_LOGIN, isUserLogin);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), bundle);
        viewPager.setAdapter(pagerAdapter);
        tab.setupWithViewPager(viewPager);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close); //drawerToggle phải đc khỏi tạo sau toolbar
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        IntentFilter filter = new IntentFilter(NetworkStateReceiver.UPDATE_UI_FROM_BROADCAST_CHANGE_NETWORK_STATE);
        registerReceiver(updateUIReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(updateUIReceiver);
    }

    private void addControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbarID);
        tab = (TabLayout) findViewById(R.id.tab);
//        navAvatar = (ImageView) findViewById(R.id.nav_avatar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        btSearch = (Button) findViewById(R.id.btSearch);
        btSearch.setOnClickListener(this);
        loginMenuTitle = getString(R.string.log_in);
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
        ImageHelper imageHelper = new ImageHelper(this);
        if (isUserLogin) {
            item = menu.findItem(R.id.menuLogin);
            item.setTitle(facebookUserName);
            item.setIcon(new BitmapDrawable(imageHelper.
                    circleBitmap(imageHelper.getBitmapFromUrl(facebookPictureUrl))));
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
