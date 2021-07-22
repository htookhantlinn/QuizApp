package com.hkl.quizapp;

import static com.hkl.quizapp.SplashActivity.categoryList;
import static com.hkl.quizapp.SplashActivity.selected_category_index;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SetsActivity extends AppCompatActivity {

    private GridView gridViewSets;
    private FirebaseFirestore firebaseFirestore;
    // public static int category_id;
    private Dialog loadingDialog;
    public static List<String> setIDs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);

        Toolbar toolbar = findViewById(R.id.setsActivity_toolbar);
        setSupportActionBar(toolbar);
        /*String title = getIntent().getStringExtra("Category");
        category_id = getIntent().getIntExtra("Category_ID", 1);*/
        getSupportActionBar().setTitle(categoryList.get(selected_category_index).getName());
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
        setIDs.clear();
        firebaseFirestore.collection("Quiz").document(categoryList.get(selected_category_index).getId())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                long noOfSets = (long) documentSnapshot.get("SETS");
                for (int i = 1; i <= noOfSets; i++) {
                    setIDs.add(documentSnapshot.getString("SET" + String.valueOf(i) + "_ID"));
                }


                SetsAdapter setsAdapter = new SetsAdapter(setIDs.size());
                gridViewSets.setAdapter(setsAdapter);
                loadingDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SetsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });


    }
}