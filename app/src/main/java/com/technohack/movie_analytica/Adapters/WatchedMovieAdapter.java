package com.technohack.movie_analytica.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technohack.movie_analytica.Models.PopularMoviePojo;
import com.technohack.movie_analytica.Models.SearchMoviePojo;
import com.technohack.movie_analytica.Models.WatchedMoviePojo;
import com.technohack.movie_analytica.PopularMoviesDetails;
import com.technohack.movie_analytica.R;

import java.util.List;

public class WatchedMovieAdapter extends RecyclerView.Adapter<WatchedMovieAdapter.WatchedMovieHolder> {

    private Context context;
    private List<WatchedMoviePojo> watchedMoviePojoList;

    public WatchedMovieAdapter(Context context, List<WatchedMoviePojo> watchedMoviePojoList) {
        this.context = context;
        this.watchedMoviePojoList = watchedMoviePojoList;
    }

    @NonNull
    @Override
    public WatchedMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.single_watched_movies,parent,false);
        return new WatchedMovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchedMovieHolder holder, int position) {

        holder.title.setText(watchedMoviePojoList.get(position).getTitle());
        holder.rating.setText(String.valueOf(watchedMoviePojoList.get(position).getRating()));

        //call the method to set the image
        holder.onImagePoster(watchedMoviePojoList.get(position).getPoster());

        final String title=watchedMoviePojoList.get(position).getTitle();
        final String desc=watchedMoviePojoList.get(position).getOverview();
        final String poster=watchedMoviePojoList.get(position).getPoster();
        final double rating=watchedMoviePojoList.get(position).getRating();
        final String release_date=watchedMoviePojoList.get(position).getRelease_date();
        //final int id=watchedMoviePojoList.get(position).getId();


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent watchedMovieIntent=new Intent(context, PopularMoviesDetails.class);
                watchedMovieIntent.putExtra("title",title);
                watchedMovieIntent.putExtra("desc",desc);
                watchedMovieIntent.putExtra("poster",poster);
                watchedMovieIntent.putExtra("rating",rating);
                watchedMovieIntent.putExtra("release_date",release_date);
                //popularMovieIntent.putExtra("id",id);

                context.startActivity( watchedMovieIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return watchedMoviePojoList.size();
    }

    public class WatchedMovieHolder  extends RecyclerView.ViewHolder{

        TextView title,rating;
        ImageView poster;
        CardView cardView;
        public WatchedMovieHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.watched_movie_titleId);
            rating=itemView.findViewById(R.id.watched_movie_ratingId);
            poster=itemView.findViewById(R.id.watched_movie_imageId);
            cardView=itemView.findViewById(R.id.watched_movie_cardId);

        }
        public void onImagePoster(String imagePath){

            //placeholder image
            Glide.with(context).load("https://image.tmdb.org/t/p/w500/"+imagePath).into(poster);
        }
    }
}
