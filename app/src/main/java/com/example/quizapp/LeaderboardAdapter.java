package com.example.quizapp;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_SCORE = 1;

    private final List<LeaderboardSection> sectionList;

    public LeaderboardAdapter(List<LeaderboardSection> sectionList) {
        this.sectionList = sectionList;
    }

    @Override
    public int getItemViewType(int position) {
        int count = 0;
        for (LeaderboardSection section : sectionList) {
            if (position == count) return TYPE_HEADER;
            count++; // header
            int size = section.scores.size();
            if (position < count + size) return TYPE_SCORE;
            count += size;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        int total = 0;
        for (LeaderboardSection section : sectionList) {
            total += 1 + section.scores.size(); // 1 for header
        }
        return total;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard_score, parent, false);
            return new ScoreViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int count = 0;
        for (LeaderboardSection section : sectionList) {
            if (position == count && holder instanceof HeaderViewHolder) {
                HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
                headerHolder.title.setText("📘 " + section.quizTitle);

                // 🔁 Dynamically set background color based on quiz title
                int bgColor;
                String title = section.quizTitle.toLowerCase();
                if (title.contains("java")) {
                    bgColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.red_accent);
                } else if (title.contains("probability")) {
                    bgColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.orange_accent);
                } else if (title.contains("current")) {
                    bgColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.blue_accent);
                } else if (title.contains("psychometric")) {
                    bgColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.purple_accent);
                } else {
                    bgColor = ContextCompat.getColor(holder.itemView.getContext(), R.color.surface_dark);
                }

                headerHolder.title.setBackgroundColor(bgColor);
                headerHolder.title.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.button_text_light));
                return;
            }

            count++;
            int scoreIndex = position - count;
            if (scoreIndex < section.scores.size()) {
                if (holder instanceof ScoreViewHolder) {
                    ((ScoreViewHolder) holder).score.setText((scoreIndex + 1) + ". " + section.scores.get(scoreIndex));
                    return;
                }
            }
            count += section.scores.size();
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.quizHeader);
        }
    }

    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView score;
        ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            score = itemView.findViewById(R.id.scoreItem);
        }
    }
}
