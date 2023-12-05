package com.example.donatereddropp.ActivityClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.donatereddropp.R;

public class SplashScrean extends AppCompatActivity {
    LottieAnimationView animationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screan);
        // Hide the status bar
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        // Hide the navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        animationView = findViewById(R.id.lottieAnimationView);
        animationView.playAnimation();
        animationView.setSpeed(1.5f);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                SharedPreferences shrd=getSharedPreferences("login",MODE_PRIVATE);
                Boolean check=shrd.getBoolean("flag",false);
                Intent intent;

                if (check){
                    intent=new Intent(SplashScrean.this, Home.class);


                }else {
                    intent=new Intent(SplashScrean.this,Login.class);
                }
                animationView.cancelAnimation();
                startActivity(intent)   ;

            }
        },8000);
    }
}