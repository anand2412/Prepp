package com.prepp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.prepp.utils.AppConstants;
import com.prepp.R;
import com.prepp.utils.PreferencesUtility;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imgGif=(ImageView)findViewById(R.id.img_gif);

        Glide.with(this)
                .load(R.drawable.splash_screen_anim)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .into(imgGif);


       new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                openActivity();
            }
        }, SPLASH_TIME_OUT);
    }

    private void openActivity() {

        boolean isActive= PreferencesUtility.getSharedPrefBooleanData(SplashActivity.this, AppConstants.PREF_IS_ACTIVE);
        if(!isActive) {
            Intent i = new Intent(SplashActivity.this, IntroActivity.class);
            startActivity(i);
        }else{
            boolean isPlayed=PreferencesUtility.getSharedPrefBooleanData(SplashActivity.this,AppConstants.PREF_QUIZ_PLAYED);
            if(!isPlayed) {
                Intent i = new Intent(SplashActivity.this, UserDashboardActivity.class)
                        .putExtra("WAIT_TIME", "").putExtra("MAIL_ACTIVE",isActive);
                startActivity(i);
            }else{
                Intent i = new Intent(SplashActivity.this, UserDashboardActivity.class)
                        .putExtra("WAIT_TIME", covertTime());
                startActivity(i);
            }
        }

        // close this activity
        finish();
    }

    private String covertTime(){


        Calendar calendaram = Calendar.getInstance();
        calendaram.set(Calendar.HOUR_OF_DAY, 23);
        calendaram.set(Calendar.MINUTE, 59);
        calendaram.set(Calendar.SECOND, 58);

        long millis=calendaram.getTimeInMillis()-System.currentTimeMillis();

        String hms = String.format("%2d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
        return hms;
    }

}
