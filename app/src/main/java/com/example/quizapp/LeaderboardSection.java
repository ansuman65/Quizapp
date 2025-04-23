package com.example.quizapp;

import java.util.List;

public class LeaderboardSection {
    public String quizTitle;
    public List<String> scores;

    public LeaderboardSection(String quizTitle, List<String> scores) {
        this.quizTitle = quizTitle;
        this.scores = scores;
    }
}
