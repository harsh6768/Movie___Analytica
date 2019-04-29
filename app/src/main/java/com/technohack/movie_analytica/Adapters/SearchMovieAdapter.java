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
import com.technohack.movie_analytica.PopularMoviesDetails;
import com.technohack.movie_analytica.R;

import java.util.List;

public class SearchMovieAdapter  extends RecyclerView.Adapter<SearchMovieAdapter.SearchMovieHolder> {

    private Context context;
    private List<SearchMoviePojo> searchMoviePojoList;

    public SearchMovieAdapter(Context context, List<SearchMoviePojo> searchMoviePojoList) {
        this.context = context;
        this.searchMoviePojoList = searchMoviePojoList;
    }

    @NonNull
    @Override
    public SearchMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.search_movie_single_item,parent,false);
        return new SearchMovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchMovieHolder holder, int position) {

        holder.title.setText(searchMoviePojoList.get(position).getTitle());
        holder.rating.setText(String.valueOf(searchMoviePojoList.get(position).getRating()));

        //call the method to set the image
        holder.onImagePoster(searchMoviePojoList.get(position).getPoster());

        final String title=searchMoviePojoList.get(position).getTitle();
        final String desc=searchMoviePojoList.get(position).getOverview();
        final String poster=searchMoviePojoList.get(position).getPoster();
        final double rating=searchMoviePojoList.get(position).getRating();
        final String release_date=searchMoviePojoList.get(position).getRelease_date();
        final int id=searchMoviePojoList.get(position).getId();


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
        return searchMoviePojoList.size();
    }

    public class SearchMovieHolder  extends RecyclerView.ViewHolder{

        TextView title,rating;
        ImageView poster;
        CardView cardView;
        public SearchMovieHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.search_titleId);
            rating=itemView.findViewById(R.id.search_ratingId);
            poster=itemView.findViewById(R.id.search_imageId);
            cardView=itemView.findViewById(R.id.search_movie_cardId);

        }
        public void onImagePoster(String imagePath){

            //placeholder image
            Glide.with(context).load("https://image.tmdb.org/t/p/w500/"+imagePath).into(poster);
        }
    }
}
