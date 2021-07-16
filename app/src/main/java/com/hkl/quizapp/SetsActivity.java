package com.hkl.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;

import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SetsActivity extends AppCompatActivity {

    private GridView gridViewSets;
    private FirebaseFirestore firebaseFirestore;
    public static int category_id;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);

        Toolbar toolbar = findViewById(R.id.setsActivity_toolbar);
        setSupportActionBar(toolbar);
        String title = getIntent().getStringExtra("Category");
        category_id = getIntent().getIntExtra("Category_ID", 1);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gridViewSets = findViewById(R.id.setsActivity_gridViewSets);


        loadingDialog = new Dialog(SetsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();


        firebaseFirestore = FirebaseFirestore.getInstance();

        loadSets();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void loadSets() {
        firebaseFirestore.collection("Quiz").document("CAT" + String.valueOf(category_id)).
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    System.out.println("Task successful");
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        long sets = (long) documentSnapshot.get("SETS");

                        SetsAdapter setsAdapter = new SetsAdapter((int) sets);
                        gridViewSets.setAdapter(setsAdapter);

                        loadingDialog.cancel();

                    } else {
                        Toast.makeText(SetsActivity.this, "NO Category Document", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(SetsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.cancel();

            }
        });

    }
}