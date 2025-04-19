package com.example.quizapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // must include home_fragment_container & bottomNav

        // Get username from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        currentUsername = prefs.getString("username", null);

        bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_quiz) {
                selectedFragment = new QuizListFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = ProfileFragment.newInstance(currentUsername);
            } else if (itemId == R.id.nav_leaderboard) {
                selectedFragment = new LeaderboardFragment();
            } else if (itemId == R.id.nav_more) {
                selectedFragment = new MoreFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });

        // Load default tab
        bottomNav.setSelectedItemId(R.id.nav_quiz);
    }
}
