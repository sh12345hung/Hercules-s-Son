package com.example.trungnguyen.newsapp;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = WelcomeActivity.class.getSimpleName();
    public static final String IS_LOGIN = "is_login";
    TextView tvSkip;
    ProgressBar progressBar;

    CallbackManager callbackManager;

    LoginButton loginButton;

    AccessToken accessToken;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(R.layout.activity_welcome);
        addControls();

        callbackManager = CallbackManager.Factory.create();

        accessToken = AccessToken.getCurrentAccessToken();


        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TEST", "THANH CONG");
                moveToMainActivity(true);
            }

            @Override
            public void onCancel() {
                Log.d("TEST", "TAT DANG NHAP");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(WelcomeActivity.this, "Thiết bị chưa được kết nối Internet", Toast.LENGTH_SHORT).show();
                Log.d("TEST", "LOI ROI: ");
            }
        });

        tvSkip.setOnClickListener(this);

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.example.trungnguyen.newsapp",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accessToken != null) {
            if (isNetworkAvailable())
                moveToMainActivity(true);
            else {
                Toast.makeText(WelcomeActivity.this, "Thiết bị chưa được kết nối Internet", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addControls() {
        tvSkip = (TextView) findViewById(R.id.tvSkip);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loginButton = (LoginButton) findViewById(R.id.login_button);
    }

    private void moveToMainActivity(boolean isLogin) {
        Intent mainIntent = new Intent(WelcomeActivity.this, MainActivity.class);
        mainIntent.putExtra(IS_LOGIN, isLogin);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if (!isNetworkAvailable())
            Toast.makeText(WelcomeActivity.this, "Thiết bị chưa được kết nối Internet", Toast.LENGTH_SHORT).show();
        else
            moveToMainActivity(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Must set callbackManager.onActivityResult(requestCode, resultCode, data);
        // to create facebook login button callback to get results of your login
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showDialogToEnableWifi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn cần phải bật wifi trước khi thực hiện kết nối");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });
        builder.show();
    }
}
