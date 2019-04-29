package com.technohack.movie_analytica.retrofit;

import com.technohack.movie_analytica.MovieSearchResponse;
import com.technohack.movie_analytica.PopularMovieResponse;
import com.technohack.movie_analytica.PopularMoviesDetailsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbApi {


    @GET("movie/popular")
    Call<PopularMovieResponse> getPopularMovie(
            @Query("api_key") String apiKey
    );

    //for searching the movie


   //https://api.themoviedb.org/3/search/movie?api_key=2d3e3995bfdaf5c190e7abac26e49040&query=the+avengers

    @GET("search/movie")
    Call<MovieSearchResponse> getResultFromSearchMovie(
            @Query("api_key") String apiKey,
            @Query("query") String movie_name
    );


    //for calling the api using specific movie id
//https://api.themoviedb.org/3/movie/299534?api_key=2d3e3995bfdaf5c190e7abac26e49040&append_to_response=videos
    @GET("movie/{movie_id}")
    Call<PopularMoviesDetailsResponse> getPopularMovieDetails(

            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey

    );


}
