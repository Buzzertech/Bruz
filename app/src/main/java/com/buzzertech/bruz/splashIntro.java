package com.buzzertech.bruz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import java.util.Random;

public class splashIntro extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_intro);
        int showSplashFor = 2500;
        Random r = new Random();
        int Low = 0;
        int High = 3;
        int Result = r.nextInt(High-Low) + Low;

        ImageView splashMotto = (ImageView) findViewById(R.id.motto);
        switch(Result){
            case 0:
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 500, getResources().getDisplayMetrics());
                splashMotto.getLayoutParams().width = width;
                splashMotto.getLayoutParams().height = height;
                splashMotto.requestLayout();
                splashMotto.setImageResource(R.drawable.splash_motto);
                break;
            case 1:
                splashMotto.setImageResource(R.drawable.splash_motto2);
                break;
            case 2:
                splashMotto.setImageResource(R.drawable.splash_motto3);
                break;
        }
        final ImageView logo = (ImageView) findViewById(R.id.Logo);
        logo.setTranslationY(100);
        logo.animate().translationY(0).setInterpolator(new OvershootInterpolator()).setDuration(300).start();
        SharedPrefs sharedPrefs = new SharedPrefs(this);
        final Boolean launched = sharedPrefs.isFirstTimeLaunch();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if(launched){
                    startActivity(new Intent(splashIntro.this, TutorialActivity.class));
                    splashIntro.this.finish();
                }
                else {
                    startActivity(new Intent(splashIntro.this, Home.class));
                    splashIntro.this.finish();
                }
            }
        }, showSplashFor);
    }


}
