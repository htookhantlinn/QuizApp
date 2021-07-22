package com.hkl.quizapp;

import static com.hkl.quizapp.SetsActivity.setIDs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView numOfQuestions, question, countDown;
    private Button option1, option2, option3, option4;
    public List<Question> questionList = new ArrayList<>();
    private int quesNum;
    private CountDownTimer countDownTimer;
    private int score;
    private FirebaseFirestore firebaseFirestore;
    private int setNo;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        numOfQuestions = findViewById(R.id.questionActivity_linearLayout1_textView_numOfQuestions);
        question = findViewById(R.id.questionActivity_linearLayout1_textView_Questions);
        countDown = findViewById(R.id.questionActivity_textView_countDown);

        option1 = findViewById(R.id.questionActivity_buttonOption1);
        option2 = findViewById(R.id.questionActivity_buttonOption2);
        option3 = findViewById(R.id.questionActivity_buttonOption3);
        option4 = findViewById(R.id.questionActivity_buttonOption4);


        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);

        loadingDialog = new Dialog(QuestionActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        questionList = new ArrayList<>();

        setNo = getIntent().getIntExtra("SetNo", 1);
        System.out.println("Sets Number " + setNo);
        firebaseFirestore = FirebaseFirestore.getInstance();
        getQuestionList();

    }

    private void getQuestionList() {
        questionList.clear();

        String currentCategoryId = SplashActivity.categoryList.get(SplashActivity.selected_category_index).getId();
        firebaseFirestore.collection("Quiz").document(currentCategoryId)
                .collection(setIDs.get(setNo))
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    docList.put(doc.getId(), doc);
                }

                QueryDocumentSnapshot questListDoc = docList.get("QUESTIONS_LIST");

                String count = questListDoc.getString("COUNT");

                for (int i = 0; i < Integer.valueOf(count); i++) {
                    String quesID = questListDoc.getString("Q" + String.valueOf(i + 1) + "_ID");

                    QueryDocumentSnapshot quesDoc = docList.get(quesID);
                    questionList.add(new Question(
                            quesDoc.getString("QUESTION"),
                            quesDoc.getString("A"),
                            quesDoc.getString("B"),
                            quesDoc.getString("C"),
                            quesDoc.getString("D"),
                            Integer.valueOf(quesDoc.getString("ANSWER"))
                    ));
                }
                if (questionList.size() == 0) {
                    Toast.makeText(QuestionActivity.this,"NO Questions ",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(QuestionActivity.this,SetsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    QuestionActivity.this.startActivity(intent);
                    QuestionActivity.this.finish();
                } else {
                    setQuestion();
                }
                loadingDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuestionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });


    }

    private void setQuestion() {
        countDown.setText(String.valueOf(10));
        question.setText(questionList.get(0).getQuestion());
        option1.setText(questionList.get(0).getOptionA());
        option2.setText(questionList.get(0).getOptionB());
        option3.setText(questionList.get(0).getOptionC());
        option4.setText(questionList.get(0).getOptionD());

        numOfQuestions.setText(String.valueOf(1) + "/" + String.valueOf(questionList.size()));

        startTimer();

        quesNum = 0;
    }

    private void startTimer() {

        countDownTimer = new CountDownTimer(12000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished < 10000) {
                    countDown.setText(String.valueOf(millisUntilFinished / 1000));
                }
            }

            @Override
            public void onFinish() {
                changeQuestion();
            }
        };
        countDownTimer.start();
    }


    @Override
    public void onClick(View v) {
        int selectedOption = 0;
        switch (v.getId()) {
            case R.id.questionActivity_buttonOption1:
                selectedOption = 1;
                break;
            case R.id.questionActivity_buttonOption2:
                selectedOption = 2;
                break;
            case R.id.questionActivity_buttonOption3:
                selectedOption = 3;
                break;
            case R.id.questionActivity_buttonOption4:
                selectedOption = 4;
                break;

            default:
        }
        countDownTimer.cancel();
        checkAnswer(selectedOption, v);
    }

    private void checkAnswer(int selectedOption, View view) {
        if (selectedOption == questionList.get(quesNum).getCorrectAns()) {
            //Right Answer
            score++;
            ((Button) view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        } else {
            //Wrong Answer
            ((Button) view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            switch (questionList.get(quesNum).getCorrectAns()) {
                case 1:
                    option1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 2:
                    option2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 3:
                    option3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 4:
                    option4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
            }
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeQuestion();
            }
        }, 2000);

    }

    private void changeQuestion() {
        if (quesNum < questionList.size() - 1) {
            // playAnim();
            quesNum++;
            playAnim(question, 0, 0);
            playAnim(option1, 0, 1);
            playAnim(option2, 0, 2);
            playAnim(option3, 0, 3);
            playAnim(option4, 0, 4);

            numOfQuestions.setText(String.valueOf(quesNum + 1) + "/" + String.valueOf(questionList.size()));

            countDown.setText(String.valueOf(10));

            startTimer();
        } else {
            // Go to Score Activity
            Intent intent = new Intent(QuestionActivity.this, ScoreActivity.class);
            intent.putExtra("SCORE", String.valueOf(score) + " / " + String.valueOf(questionList.size()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            // QuestionActivity.this.finish();
        }
    }

    private void playAnim(View view, final int value, int viewNum) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (value == 0) {
                    switch (viewNum) {
                        case 0:
                            ((TextView) view).setText(questionList.get(quesNum).getQuestion());
                            break;
                        case 1:
                            ((Button) view).setText(questionList.get(quesNum).getOptionA());
                            break;
                        case 2:
                            ((Button) view).setText(questionList.get(quesNum).getOptionB());
                            break;
                        case 3:
                            ((Button) view).setText(questionList.get(quesNum).getOptionC());
                            break;
                        case 4:
                            ((Button) view).setText(questionList.get(quesNum).getOptionD());
                            break;
                    }
                    if (viewNum != 0) {
                        ((Button) view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E99C03")));
                    }
                    playAnim(view, 1, viewNum);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDownTimer.cancel();
    }
}