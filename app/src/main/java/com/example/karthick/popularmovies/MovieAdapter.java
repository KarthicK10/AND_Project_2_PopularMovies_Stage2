package com.example.karthick.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.karthick.popularmovies.domain.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by KarthicK on 6/6/2016.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    /*View holder to store references to views
     *  saves resource spent on calls to findViewById calls
     *  helps smooth scrolling*/
    private static class MovieViewHolder{
        ImageView posterImageView;
    }

    public MovieAdapter(Activity context, List<Movie> moviesList){
        super(context, 0, moviesList);
    }

    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.i(LOG_TAG, "view asked");

        MovieViewHolder movieViewHolder;

        Context context = getContext();

        /*Recycle old view if possible*/
        if(convertView == null){
            movieViewHolder = new MovieViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_movie, parent, false);
            movieViewHolder.posterImageView = (ImageView)convertView.findViewById(R.id.grid_item_movie_poster_imageView);
            convertView.setTag(movieViewHolder);
            Log.i(LOG_TAG, "view created");
        }
        else{
            Log.i(LOG_TAG, "view recycled");
            movieViewHolder = (MovieViewHolder)convertView.getTag();
        }
        Movie movie = getItem(position);
        String posterPath = movie.getPosterPath();
        final String IMAGE_BASE_URL = context.getString(R.string.mdb_image_baseUrl);
        String imageSizePath = context.getString(R.string.mdb_image_size_342);

        /*Load the image view by fetching image from picasso using the movie's poster path */
        //ImageView posterImageView = (ImageView)convertView.findViewById(R.id.grid_item_movie_poster_imageView);
        String picassoPath = IMAGE_BASE_URL+imageSizePath+movie.getPosterPath();
        Picasso.with(context).load(picassoPath).into(movieViewHolder.posterImageView);

        Log.i(LOG_TAG, "view returned");

        return convertView;


    }
}

