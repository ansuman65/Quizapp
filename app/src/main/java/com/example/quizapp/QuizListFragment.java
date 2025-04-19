package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class QuizListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_list, container, false);

        view.findViewById(R.id.javaQuizBtn).setOnClickListener(v -> launch("Java Quiz"));
        view.findViewById(R.id.probQuizBtn).setOnClickListener(v -> launch("Probability Quiz"));
        view.findViewById(R.id.currentAffairsBtn).setOnClickListener(v -> launch("Current Affairs in India Quiz"));
        view.findViewById(R.id.psychometricBtn).setOnClickListener(v -> launch("Psychometric Quiz"));

        return view;
    }

    private void launch(String name) {
        Intent intent = new Intent(getActivity(), QuizActivity.class);
        intent.putExtra("quizName", name);
        startActivity(intent);
    }
}
