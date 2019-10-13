package com.example.zivug.Activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.zivug.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;


public class ActivitySplash extends AppCompatActivity {

    ProgressBar splashProgress;
    int SPLASH_TIME = 1000; //This is 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //This is additional feature, used to run a progress bar
        splashProgress = findViewById(R.id.splashProgress);
        Sprite doubleBounce = new DoubleBounce();
        splashProgress.setIndeterminateDrawable(doubleBounce);
        playProgress();

        //Code to start timer and take action after the timer ends
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do any action here. Now we are moving to next page
                Intent mySuperIntent = new Intent(ActivitySplash.this, AuthentificationActivity.class);
                startActivity(mySuperIntent);

                finish();

            }
        }, SPLASH_TIME);
    }

    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(1000)
                .start();
    }
}