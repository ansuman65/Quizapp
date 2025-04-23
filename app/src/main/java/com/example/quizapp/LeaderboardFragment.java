package com.example.quizapp;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class LeaderboardFragment extends Fragment {

    private DBHelper dbHelper;
    private RecyclerView leaderboardRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        leaderboardRecyclerView = view.findViewById(R.id.leaderboardRecyclerView);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DBHelper(getContext());

        List<LeaderboardSection> sections = new ArrayList<>();
        Set<String> quizNames = dbHelper.getAllQuizNames();

        for (String quiz : quizNames) {
            List<String> scores = dbHelper.getTopScoresForQuiz(quiz);
            sections.add(new LeaderboardSection(quiz, scores));
        }

        LeaderboardAdapter adapter = new LeaderboardAdapter(sections);
        leaderboardRecyclerView.setAdapter(adapter);

        return view;
    }
}
