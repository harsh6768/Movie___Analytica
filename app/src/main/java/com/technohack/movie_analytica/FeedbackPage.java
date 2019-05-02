package com.technohack.movie_analytica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class FeedbackPage extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText feedbackText;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressDialog;
    private Button saveFeedbackBtn;
    //private AppBarLayout appBarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_page);

        toolbar=findViewById(R.id.common_toolbarId);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Feedback");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseApp.initializeApp(this);
        firebaseFirestore=FirebaseFirestore.getInstance();

      //  appBarLayout=findViewById(R.id.common_appbarId);

        saveFeedbackBtn=findViewById(R.id.feedback_btnId);
        feedbackText=findViewById(R.id.feedback_editTextId);
       final String feeback=feedbackText.getText().toString().trim();

        feedbackText.setScroller(new Scroller(getApplicationContext()));
        feedbackText.setVerticalScrollBarEnabled(true);

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
        progressDialog.setMessage("Submitting Feedback ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //String  userName= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

        HashMap<String,Object> feedBackMap=new HashMap<>();
        feedBackMap.put("feedback",feedback);
        //feedBackMap.put("user_name",userName);


        //To save the feedback into the fireStore
        firebaseFirestore.collection("Feedback").add(feedBackMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

                if(task.isSuccessful()){
                    progressDialog.dismiss();

                    //getSupportActionBar().setTitle("Successfully Submitted");
                    //toolbar.setBackgroundColor(getResources().getColor(R.color.splash));
                    //feedbackText.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                    String feed="Thanks for your feedback";
                    //appBarLayout.setBackgroundColor(getResources().getColor(R.color.splash));
                    //Toast.makeText(FeedbackPage.this, "Thanks for your feedback", Toast.LENGTH_SHORT).show();
                    Toast toast = Toast.makeText(FeedbackPage.this, Html.fromHtml("<font color='#f5f6fa' ><b>" + feed + "</b></font>"), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    //startActivity(new Intent(FeedbackPage.this,HomePage.class));
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(FeedbackPage.this, "Try Again to give the feedback", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
