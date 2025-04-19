package com.example.quizapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import java.util.*;

public class LeaderboardFragment extends Fragment {

    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TextView tv = new TextView(getActivity());
        tv.setTextSize(18);
        tv.setPadding(32, 32, 32, 32);

        dbHelper = new DBHelper(getContext());

        StringBuilder result = new StringBuilder();
        result.append("🏆 Leaderboard (Top 5 Per Quiz)\n\n");

        // get quizzes
        Set<String> quizNames = dbHelper.getAllQuizNames();

        for (String quiz : quizNames) {
            result.append("📘 ").append(quiz).append("\n");

            List<String> scores = dbHelper.getTopScoresForQuiz(quiz);
            if (scores.isEmpty()) {
                result.append("  No scores yet\n");
            } else {
                int rank = 1;
                for (String row : scores) {
                    result.append("  ").append(rank++).append(". ").append(row).append("\n");
                }
            }

            result.append("\n");
        }

        tv.setText(result.toString());
        return tv;
    }
}
