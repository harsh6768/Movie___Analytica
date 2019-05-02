package com.technohack.movie_analytica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.technohack.movie_analytica.Models.ProfileDataModel;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfilePage extends AppCompatActivity {

    public static final int GALLERY=100;
    private CircleImageView profileImage;
    private EditText userName,userEmail;
    private Button saveProfileBtn;
    private ProgressDialog progressDialog=null;
    private Uri imageUri=null;
    private FirebaseFirestore firestore;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_page);

        FirebaseApp.initializeApp(this);
        firestore=FirebaseFirestore.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        profileImage=findViewById(R.id.profile_imageId);
        userName=findViewById(R.id.profile_user_nameId);
        userEmail=findViewById(R.id.profile_user_emailId);
        saveProfileBtn=findViewById(R.id.profile_btnId);


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //check if permission granted or not
                    if (ContextCompat.checkSelfPermission(ViewProfilePage.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(ViewProfilePage.this, "Permission Denied!!!", Toast.LENGTH_LONG).show();

                        ActivityCompat.requestPermissions(ViewProfilePage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {
                        //if permission granted then we need to set the image
                        setProfileImage();
                    }
                } else {

                    //android version is less than 6.0
                    setProfileImage();

                }
            }
        });


        //store profile data
        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

     }

    private void saveProfile() {

       final String mName=userName.getText().toString().trim();
       final  String mEmail=userEmail.getText().toString().trim();
      if(imageUri==null){
          Toast.makeText(this, "Provide Profile Image", Toast.LENGTH_SHORT).show();
      }else if(mName.isEmpty()){
          userName.setError("Please Enter Your Username");
      }else if(mEmail.isEmpty()){
          userEmail.setError("Please Enter Your Email");
      }else{

          storeIntoFireStore(mName,mEmail,imageUri);
      }

    }

    private void storeIntoFireStore(final String mName, final String mEmail, final Uri imageUri) {


        //for progressbarDialog
        progressDialog= new ProgressDialog(ViewProfilePage.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Saving Details ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Map<String,Object> profileData=new HashMap<>();
        profileData.put("image",imageUri);
        profileData.put("name",mName);
        profileData.put("email",mEmail);

        firestore.collection("ProfileDetails").add(profileData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

                if(task.isSuccessful()){

                    StorageReference storageReference=FirebaseStorage.getInstance().getReference();
                    //setting the path where we want to upload the images
                    final StorageReference fileToUpload=storageReference.child("Images/"+mName);
                  /*  fileToUpload.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            if(taskSnapshot!=null){
                                fileToUpload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        if(uri!=null){

                                            progressDialog.dismiss();

                                            String imageUrl=uri.toString();
                                            ProfileDataModel profileDataModel=new ProfileDataModel(mName,mEmail,imageUrl);

                                            Toast.makeText(ViewProfilePage.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                                        }else{
                                            progressDialog.dismiss();
                                            Toast.makeText(ViewProfilePage.this, "Failed to Upload Image", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            }
                        }
                    });
                    */

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(ViewProfilePage.this, "Failed to Update Profile", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    //set profileImage
    private void setProfileImage() {
        Intent setImage=new Intent(Intent.ACTION_PICK);
        setImage.setType("image/*");
        startActivityForResult(setImage,GALLERY);

    }
    //set the image to the circleImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY){

            imageUri = data.getData();
            Log.i("TAG", "onActivityResult: "+imageUri);
            profileImage.setImageURI(imageUri);

        }
    }

}
