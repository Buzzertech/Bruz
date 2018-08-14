package com.buzzertech.bruz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro;

public class TutorialActivity extends AppIntro {

    Fragment firstFragment, secondFragment, thirdFragment, fourthFragment, fifthfragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        firstFragment = new FragmentOne();
        secondFragment = new FragmentTwo();
        thirdFragment = new FragmentThree();
        fourthFragment = new FragmentFour();
        fifthfragment = new FragmentFive();
        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(firstFragment);
        addSlide(secondFragment);
        addSlide(thirdFragment);
        addSlide(fourthFragment);
        addSlide(fifthfragment);


        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(ContextCompat.getColor(this, R.color.baseColor));
        setSeparatorColor(Color.parseColor("#2196F3"));
        setFadeAnimation();

        // Hide Skip/Done button.



        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {

        goToNextSlide();
        // Do something when users tap on Skip button.
    }


    public void goToNextSlide() {
        pager.setCurrentItem(pager.getCurrentItem() + 4);

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent i = new Intent(this, Home.class);
        startActivity(i);
        finish();
        SharedPrefs sharedPrefs = new SharedPrefs(this);
        sharedPrefs.setFirstTimeLaunch(false);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.


    }
}
