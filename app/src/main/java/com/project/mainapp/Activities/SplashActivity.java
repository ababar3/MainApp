package com.project.mainapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.project.mainapp.R;
import com.project.mainapp.database.DatabaseHelper;
import com.project.mainapp.models.DbModel;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        button = (Button) findViewById(R.id.proceedbtn1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openClientInfo();
            }
        });

        DatabaseHelper db = new DatabaseHelper(this);

        if (db.getCount() == 0) {
            db.addType("Device Security");
            db.addType("Social Media");
            db.addType("Data Protection");
            db.addType("Network Security");
        }

        ArrayList<DbModel> data = db.getAll();
    }

    public void openClientInfo() {
        Intent intent = new Intent(this, ClientInfoActivity.class);
        startActivity(intent);
        this.finish();
    }
}
