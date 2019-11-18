package com.project.mainapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mainapp.R;
import com.project.mainapp.adapters.AdminControlAdapter;
import com.project.mainapp.database.DatabaseHelper;
import com.project.mainapp.interfaces.SimpleCallback;
import com.project.mainapp.utils.Constants;
import com.project.mainapp.utils.PrefUtils;

public class AdminControlActivity extends AppCompatActivity {

    private RecyclerView adminControlRecycler;
    private Button addBtn, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_control);

        adminControlRecycler = findViewById(R.id.adminControlRecycler);
        addBtn = findViewById(R.id.addBtn);
        delete = findViewById(R.id.deleteBtn);
        adminControlRecycler.setLayoutManager(new LinearLayoutManager(this));

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteType();
            }
        });

    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Add new field:");

        // Set up the input
        final EditText input = new EditText(this);
        input.setHint("Enter here");
        // Specify the type of input expected; this, for example, sets the input as a password,
        // and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String data = input.getText().toString();
                if (data.isEmpty()) {
                    Toast.makeText(AdminControlActivity.this, "Please enter the type name!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        DatabaseHelper db = new DatabaseHelper(AdminControlActivity.this);
                        db.addType(data);
                        resumeProcess();
                    } catch (TypeAlreadyExistsException ignored) {
                        Toast.makeText(AdminControlActivity.this, "Type already exists so was not" +
                                " added.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        resumeProcess();
    }

    private void resumeProcess() {
        DatabaseHelper db = new DatabaseHelper(this);
        AdminControlAdapter adapter = new AdminControlAdapter(db.getAllTypes(),
                new SimpleCallback() {
                    @Override
                    public void onClick(String value) {
                        Intent i = new Intent(AdminControlActivity.this,
                                CreateQuestionActivity.class);
                        i.putExtra(Constants.QUESTION_TYPE_KEY, value);
                        startActivity(i);
                    }
                });
        adminControlRecycler.setAdapter(adapter);
    }

    private void deleteType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Please enter the name you want to delete:");

        // Set up the input
        final EditText input = new EditText(this);
        input.setHint("Enter here");
        // Specify the type of input expected; this, for example, sets the input as a password,
        // and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String data = input.getText().toString();
                if (data.isEmpty()) {
                    Toast.makeText(AdminControlActivity.this, "Please enter the type name!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseHelper db = new DatabaseHelper(AdminControlActivity.this);
                    String deleteResponse = db.deleteType(data);
                    if (deleteResponse != null) {
                        Toast.makeText(AdminControlActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        PrefUtils.clearSingleList(AdminControlActivity.this, deleteResponse);
                        PrefUtils.clearQuestions(AdminControlActivity.this, deleteResponse);
                        resumeProcess();
                    } else {
                        Toast.makeText(AdminControlActivity.this, "Could not find '" + data + "'"
                                , Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
