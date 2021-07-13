package com.hkl.quizapp;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appName=findViewById(R.id.splashActivity_txtViewAppName);
        Typeface typeface= ResourcesCompat.getFont(this,R.font.blacklist);
        appName.setTypeface(typeface);

        Animation animation= AnimationUtils.loadAnimation(this,R.anim.myanim    );
        appName.setAnimation(animation);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
            }
        }).start();
    }
}