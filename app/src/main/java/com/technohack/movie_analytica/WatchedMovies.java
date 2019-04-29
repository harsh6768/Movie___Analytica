package com.technohack.movie_analytica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.technohack.movie_analytica.Adapters.WatchedMovieAdapter;
import com.technohack.movie_analytica.Models.WatchedMoviePojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WatchedMovies extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WatchedMovieAdapter watchedMovieAdapter;
    private List<WatchedMoviePojo> watchedMoviePojoList;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watched_movies);

        firebaseFirestore=FirebaseFirestore.getInstance();
        toolbar=findViewById(R.id.common_toolbarId);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Watch List");


        recyclerView=findViewById(R.id.watched_recycler_viewId);
        watchedMoviePojoList=new ArrayList<>();
        watchedMovieAdapter=new WatchedMovieAdapter(this,watchedMoviePojoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(watchedMovieAdapter);

        fetchWatchedMovie();
    }

    private void fetchWatchedMovie() {

        //for progressbarDialog
        progressDialog= new ProgressDialog(WatchedMovies.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Watched Movies ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        firebaseFirestore.collection("WatchList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    progressDialog.dismiss();

                    for(QueryDocumentSnapshot doc: Objects.requireNonNull(task.getResult())){

                        Map<String ,Object>  watchedMovie=doc.getData();

                        String title=(String)watchedMovie.get("title");
                        String desc=(String) watchedMovie.get("desc");
                        String poster=(String) watchedMovie.get("poster");
                        String release_date=(String) watchedMovie.get("release_date");
                        double rating=(double) watchedMovie.get("rating");

                        WatchedMoviePojo watchedMoviePojo=new WatchedMoviePojo(title,desc,poster,release_date,rating);
                        watchedMoviePojoList.add(watchedMoviePojo);

                    }
                    watchedMovieAdapter.notifyDataSetChanged();

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(WatchedMovies.this, "Failed To Load the Movies", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
