package com.example.trungnguyen.newsapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
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

import com.example.trungnguyen.newsapp.adapter.ViewPagerAdapter;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    Toolbar toolbar;
    TabLayout tab;
    Button btSearch;
    ViewPager viewPager;
    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    AccessToken mAccessToken;
    String facebookUserName;
    boolean isUserLogin = false;
    String loginMenuTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "oncreate");
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
                    Log.d(TAG, "complete Graph API");
                    try {
                        facebookUserName = object.getString("name");
                        invalidateOptionsMenu();
                        Log.d(TAG, facebookUserName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link, email");
            request.setParameters(parameters);
            request.executeAsync();
        }
        addControls();
        setSupportActionBar(toolbar); // Because we are using AppCompat, that is support library
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
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
                invalidateOptionsMenu();
                break;
        }
        return true;
    }

    private void addControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbarID);
        tab = (TabLayout) findViewById(R.id.tab);
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

        Log.d("KIEMTRA", "onNavigationItemSelected " + viewPager.getCurrentItem());

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
