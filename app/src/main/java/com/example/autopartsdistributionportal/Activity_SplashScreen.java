package com.example.autopartsdistributionportal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Activity_SplashScreen extends AppCompatActivity {
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    Thread splashTread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__splash_screen);
        StartAnimations();
    }

    public void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);
        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        SharedPreferences pref = getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        Boolean saveLogin = pref.getBoolean("saveLogin", false);

        if (saveLogin.equals(true)) {
            finish();
            startActivity(new Intent(Activity_SplashScreen.this, MainActivity.class));
        } else {

            splashTread = new Thread() {
                @Override
                public void run() {
                    try {
                        int waited = 0;
                        while (waited < 3500) {
                            sleep(100);
                            waited += 100;
                        }
                        Intent intent = new Intent(Activity_SplashScreen.this, Activity_Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        Activity_SplashScreen.this.finish();
                    } catch (InterruptedException e) {
                    } finally {
                        Activity_SplashScreen.this.finish();
                    }
                }
            };
            splashTread.start();
        }
    }
}

