package com.example.quizapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    TextView questionCounter;
    TextView questionText;
    TextView timerText;
    Button option1, option2, option3, option4, nextButton;

    SQLiteDatabase db;
    Cursor cursor;
    int score = 0;
    int totalQuestions;
    int currentQuestion = 1;

    CountDownTimer countDownTimer;
    long timeLeftInMillis = 45000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionCounter = findViewById(R.id.questionCounter);
        questionText = findViewById(R.id.questionText);
        timerText = findViewById(R.id.timerText);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        nextButton = findViewById(R.id.nextButton);

        String quizName = getIntent().getStringExtra("quizName");

        if (quizName == null) {
            Toast.makeText(this, "Quiz not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        DBHelper helper = new DBHelper(this);
        db = helper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM questions WHERE quiz_name = ?", new String[]{quizName});

        Log.d("QuizDebug", "Column count = " + cursor.getColumnCount());

        totalQuestions = cursor.getCount();

        if (totalQuestions == 0) {
            Toast.makeText(this, "No questions found for this quiz.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (cursor.getColumnCount() < 8) {
            Toast.makeText(this, "Invalid quiz data structure.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        cursor.moveToFirst();
        updateCounter();
        showQuestion();
        startTimer();

        option1.setOnClickListener(optionClick);
        option2.setOnClickListener(optionClick);
        option3.setOnClickListener(optionClick);
        option4.setOnClickListener(optionClick);

        nextButton.setOnClickListener(view -> {
            if (countDownTimer != null) countDownTimer.cancel();

            if (!cursor.isLast()) {
                cursor.moveToNext();
                currentQuestion++;
                updateCounter();
                showQuestion();
                enableOptions();
                startTimer();
            } else {
                cursor.close();
                Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                intent.putExtra("score", score);
                intent.putExtra("quizName", quizName);
                startActivity(intent);
                finish();
            }
        });
    }

    private final android.view.View.OnClickListener optionClick = view -> {
        if (countDownTimer != null) countDownTimer.cancel();

        Button selected = (Button) view;
        String correctAnswer = cursor.isNull(7) ? "" : cursor.getString(7);
        Log.d("QuizRow", "question=" + cursor.getString(2) + ", ans=" + correctAnswer);

        if (selected.getText().toString().equals(correctAnswer)) {
            score++;
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
        }

        disableOptions();
    };

    private void showQuestion() {
        questionText.setText(cursor.getString(2));
        option1.setText(cursor.getString(3));
        option2.setText(cursor.getString(4));
        option3.setText(cursor.getString(5));
        option4.setText(cursor.getString(6));
    }

    private void disableOptions() {
        option1.setEnabled(false);
        option2.setEnabled(false);
        option3.setEnabled(false);
        option4.setEnabled(false);
    }

    private void enableOptions() {
        option1.setEnabled(true);
        option2.setEnabled(true);
        option3.setEnabled(true);
        option4.setEnabled(true);
    }

    private void updateCounter() {
        questionCounter.setText("Question " + currentQuestion + " of " + totalQuestions);
    }

    private void startTimer() {
        timeLeftInMillis = 45000;
        timerText.setText("Time Left: 45s");

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText("Time Left: " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                Toast.makeText(QuizActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                nextButton.performClick();
            }
        }.start();
    }
}
