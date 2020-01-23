package com.example.eyad.ministryofinteriorproject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.eyad.ministryofinteriorproject.Common.Common;
import com.example.osama.ministryofinteriorproject.R;
import com.wang.avi.AVLoadingIndicatorView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashScreenActivity extends AppCompatActivity {

    AVLoadingIndicatorView avi;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Monadi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_splash_screen);

        avi = findViewById(R.id.avi);

        startAnim();

        View parentLayout = findViewById(android.R.id.content);


        if(Common.isConnectedToInternet(SplashScreenActivity.this)){

            int secondsDelayed = 1;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();
                }
            }, secondsDelayed * 2000);

        }
        else{
            Snackbar.make(parentLayout, "الرجاء الاتصال بالأنترنت", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
            return;
        }
    }

    void startAnim(){
        avi.show();
    }

    void stopAnim(){
        avi.hide();
    }
}
