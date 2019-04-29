package com.technohack.movie_analytica.Models;

public class WatchedMoviePojo {

    private String title;
    private String overview;
    private String poster;
    private String release_date;
    private double rating;

    public WatchedMoviePojo() {
    }

    public WatchedMoviePojo(String title, String overview, String poster,String release_date, double rating) {
        this.title = title;
        this.overview = overview;
        this.poster = poster;
        this.release_date=release_date;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


}
