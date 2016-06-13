package com.example.karthick.popularmovies;

import android.app.Activity;
import android.content.Context;
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

        Context context = getContext();

        /*Recycle old view if possible*/
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_movie, parent, false);
        }
        Movie movie = getItem(position);
        String posterPath = movie.getPosterPath();
        final String IMAGE_BASE_URL = context.getString(R.string.mdb_image_baseUrl);
        String imageSizePath = context.getString(R.string.mdb_image_size_500);

        /*Load the image view by fetching image from picasso using the movie's poster path */
        ImageView posterImageView = (ImageView)convertView.findViewById(R.id.grid_item_movie_poster_imageView);
        String picassoPath = IMAGE_BASE_URL+imageSizePath+movie.getPosterPath();
        Picasso.with(context).load(picassoPath).into(posterImageView);

        return convertView;
    }
}

