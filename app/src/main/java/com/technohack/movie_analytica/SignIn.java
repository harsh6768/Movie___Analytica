package com.technohack.movie_analytica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.multidex.MultiDex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignIn extends AppCompatActivity {

     private  Button loginSignInBtn,loginReg;
     private TextInputEditText loginEmail,loginPass;

     private ProgressDialog progressDialog;
     private FirebaseAuth mAuth;

    private Toolbar toolbar;
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser!=null){

            Intent loginIntent=new Intent(SignIn.this,HomePage.class);
           // regSuccessIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

       // MultiDex.install(this);

        FirebaseApp.initializeApp(this);

        mAuth=FirebaseAuth.getInstance();

        toolbar=findViewById(R.id.common_toolbarId);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Sign-In User");

        loginEmail=findViewById(R.id.login_emailId);
        loginPass=findViewById(R.id.login_passId);
        loginReg=findViewById(R.id.login_regId);
        loginSignInBtn=findViewById(R.id.login_signInId);

        loginReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginRegIntent=new Intent(SignIn.this,Register.class);
                startActivity(loginRegIntent);
            }
        });

        loginSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUser();
            }
        });

    }

    private void signUser() {

        final String mEmail= Objects.requireNonNull(loginEmail.getText()).toString().trim();
        final String mPass= Objects.requireNonNull(loginPass.getText()).toString().trim();

        if(mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
            loginEmail.setError("Please enter valid email");
        }else if(mPass.isEmpty() || mPass.length()<6){
            loginPass.setError("Please enter valid password");
        }else{

            loginUser(mEmail,mPass);
        }

    }

    private void loginUser(String mEmail, String mPass) {

        //for progressbarDialog
        progressDialog= new ProgressDialog(SignIn.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sign-In Account ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    progressDialog.dismiss();
                    // Toast.makeText(Register.this, "Failed to register", Toast.LENGTH_SHORT).show();
                    Intent loginIntent=new Intent(SignIn.this,HomePage.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                    finish();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(SignIn.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
