package com.project.mainapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.mainapp.R;
import com.project.mainapp.database.DatabaseHelper;
import com.project.mainapp.models.DbModel;
import com.project.mainapp.utils.Constants;

public class CreateQuestionActivity extends AppCompatActivity {

    private String type = null;
    EditText question, option_1, option_2, option_3, option_4, answer, et, et2, et3, et4, et5;
    Button submit;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        type = getIntent().getStringExtra(Constants.QUESTION_TYPE_KEY);

        initView();
    }

    private void initView() {
        submit = findViewById(R.id.submit);
        delete = findViewById(R.id.deleteQuestionBtn);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateQuestionActivity.this,DeleteQuestionActivity.class);
                i.putExtra(Constants.TYPE_KEY,type);
                startActivity(i);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                question = findViewById(R.id.question);
                option_1 = findViewById(R.id.option_1);
                option_2 = findViewById(R.id.option_2);
                option_3 = findViewById(R.id.option_3);
                //option_4 = findViewById(R.id.option_4);
                answer = findViewById(R.id.answer);
                et = findViewById(R.id.editText);
                et2 = findViewById(R.id.editText2);
                et3 = findViewById(R.id.editText3);
                et4 = findViewById(R.id.editText4);
               // et5 = findViewById(R.id.editText5);

                DbModel model = new DbModel(
                        "",
                        question.getText().toString(),
                        option_1.getText().toString(),
                        option_2.getText().toString(),
                        option_3.getText().toString(),
                        et.getText().toString(),
                        et2.getText().toString(),
                        et3.getText().toString(),
                        et4.getText().toString(),
                        type,
                        answer.getText().toString(),
                        0);

                if (answer.getText().toString().length() == 0) {
                    Toast toast = Toast.makeText(CreateQuestionActivity.this, "Please " +
                            "enter a ID before adding question!", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (question.getText().toString().length() == 0 || question.getText().toString().length() == 1) {
                    Toast toast = Toast.makeText(CreateQuestionActivity.this, "Please " +
                            "enter a question!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    DatabaseHelper db = new DatabaseHelper(CreateQuestionActivity.this);
                    db.insert(model);
                    Toast toast = Toast.makeText(getApplicationContext(), "Question added",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}
