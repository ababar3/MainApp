package com.project.mainapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.mainapp.R;

public class ClientInfoActivity extends AppCompatActivity {

    String st;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_info);


        final EditText editText1 = (EditText) findViewById(R.id.fullname);
        final EditText editText2 = (EditText) findViewById(R.id.email);
        final EditText editText3 = (EditText) findViewById(R.id.address);

        button = (Button) findViewById(R.id.buttonDS);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText1.getText().toString().length() == 0 || editText1.getText().toString().length() == 1) {
                    Toast toast = Toast.makeText(ClientInfoActivity.this, "Enter Your Full Name " +
                            "Before " +
                            "Submitting", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (editText2.getText().toString().length() == 0 || editText1.getText().toString().length() == 1) {
                    Toast toast = Toast.makeText(ClientInfoActivity.this, "Enter Your Email ID " +
                            "Before " +
                            "Submitting", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (editText3.getText().toString().length() == 0 || editText1.getText().toString().length() == 1) {
                    Toast toast = Toast.makeText(ClientInfoActivity.this, "Enter Your Address " +
                            "Before " +
                            "Submitting", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Intent i = new Intent(ClientInfoActivity.this, MainViewActivity.class);
                    st = editText1.getText().toString();
                    i.putExtra("Value", st);
                    startActivity(i);
                    finish();
                }
            }
        });

    }


//    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("EXIT", true);
//        startActivity(intent);
//        finish();
//    }


}
