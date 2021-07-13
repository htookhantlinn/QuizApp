package com.hkl.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnStart;
    TextView appName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart=findViewById(R.id.mainActivity_buttonStart);
        appName=findViewById(R.id.mainActivity_textViewAppName);

        Typeface typeface= ResourcesCompat.getFont(this,R.font.blacklist);
        appName.setTypeface(typeface);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,CategoryActivity.class);
                startActivity(intent);
            }
        });
    }
}