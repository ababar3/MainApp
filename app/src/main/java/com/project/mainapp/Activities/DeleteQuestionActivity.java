package com.project.mainapp.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mainapp.R;
import com.project.mainapp.adapters.DeleteQuestionAdapter;
import com.project.mainapp.database.DatabaseHelper;
import com.project.mainapp.interfaces.LongClickCallback;
import com.project.mainapp.models.DbModel;
import com.project.mainapp.utils.Constants;

import java.util.ArrayList;

public class DeleteQuestionActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private String type;
    private DatabaseHelper db;
    private DeleteQuestionAdapter adapter;
    private TextView tv_noQuesAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_question);

        db = new DatabaseHelper(this);
        type = getIntent().getStringExtra(Constants.TYPE_KEY);

        showInfoDialog();

        ArrayList<DbModel> questions = db.getAllFromType(type);


        tv_noQuesAdded = findViewById(R.id.noQuestionsAddedTv);
        recycler = findViewById(R.id.deleteRecycler);
        adapter = new DeleteQuestionAdapter(questions,
                new LongClickCallback() {
                    @Override
                    public void onLongClick(DbModel item) {
                        showDialogToConfirmDelete(item);
                    }
                });

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        if (questions.isEmpty()) {
            recycler.setVisibility(View.GONE);
            tv_noQuesAdded.setVisibility(View.VISIBLE);
        } else {
            recycler.setVisibility(View.VISIBLE);
            tv_noQuesAdded.setVisibility(View.GONE);
        }
    }

    private void showInfoDialog() {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle("Info")
                .setMessage("Press on a row to delete it.")
                .setPositiveButton("Okay",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
    }

    private void showDialogToConfirmDelete(final DbModel item) {
        String sb = "Are you sure you want to delete the following question: \n\n" +
                item.question + "\n" +
                item.option1 + "\n" +
                item.option2 + "\n" +
                item.option3 + "\n" ;

        new AlertDialog.Builder(this)
                .setTitle("Delete Questions")
                .setMessage(sb)
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (db.deleteQuestion(item.id)) {
                            showToast("Deleted questions successfully");
                        } else {
                            showToast("Deleted questions successfully");
                        }

                        ArrayList<DbModel> questions = db.getAllFromType(type);
                        adapter.swapData(questions);

                        if (questions.isEmpty()) {
                            showToast("No question to show");
                        }
                    }
                })
                .setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
