package com.hkl.quizapp;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private TextView appName;
    public static List<CategoryModel> categoryList = new ArrayList<>();
    public static int selected_category_index=0;
    public FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appName = findViewById(R.id.splashActivity_txtViewAppName);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        appName.setTypeface(typeface);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.myanim);
        appName.setAnimation(animation);
        firebaseFirestore = FirebaseFirestore.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {

                loadData();

            }
        }).start();
    }

    private void loadData() {
        categoryList.clear();
        firebaseFirestore.collection("Quiz").document("Categories").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    System.out.println("Task successful");
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        long count = (long) documentSnapshot.get("COUNT");
                        System.out.println("COUNT" + count);
                        for (int i = 0; i < count; i++) {
                            String categoryName = documentSnapshot.getString("CAT" + String.valueOf(i + 1)+"_NAME");
                            String categoryID = documentSnapshot.getString("CAT" + String.valueOf(i + 1)+"_ID");
                                categoryList.add(new CategoryModel(categoryID,categoryName));
                            System.out.println("Category name" + categoryName);
                        }
                        System.out.println("CATEGORY List size" + categoryList.size());
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        SplashActivity.this.finish();
                    } else {
                        Toast.makeText(SplashActivity.this, "NO Category Document", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(SplashActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}