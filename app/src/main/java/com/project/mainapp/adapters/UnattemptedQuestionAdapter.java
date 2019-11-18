package com.project.mainapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mainapp.R;
import com.project.mainapp.models.DbModel;
import com.project.mainapp.models.UnattemptedModel;

import java.util.ArrayList;

public class UnattemptedQuestionAdapter extends RecyclerView.Adapter<UnattemptedQuestionAdapter.UnattemptedQuestionViewHolder> {

    ArrayList<UnattemptedModel> questions;

    public UnattemptedQuestionAdapter(ArrayList<UnattemptedModel> data){
        this.questions = data;
    }

    @NonNull
    @Override
    public UnattemptedQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                            int viewType) {
        return new UnattemptedQuestionViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.unattempted_question_adapter_layout, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull UnattemptedQuestionViewHolder holder, int position) {
        UnattemptedModel item = questions.get(position);
        holder.heading.setText(item.type);
        holder.body.setText(item.getQuestions());
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class UnattemptedQuestionViewHolder extends RecyclerView.ViewHolder {

        TextView heading, body;

        public UnattemptedQuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            heading = itemView.findViewById(R.id.heading);
            body = itemView.findViewById(R.id.body);
        }
    }
}
