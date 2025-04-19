package com.example.quizapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {

    ListView leaderboardListView;
    TextView quizTitle;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        leaderboardListView = findViewById(R.id.leaderboardListView);
        quizTitle = findViewById(R.id.quizTitle);
        dbHelper = new DBHelper(this);

        String quizName = getIntent().getStringExtra("quizName");
        if (quizName == null) quizName = "Unknown Quiz";

        quizTitle.setText("Top 5 Scores - " + quizName);

        ArrayList<String> leaderboardData = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT username, score FROM scores WHERE quiz_name = ? ORDER BY score DESC LIMIT 5",
                new String[]{quizName}
        );

        int rank = 1;
        while (cursor.moveToNext()) {
            String user = cursor.getString(0);
            int score = cursor.getInt(1);
            leaderboardData.add(rank + ". " + user + " - " + score);
            rank++;
        }

        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, leaderboardData);
        leaderboardListView.setAdapter(adapter);
    }
}
