package com.technohack.movie_analytica;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.technohack.movie_analytica.retrofit.TmdbApi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PopularMoviesDetails extends AppCompatActivity {
    public static final String RELEASE_DATE="Release Date:";
    public static final String RATINGS="Ratings:";
    public static final String HOURS="hrs";
    private ImageView moviePoster;
    private TextView movieDesc,movieReleaseDate,movieLength;
    private RatingBar movieRating;
    private ProgressDialog progressDialog;
    int id=0;
    private Retrofit retrofit;

    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    String title,poster,desc,release_date;
    double rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //for getting the full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_popular_movies_details);

        Intent popularMovie=getIntent();
        title=popularMovie.getExtras().getString("title");
        poster=popularMovie.getExtras().getString("poster");
        desc=popularMovie.getExtras().getString("desc");
        rating=popularMovie.getExtras().getDouble("rating");
        release_date=popularMovie.getExtras().getString("release_date");
        id=popularMovie.getExtras().getInt("id");


        FirebaseApp.initializeApp(this);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(title);

        moviePoster=findViewById(R.id.imageId);
        movieDesc=findViewById(R.id.popular_movie_details_descId);
        movieReleaseDate=findViewById(R.id.popular_movie_details_release_dateId);
        movieRating=findViewById(R.id.popular_movie_details_ratingId);
        FloatingActionButton fab = findViewById(R.id.fab);

        //Setting the values
        Glide.with(this).load("https://image.tmdb.org/t/p/w500/"+poster).into(moviePoster);
        movieDesc.setText(desc);
        movieReleaseDate.setText(String.format("%s%s\n%s%s", RELEASE_DATE, release_date, RATINGS,rating));
        movieRating.setRating((float) rating);

        //to add the movie
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMovieIntoWatchList(view);
            }
        });
    }

    private void addMovieIntoWatchList(final View view) {

        //for progressbarDialog
        progressDialog= new ProgressDialog(PopularMoviesDetails.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding Into WatchList ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Map<String ,Object> movieWatchList=new HashMap<>();
        movieWatchList.put("title",title);
        movieWatchList.put("desc",desc);
        movieWatchList.put("poster",poster);
        movieWatchList.put("rating",rating);
        movieWatchList.put("release_date",release_date);
        movieWatchList.put("time_stamp", FieldValue.serverTimestamp());

        String currentUserId= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String currentUserName=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();


        firebaseFirestore.collection("WatchList").add(movieWatchList).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(view, "Movie Added Successfully into WatchList", Snackbar.LENGTH_LONG);
                    ColoredSnackbar.confirm(snackbar).show();

                }else{
                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(view, "Movie Added Successfully into WatchList", Snackbar.LENGTH_LONG);
                    ColoredSnackbar.alert(snackbar).show();
                }
            }
        });
    }

    private void onPopularMovieDetails() {

        retrofit=new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TmdbApi tmdbApi=retrofit.create(TmdbApi.class);

        Call<PopularMoviesDetailsResponse> moviesDetailsResponseCall=tmdbApi.getPopularMovieDetails(id,Constants.API_KEY);

        moviesDetailsResponseCall.enqueue(new Callback<PopularMoviesDetailsResponse>() {
            @Override
            public void onResponse(Call<PopularMoviesDetailsResponse> call, Response<PopularMoviesDetailsResponse> response) {

              PopularMoviesDetailsResponse moviesDetailsResponse=response.body();

                assert moviesDetailsResponse != null;
                float runTime=moviesDetailsResponse.getRuntime();
             //  float time= (float) (runTime/60.0);
             //  movieLength.setText(String.format("%s%s", String.valueOf(runTime), HOURS));

                PopularMoviesDetailsResponse.VideosBean videosBean=moviesDetailsResponse.getVideos();

                assert videosBean != null;
                List<PopularMoviesDetailsResponse.VideosBean.ResultsBean> resultsBeanList=videosBean.getResults();

               // for (PopularMoviesDetailsResponse.VideosBean.ResultsBean resultsBean:resultsBeanList){ }
            }

            @Override
            public void onFailure(Call<PopularMoviesDetailsResponse> call, Throwable t) {

            }
        });
    }
}
