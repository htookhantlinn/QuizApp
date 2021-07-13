package com.hkl.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

public class SetsActivity extends AppCompatActivity {

    private GridView gridViewSets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);

        Toolbar toolbar=findViewById(R.id.setsActivity_toolbar);
        setSupportActionBar(toolbar);
        String title=getIntent().getStringExtra("Category");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gridViewSets=findViewById(R.id.setsActivity_gridViewSets);

        SetsAdapter setsAdapter=new SetsAdapter(6);
        gridViewSets.setAdapter(setsAdapter);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}