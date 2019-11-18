package com.project.mainapp.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mainapp.R;
import com.project.mainapp.adapters.ResultAdapter;
import com.project.mainapp.adapters.UnattemptedQuestionAdapter;
import com.project.mainapp.database.DatabaseHelper;
import com.project.mainapp.models.DbModel;
import com.project.mainapp.models.ScoreModel;
import com.project.mainapp.models.UnattemptedModel;
import com.project.mainapp.utils.PrefUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static java.io.File.separator;

public class ResultActivity extends AppCompatActivity {

    private final int STORAGE_REQUEST_CODE = 1693;

    private Button createPdfBtn;
    private RecyclerView recyclerView, remQuesRecycler;
    private ProgressBar bar;
    private TextView scoreText, notesTextView;
    private NestedScrollView root;

    private DatabaseHelper db;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }

    @Override
    protected void onResume() {
        super.onResume();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Creating image");
        progressDialog.dismiss();

        remQuesRecycler = findViewById(R.id.rvRemQuestions);
        remQuesRecycler.setLayoutManager(new GridLayoutManager(this,2));


        notesTextView = findViewById(R.id.notesTextView);

        notesTextView.setVisibility(View.GONE);

        createPdfBtn = findViewById(R.id.createPdfBtn);
        bar = findViewById(R.id.progressBarMain);
        scoreText = findViewById(R.id.points_tv);
        root = findViewById(R.id.rootLayout);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this,
//                LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); //horizontal fit 3 inflators

        db = new DatabaseHelper(this);
        ArrayList<ScoreModel> data = new ArrayList<>();

        ArrayList<String> types = db.getAllTypes();

        int totalMaxScore = 0;
        int totalEarnedScore = 0;

        ArrayList<UnattemptedModel> adapterData = new ArrayList<>(); //list would hold unanswered questions

        for (String singleType : types) {
            ScoreModel model = PrefUtils.fetch(this, singleType);  //for a page if exists
            ArrayList<DbModel> oldRemainingQuestions = PrefUtils.fetchUnanswered(this, singleType); //for same page we get unanswered questions
            if (oldRemainingQuestions != null && !oldRemainingQuestions.isEmpty()) {
                UnattemptedModel tempModel = new UnattemptedModel();
                tempModel.type = singleType;
                tempModel.setQuestions(oldRemainingQuestions);
                adapterData.add(tempModel); //unanswered question get stored here.
            }
            if (model == null) {
                model = new ScoreModel(0, 0, singleType);
            }
            totalMaxScore += model.totalScore;
            totalEarnedScore += model.earnedScore;
            data.add(model);
        }

        UnattemptedQuestionAdapter remQuesAdapter = new UnattemptedQuestionAdapter(adapterData);
        remQuesRecycler.setNestedScrollingEnabled(false);

        remQuesRecycler.setAdapter(remQuesAdapter);

        remQuesRecycler.setVisibility(View.GONE);

        ResultAdapter adapter = new ResultAdapter(data);
        recyclerView.setAdapter(adapter);

        if (totalEarnedScore <= 0) {
            scoreText.setText("0");
            bar.setProgress(0);
        } else {
            float prog = ((float) totalEarnedScore) / ((float) totalMaxScore) * 100;
            int progress = Math.round(prog);
            if (progress > 100) {
                progress = 100;
            }
            scoreText.setText(String.valueOf(progress));
            bar.setProgress(progress);
        }
        createPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermissions()) {
                    requestPermissions();
                } else {
                    createPdf();
                }
            }
        });

    }

    public boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_REQUEST_CODE && checkPermissions()) {
            new AlertDialog.Builder(this).setTitle("Permission not given")
                    .setMessage("Image creation process requires permission for storage. Please " +
                            "grant the permission for creating pdf").setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
        } else {
            createPdf();
        }
    }

    private void createPdf() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Notes:");

        // Set up the input
        final EditText input = new EditText(this);
        input.setHint("Enter here");
        // Specify the type of input expected; this, for example, sets the input as a password,
        // and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String notes = input.getText().toString();
                if (!notes.isEmpty()) {
                    notesTextView.setText(notes);
                }

                getPdfName();
            }
        });

        builder.show();
    }

    private void onImageCreated(boolean wasTaskSuccessful) {
        progressDialog.dismiss();
        notesTextView.setVisibility(View.GONE);
        createPdfBtn.setVisibility(View.VISIBLE);
        remQuesRecycler.setVisibility(View.GONE);
        Toast.makeText(this, "Task status: " + (wasTaskSuccessful ? "successful" : "failed"),
                Toast.LENGTH_SHORT).show();
        Log.d("theH", "Task status: " + wasTaskSuccessful);
    }

    private void layoutToImage(String pdfFileName) {
        // get view group using reference
        NestedScrollView snapshot = root;
        int height = 0;
        int childCount = snapshot.getChildCount();
        for (int i = 0; i < childCount; i++) {
            height += snapshot.getChildAt(i).getHeight();
        }
//        Bitmap bitmap = Bitmap.createBitmap(snapshot.getWidth(),
//                snapshot.getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap _bitmap = Bitmap.createBitmap(snapshot.getWidth(),
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(_bitmap);
        Drawable bgDrawable = snapshot.getBackground();
        // convert view group to bitmap
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        snapshot.draw(canvas);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        _bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        final File f =
                new File(Environment.getExternalStorageDirectory().getAbsolutePath() + separator + pdfFileName + ".jpg");
        boolean check;
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            check = true;
        } catch (IOException e) {
            check = false;
            e.printStackTrace();
        }

        final boolean finalCheck = check;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onImageCreated(finalCheck);
            }
        });
    }

    private void getPdfName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Enter pdf name:");

        // Set up the input
        final EditText input = new EditText(this);
        input.setHint("Enter here");
        // Specify the type of input expected; this, for example, sets the input as a password,
        // and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String pdfFileName = input.getText().toString();
                if (pdfFileName.isEmpty()) {
                    Toast.makeText(ResultActivity.this, "Please enter the file name to proceed",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.show();


                notesTextView.setVisibility(View.VISIBLE);
                remQuesRecycler.setVisibility(View.VISIBLE);
                createPdfBtn.setVisibility(View.GONE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                        }
                        layoutToImage(pdfFileName);
                    }
                }).start();
            }
        });

        builder.show();
    }


    @Override
    public void onBackPressed() {
        ArrayList<String> allTypes = db.getAllTypes();
        PrefUtils.clearLists(this, allTypes);
        PrefUtils.clearQuestions(this, allTypes);
        PrefUtils.clearUnattempted(this, allTypes);
        this.finish();
    }
}
