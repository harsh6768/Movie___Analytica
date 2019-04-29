package com.technohack.movie_analytica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class FeedbackPage extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText feedbackText;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressDialog;
    private Button saveFeedbackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_page);

        toolbar=findViewById(R.id.common_toolbarId);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Feedback");

        FirebaseApp.initializeApp(this);
        firebaseFirestore=FirebaseFirestore.getInstance();

        saveFeedbackBtn=findViewById(R.id.feedback_btnId);
        feedbackText=findViewById(R.id.feedback_editTextId);
        final String feeback=feedbackText.getText().toString().trim();


        saveFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFeedback(feeback);
            }
        });

    }

    private void saveFeedback( String feedback) {

        //for progressbarDialog
        progressDialog= new ProgressDialog(FeedbackPage.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching Popular Movies ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        firebaseFirestore.collection("Feedback").add(feedback).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(FeedbackPage.this, "Thanks for your feedback", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(FeedbackPage.this, "Try Again to give the feedback", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
