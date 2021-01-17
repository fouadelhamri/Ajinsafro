package com.example.ajinafro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.ajinafro.authentification.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {
    private static int SPLASH_TIMEOUT=2000;
    private SharedPreferences ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Animation animateTranslate;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ref=getSharedPreferences("ajinsafro",MODE_PRIVATE);
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(ref.getBoolean("isLogged",false)){
                    Intent nextActivity= new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(nextActivity);
                    finish();
                }else {
                    Intent nextActivity= new Intent(WelcomeActivity.this, SignInActivity.class);
                    startActivity(nextActivity);
                    finish();
                }

            }
        },SPLASH_TIMEOUT);
        //animateTranslate= AnimationUtils.loadAnimation(this,R.anim.right_to_left);

    }
}