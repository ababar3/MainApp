package com.project.mainapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mainapp.R;
import com.project.mainapp.adapters.QuestionsListAdapter;
import com.project.mainapp.database.DatabaseHelper;
import com.project.mainapp.interfaces.DataChangeListener;
import com.project.mainapp.models.DbModel;
import com.project.mainapp.models.ScoreModel;
import com.project.mainapp.utils.Constants;
import com.project.mainapp.utils.PrefUtils;

import java.util.ArrayList;

public class QuestionsListActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private Button proceedBtn;
    private String type = null;
    private TextView title, emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_list);
    }

    @Override
    protected void onResume() {
        super.onResume();

        recycler = findViewById(R.id.questionListRecycler);
        proceedBtn = findViewById(R.id.btnProceed);
        emptyTextView = findViewById(R.id.emptyTextView);
        title = findViewById(R.id.textView3);

        recycler.setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.GONE);

        recycler.setLayoutManager(new LinearLayoutManager(this));

        type = getIntent().getStringExtra(Constants.QUESTION_TYPE_KEY);

        log("Type: " + type);

        title.setText(type);

        initView();
    }

    private void log(String msg) {
        Log.d("theH", msg);
    }

    private void initView() {
        DatabaseHelper db = new DatabaseHelper(this);
        final ArrayList<DbModel> data = db.getAllFromType(type);
        ArrayList<DbModel> previouslySavedData = PrefUtils.fetchList(QuestionsListActivity.this,
                type); //s questions specfic page get saved into previouslySavedData
        final ArrayList<DbModel> newData = new ArrayList<>();

        //this checks for the previous answers question and saves that to the adapter
        if (previouslySavedData != null) {
            if (data.size() > previouslySavedData.size()) {
                newData.addAll(previouslySavedData);
                int size = previouslySavedData.size();
                int size2 = data.size();
                for (int i = size; i < size2; i++) {  //for loop to add new data to newData list.
                    newData.add(data.get(i));
                }
            } else if (data.size() == previouslySavedData.size()) {
                newData.addAll(previouslySavedData);
            }
        } else {
            newData.addAll(data);
        }

        if (newData.isEmpty()) {
            recycler.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        }

        final QuestionsListAdapter adapter = new QuestionsListAdapter(newData,
                new DataChangeListener() {
                    @Override
                    public void onDataChange(ArrayList<DbModel> data) {
                        PrefUtils.save(QuestionsListActivity.this, type, data);  //saves new data using shared preferences
                    }
                });
        recycler.setAdapter(adapter);


        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log("Proceed clicked");
                int earnedPoints = 0;
                int maxPoints = 0;
                if (newData.size() > 0) {
                    earnedPoints = adapter.getPointsEarnedByUser();
                    maxPoints = adapter.getMaxPointsThatCanBeEarned();
                }
                ScoreModel sm = new ScoreModel(earnedPoints, maxPoints, type); //score gets set for each page from here.
                PrefUtils.save(QuestionsListActivity.this, type, sm); //score gets saved until user goes to scoring page
                ArrayList<DbModel> temp = adapter.getUnattempted(); //for questions not answered
                PrefUtils.saveUnanswered(QuestionsListActivity.this,type,temp);
                String nextType = checkNext(type);
                log("Next type: " + nextType);

                if (nextType == null) {
                    Log.d("theH", "Time for final activity");
                    Intent i = new Intent(QuestionsListActivity.this, ResultActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(QuestionsListActivity.this, QuestionsListActivity.class);//repeat the same with the next page using intent
                    i.putExtra(Constants.QUESTION_TYPE_KEY, nextType); //gets to the next page if it exists.
                    startActivity(i); //repeats
                }
            }
        });
    }

    private String checkNext(String type) {
        DatabaseHelper db = new DatabaseHelper(QuestionsListActivity.this);
        ArrayList<String> allTypes = db.getAllTypes();
        int size = allTypes.size();
        for (int i = 0; i < size; i++) {
            if (allTypes.get(i).compareToIgnoreCase(type) == 0) { //ignoring the current page
                if (i + 1 < size) {
                    return allTypes.get(i + 1);
                }
            }
        }
        return null;
    }
}
