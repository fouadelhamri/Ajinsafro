package com.example.ajinafro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {
    private static int SPLASH_TIMEOUT=5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Animation animateTranslate;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent nextActivity= new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(nextActivity);
                finish();
            }
        },SPLASH_TIMEOUT);
        //animateTranslate= AnimationUtils.loadAnimation(this,R.anim.right_to_left);

    }
}