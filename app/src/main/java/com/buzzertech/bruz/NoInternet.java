package com.buzzertech.bruz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

public class NoInternet extends AppCompatActivity {
    private static final String TAG = "NoInternet" ;
    Handler handler = new Handler();
    Runnable runnable;
    CheckInternet checkInternet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        Button retry = (Button) findViewById(R.id.retry);

        setupWindowTransition();
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOnline();
            }
        });

        checkInternet = new CheckInternet(this);


        runnable = new Runnable() {
            @Override
            public void run() {
                if(CheckInternet.isDeviceOnline(NoInternet.this)){

                    Intent i = new Intent(NoInternet.this, Home.class);
                    startActivity(i);
                    finish();


                }
                else if (!CheckInternet.isDeviceOnline(NoInternet.this)){
                    handler.postDelayed(runnable, 1000);
                    Log.d(TAG, "Pushed the runnable!");
                }
            }
        };

        handler.postDelayed(runnable, 3000);
    }

    private void checkOnline() {
        if(CheckInternet.isDeviceOnline(this)){
            Intent i = new Intent(NoInternet.this, Home.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onBackPressed(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }


    private void setupWindowTransition() {
        Slide exitSlide = new Slide();
        exitSlide.setSlideEdge(Gravity.BOTTOM);
        exitSlide.setDuration(270);
        exitSlide.setInterpolator(new AccelerateDecelerateInterpolator());

        getWindow().setExitTransition(exitSlide);


        Slide enterSlide = new Slide();
        enterSlide.setSlideEdge(Gravity.TOP);
        enterSlide.setDuration(270);
        enterSlide.setInterpolator(new AccelerateDecelerateInterpolator());
        getWindow().setEnterTransition(enterSlide);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(runnable);
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }
}
