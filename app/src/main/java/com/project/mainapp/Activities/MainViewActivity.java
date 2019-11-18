package com.project.mainapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mainapp.R;
import com.project.mainapp.adapters.MainViewAdapter;
import com.project.mainapp.database.DatabaseHelper;
import com.project.mainapp.interfaces.SimpleCallback;
import com.project.mainapp.utils.Constants;

import java.util.ArrayList;

public class MainViewActivity extends AppCompatActivity {

    TextView tv;
    String st;

    private RecyclerView mainViewRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        setContentView(R.layout.activity_main_view);


        mainViewRecycler = findViewById(R.id.mainViewRecycler);

        tv = findViewById(R.id.textView2);

        st = getIntent().getExtras().getString("Value");

        tv.setText(st);


        mainViewRecycler.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();

        DatabaseHelper db = new DatabaseHelper(this);
        ArrayList<String> data = db.getAllTypes(); //ARRAYLIST HAS ALL CREATED PAGES
        data.add("Admin Control"); //We manually add Admin Control Page
        MainViewAdapter adapter = new MainViewAdapter(data, new SimpleCallback() {
            @Override
            public void onClick(String value) {
                if(value.compareTo("Admin Control") == 0){
                    Intent i =new Intent(MainViewActivity.this,AdminControlActivity.class);
                    startActivity(i);
                }
                else{
                    Intent i =new Intent(MainViewActivity.this,QuestionsListActivity.class);
                    i.putExtra(Constants.QUESTION_TYPE_KEY,value);
                    startActivity(i);
                }
            }
        });
        mainViewRecycler.setAdapter(adapter);
    }

    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ClientInfoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }
}
