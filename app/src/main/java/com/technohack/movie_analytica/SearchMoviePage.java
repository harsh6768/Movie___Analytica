package com.technohack.movie_analytica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.technohack.movie_analytica.Adapters.SearchMovieAdapter;
import com.technohack.movie_analytica.Models.SearchMoviePojo;
import com.technohack.movie_analytica.retrofit.TmdbApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchMoviePage extends AppCompatActivity {

    //private Toolbar toolbar;
    private SearchMovieAdapter searchMovieAdapter;
    private RecyclerView recyclerView;
    private Retrofit retrofit;
    private List<SearchMoviePojo> searchMoviePojoList;
    private EditText searchText;
    private ProgressDialog progressDialog;
    private ImageView backBtnImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie_page);

        recyclerView=findViewById(R.id.search_recyclerId);
        searchText=findViewById(R.id.search_editTextId);
        backBtnImage=findViewById(R.id.back_btnImageId);

        searchMoviePojoList=new ArrayList<>();
        searchMovieAdapter=new SearchMovieAdapter(this,searchMoviePojoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(searchMovieAdapter);

       backBtnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchMoviePage.this,HomePage.class));
            }
        });

        //when we click keypad's search button or done button it will search the movie
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || actionId==EditorInfo.IME_ACTION_GO) {

                    SearchMoviePage.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    String movieName=searchText.getText().toString();
                    searchMovie(movieName);
                }
                return false;
            }
        });

    }

    //to search the movie
    public void searchMovie(String movieName) {
        //for progressbarDialog
        progressDialog= new ProgressDialog(SearchMoviePage.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching Movies ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        retrofit=new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TmdbApi tmdbApi=retrofit.create(TmdbApi.class);

        Log.i("TAG", "onClick: inside method"+movieName);

        Call<MovieSearchResponse> searchResponseCall=tmdbApi.getResultFromSearchMovie(Constants.API_KEY,movieName);

        searchResponseCall.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
             if(response.body()!=null){

                progressDialog.dismiss();

               MovieSearchResponse movieSearchResponse=response.body();

              List<MovieSearchResponse.ResultsBean> resultsBeanList=movieSearchResponse.getResults();
              searchMoviePojoList.clear();
              for(MovieSearchResponse.ResultsBean resultsBean:resultsBeanList){

                  String title=resultsBean.getTitle();
                  String desc=resultsBean.getOverview();
                  double rating=resultsBean.getVote_average();
                  int id=resultsBean.getId();
                  String poster=resultsBean.getPoster_path();
                  String release_date=resultsBean.getRelease_date();

                  searchMoviePojoList.add(new SearchMoviePojo(title,desc,poster,release_date,rating,id));

              }

              searchMovieAdapter.notifyDataSetChanged();

             }

            }
            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {

            }
        });

    }
}
