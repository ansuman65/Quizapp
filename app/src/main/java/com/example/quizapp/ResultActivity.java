package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView resultText;
    Button restartBtn, homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultText = findViewById(R.id.resultText);
        restartBtn = findViewById(R.id.restartButton);
        homeBtn = findViewById(R.id.homeButton);

        int score = getIntent().getIntExtra("score", 0);
        String quizName = getIntent().getStringExtra("quizName");
        String username = getSharedPreferences("quiz_prefs", MODE_PRIVATE).getString("username", "unknown");

        resultText.setText("Your Score: " + score);

        // ✅ Save the score in the DB
        DBHelper helper = new DBHelper(this);
        helper.saveScore(username, quizName, score);

        restartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
            intent.putExtra("quizName", quizName); // restart with same quiz
            startActivity(intent);
            finish();
        });

        homeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
