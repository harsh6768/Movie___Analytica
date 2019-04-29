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
import com.technohack.movie_analytica.PopularMoviesDetails;
import com.technohack.movie_analytica.R;

import java.util.List;

public class PopularMovieAdapter  extends RecyclerView.Adapter<PopularMovieAdapter.PopularMovieHolder> {

    private Context context;
    private List<PopularMoviePojo> popularMoviePojoList;

    public PopularMovieAdapter(Context context, List<PopularMoviePojo> popularMoviePojoList) {
        this.context = context;
        this.popularMoviePojoList = popularMoviePojoList;
    }

    @NonNull
    @Override
    public PopularMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.popular_movie_single_item,parent,false);
       return new PopularMovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularMovieHolder holder, int position) {

        holder.title.setText(popularMoviePojoList.get(position).getTitle());
        holder.rating.setText(String.valueOf(popularMoviePojoList.get(position).getRating()));

        //call the method to set the image
        holder.onImagePoster(popularMoviePojoList.get(position).getPoster());

        final String title=popularMoviePojoList.get(position).getTitle();
        final String desc=popularMoviePojoList.get(position).getOverview();
        final String poster=popularMoviePojoList.get(position).getPoster();
        final double rating=popularMoviePojoList.get(position).getRating();
        final String release_date=popularMoviePojoList.get(position).getRelease_date();
        final int id=popularMoviePojoList.get(position).getId();


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* //to pass the value for displaying the details in the another activity
                SharedPreferences sharedPreferences=context.getSharedPreferences("PopularMovie",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("title",title);
                editor.putString("desc",desc);
                editor.putString("poster",poster);
                editor.putString("release_date",release_date);
                editor.putString("rating", String.valueOf(rating));
                editor.putInt("id",id);

                editor.apply();
 */
                Intent popularMovieIntent=new Intent(context, PopularMoviesDetails.class);
                popularMovieIntent.putExtra("title",title);
                popularMovieIntent.putExtra("desc",desc);
                popularMovieIntent.putExtra("poster",poster);
                popularMovieIntent.putExtra("rating",rating);
                popularMovieIntent.putExtra("release_date",release_date);
                popularMovieIntent.putExtra("id",id);

                context.startActivity(popularMovieIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return popularMoviePojoList.size();
    }

    public class PopularMovieHolder  extends RecyclerView.ViewHolder{

         TextView title,rating;
         ImageView poster;
         CardView cardView;
        public PopularMovieHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.popular_titleId);
            rating=itemView.findViewById(R.id.popular_ratingId);
            poster=itemView.findViewById(R.id.popular_imageId);
            cardView=itemView.findViewById(R.id.popular_movie_cardId);

        }
        public void onImagePoster(String imagePath){

            //placeholder image
            Glide.with(context).load("https://image.tmdb.org/t/p/w500/"+imagePath).into(poster);
        }
    }
}
