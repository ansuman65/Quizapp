package com.example.quizapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // UI references
        ImageView profileImage = view.findViewById(R.id.profileImage);
        TextView usernameText = view.findViewById(R.id.usernameText);
        TextView nameAgeText = view.findViewById(R.id.nameAgeText);
        TextView attemptsText = view.findViewById(R.id.attemptsText);
        TextView bestScoreText = view.findViewById(R.id.bestScoreText);
        Button logoutBtn = view.findViewById(R.id.logoutButton);

        // get data from SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("quiz_prefs", Context.MODE_PRIVATE);
        String username = prefs.getString("username", null);

        if (username != null) {
            usernameText.setText("Username: " + username);

            DBHelper dbHelper = new DBHelper(requireContext());
            int attempts = dbHelper.getQuizAttemptCount(username);
            int best = dbHelper.getBestScoreForUser(username);
            String[] details = dbHelper.getUserDetails(username); // [name, age, gender]

            String name = details[0];
            String age = details[1];
            String gender = details[2];

            // show name and age
            nameAgeText.setText("Name: " + name + " | Age: " + age);

            attemptsText.setText("Quizzes attempted: " + attempts);
            bestScoreText.setText("Best Score: " + best);

            // set profile image based on gender
            if (gender != null) {
                switch (gender.toLowerCase()) {
                    case "female":
                        profileImage.setImageResource(R.mipmap.ic_pfp_female);
                        break;
                    case "male":
                        profileImage.setImageResource(R.mipmap.ic_pfp_male);
                        break;
                    default:
                        profileImage.setImageResource(R.mipmap.ic_pfp);
                        break;
                }
            } else {
                profileImage.setImageResource(R.mipmap.ic_pfp); // fallback
            }

        } else {
            usernameText.setText("Username not found");
            nameAgeText.setText("Name: N/A | Age: N/A");
            attemptsText.setText("Quizzes attempted: N/A");
            bestScoreText.setText("Best Score: N/A");
        }

        // Logout logic
        logoutBtn.setOnClickListener(v -> {
            prefs.edit().remove("username").apply();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        });

        return view;
    }
}
