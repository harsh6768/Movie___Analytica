package com.technohack.movie_analytica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.technohack.movie_analytica.Adapters.PopularMovieAdapter;
import com.technohack.movie_analytica.Models.PopularMoviePojo;
import com.technohack.movie_analytica.retrofit.TmdbApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private PopularMovieAdapter popularMovieAdapter;
    private Retrofit retrofit;
    private List<PopularMoviePojo> popularMoviePojoList;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        FirebaseApp.initializeApp(this);
        mAuth=FirebaseAuth.getInstance();

        toolbar=findViewById(R.id.toolbarId);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Popular Movies");

        //for drawer layout
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.home_navigationId);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //end of the code of drawer layOut

        popularMoviePojoList=new ArrayList<>();
        recyclerView=findViewById(R.id.home_recyclerViewId);
        popularMovieAdapter=new PopularMovieAdapter(this,popularMoviePojoList);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,LinearLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(popularMovieAdapter);

        //to fetch the data
        fetchMovieData();

    }

    /**
     * To Fetch the Popular Movies
     */

    private void fetchMovieData() {

        //for progressbarDialog
        progressDialog= new ProgressDialog(HomePage.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching Popular Movies ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        retrofit=new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TmdbApi tmdbApi=retrofit.create(TmdbApi.class);

        Call<PopularMovieResponse> popularMovieResponseCall=tmdbApi.getPopularMovie(Constants.API_KEY);

        popularMovieResponseCall.enqueue(new Callback<PopularMovieResponse>() {
            @Override
            public void onResponse(Call<PopularMovieResponse> call, Response<PopularMovieResponse> response) {

                if(response.body()!=null){

                    progressDialog.dismiss();

                    PopularMovieResponse popularMovieResponse=response.body();

                    List<PopularMovieResponse.ResultsBean>  resultsBeanList=popularMovieResponse.getResults();

                    for(PopularMovieResponse.ResultsBean resultsBean:resultsBeanList){

                        String movieTitle=resultsBean.getTitle();
                        String movieOverview=resultsBean.getOverview();
                        String moviePoster=resultsBean.getPoster_path();
                        String movieReleaseDate=resultsBean.getRelease_date();
                        double movieRating=resultsBean.getVote_average();
                        int id=resultsBean.getId();

                        PopularMoviePojo popularMoviePojo=new PopularMoviePojo(movieTitle,movieOverview,moviePoster,movieReleaseDate,movieRating,id);

                        popularMoviePojoList.add(popularMoviePojo);

                    }
                    popularMovieAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure(Call<PopularMovieResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.search_navId:
                startActivity(new Intent(HomePage.this,SearchMoviePage.class));
                return true;
                default:
                    return false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

          switch (item.getItemId()){
              case R.id.home_watch_movieId:
                  startActivity(new Intent(HomePage.this,WatchedMovies.class));
                  return true;
              case R.id.home_nav_logoutId:
                  mAuth.signOut();
                  startActivity(new Intent(HomePage.this,SignIn.class));
                  finish();

          }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
