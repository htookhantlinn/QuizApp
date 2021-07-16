package com.hkl.quizapp;

import static com.hkl.quizapp.SplashActivity.categoryList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private GridView categoryGridView;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar=findViewById(R.id.categoryActivity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        categoryGridView=findViewById(R.id.categoryActivity_gridView);




        CatGridAdapter catGridAdapter =new CatGridAdapter(categoryList);
        categoryGridView.setAdapter(catGridAdapter);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.home){
            CategoryActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);*//*
    }*/
}