package com.project.mainapp.adapters;

import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mainapp.R;
import com.project.mainapp.interfaces.DataChangeListener;
import com.project.mainapp.interfaces.LongClickCallback;
import com.project.mainapp.models.DbModel;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionsListAdapter extends RecyclerView.Adapter<QuestionsListAdapter.QuestionsListViewHolder> {

    private SparseIntArray points = new SparseIntArray();
    private ArrayList<DbModel> data;

    private HashMap<Integer, DbModel> questionsNotAttemptedMap = new HashMap<>();
    private HashMap<Integer, DbModel> voidMap = new HashMap<>();


    //listener if the data is changed or the question has been answered
    private DataChangeListener listener;
    //listener for if we are waiting for the click on the question that is to be deleted
    private LongClickCallback longListener;

    public QuestionsListAdapter(ArrayList<DbModel> data, DataChangeListener listener) { //updated data array is passed
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            questionsNotAttemptedMap.put(i, data.get(i)); //hashmap has the data
        }
        this.listener = listener;
    }

    public void swapData(ArrayList<DbModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public QuestionsListAdapter(ArrayList<DbModel> data,
                                LongClickCallback listener) {
        this.data = data;
        this.longListener = listener;
    }

    public ArrayList<DbModel> getData() {
        return data;
    }


    public ArrayList<DbModel> getUnattempted() {
        ArrayList<DbModel> d = new ArrayList<>();
        int size = data.size();
        for (int i = 0; i < size; i++) {
            DbModel temp = questionsNotAttemptedMap.get(i); //checking hashmap values at i.
            if (temp != null) {
                d.add(temp);
            }
        }
        return d;
    }

    @NonNull
    @Override
    public QuestionsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionsListViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_multiple_choice,
                        parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull final QuestionsListViewHolder holder,
                                 final int position) {
        final DbModel item = data.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (longListener != null) {
                    longListener.onLongClick(item);
                }
            }
        });

        holder.question.setText(item.answer + ". " + item.question);
        holder.op1.setText(item.option1);
        holder.op2.setText(item.option2);
        holder.op3.setText(item.option3);
        holder.op4.setText("Void");

        final String choice1 = item.choice1;
        final String choice2 = item.choice2;
        final String choice3 = item.choice3;
       // final String choice4 = item.choice4;

        switch (data.get(position).selectIndex) {
            case 0:
                points.put(position, Integer.valueOf(choice1));
                questionsNotAttemptedMap.put(position,null);
                holder.op1.setChecked(true);
                break;
            case 1:
                points.put(position, Integer.valueOf(choice2));
                questionsNotAttemptedMap.put(position,null);
                holder.op2.setChecked(true);
                break;
            case 2:
                points.put(position, Integer.valueOf(choice3));
                questionsNotAttemptedMap.put(position,null);
                holder.op3.setChecked(true);
                break;
            case 3:
                points.put(position, 0);
                questionsNotAttemptedMap.put(position,null);
                voidMap.put(position,item);
                holder.op4.setChecked(true);
                break;
        }


        holder.group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (longListener != null) {
                    longListener.onLongClick(item);
                    return;
                }
                questionsNotAttemptedMap.put(position,null);
                if (holder.op1.isChecked()) {
                    data.get(position).selectIndex = 0;
                    if (listener != null) {
                        listener.onDataChange(data);
                    }
                    points.put(position, Integer.valueOf(choice1));
                    Log.d("theH", "Choice 1 points: " + choice1);
                } else if (holder.op2.isChecked()) {
                    data.get(position).selectIndex = 1;
                    if (listener != null) {
                        listener.onDataChange(data);
                    }
                    points.put(position, Integer.valueOf(choice2));
                    Log.d("theH", "Choice 2 points: " + choice2);
                } else if (holder.op3.isChecked()) {
                    data.get(position).selectIndex = 2;

                    if (listener != null) {
                        listener.onDataChange(data);
                    }
                    Log.d("theH", "Choice 3 points: " + choice3);
                    points.put(position, Integer.valueOf(choice3));
                } else if (holder.op4.isChecked()) {
                    voidMap.put(position,item);
                    data.get(position).selectIndex = 3;
                    if (listener != null) {
                        listener.onDataChange(data);
                    }
                    points.put(position, 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public int getPointsEarnedByUser() {
        int totalPoints = 0;
        for (int i = 0; i < points.size(); i++) {
            int key = points.keyAt(i);
            // get the object by the key.
            totalPoints += points.get(key);
        }
        return totalPoints;
    }

    public int getMaxPointsThatCanBeEarned() {
        int size = data.size();
        int maxPoints = 0;
        for (int i = 0; i < size; i++) {
            if (questionsNotAttemptedMap.get(i) == null && voidMap.get(i) == null) {
                maxPoints += Integer.valueOf(data.get(i).totalScore); //add total score input to maxPoints to get the total score that can be earned.
            }
        }
        return maxPoints;
    }

    class QuestionsListViewHolder extends RecyclerView.ViewHolder {
        RadioButton op1, op2, op3, op4;
        RadioGroup group;
        TextView question;

        QuestionsListViewHolder(@NonNull View itemView) {
            super(itemView);

            op1 = itemView.findViewById(R.id.id1);
            op2 = itemView.findViewById(R.id.id2);
            op3 = itemView.findViewById(R.id.id3);
            op4 = itemView.findViewById(R.id.id4);
            group = itemView.findViewById(R.id.options);

            question = itemView.findViewById(R.id.question);
        }
    }
}
