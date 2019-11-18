package com.project.mainapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mainapp.R;
import com.project.mainapp.models.ScoreModel;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    private ArrayList<ScoreModel> data;

    public ResultAdapter(ArrayList<ScoreModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ResultViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item, parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        ScoreModel item = data.get(position);

        holder.progressText.setText("0");
        if (item.totalScore <= 0 || item.earnedScore < 0) {
            holder.progressBar.setProgress(0);
        } else if (item.earnedScore == 0) {
            holder.progressBar.setProgress(0);
        } else {
            float prog = ((float) item.earnedScore) / ((float) item.totalScore) * 100;
            int progress = Math.round(prog);
            if (progress > 100) {
                progress = 100;
            }
            holder.progressBar.setProgress(progress);
            holder.progressText.setText(String.valueOf(progress));
        }
        holder.nameText.setText(item.type.replace(" ", "\n"));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        TextView progressText, nameText;


        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressText = itemView.findViewById(R.id.textView10);
            nameText = itemView.findViewById(R.id.textView6);
        }
    }
}
