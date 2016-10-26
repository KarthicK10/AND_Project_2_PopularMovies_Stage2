package com.example.karthick.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.example.karthick.popularmovies.R;
import com.example.karthick.popularmovies.data.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KarthicK on 6/6/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {


    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    /*View holder to store references to views
     *  saves resource spent on calls to findViewById calls
     *  helps smooth scrolling*/
    public static class MovieViewHolder extends RecyclerView.ViewHolder{
        ImageView posterImageView;
        public MovieViewHolder(View entireView){
            super(entireView);
            posterImageView = (ImageView)entireView.findViewById(R.id.grid_item_movie_poster_imageView);
        }
    }

    private List<Movie> mMoviesList = new ArrayList<>();
    private Context mContext;
    private static String IMAGE_BASE_URL;
    private static String IMAGE_SIZE_PATH;

    public MovieAdapter(List<Movie> movieList, Context context){
        mMoviesList = movieList;
        mContext = context;
        IMAGE_BASE_URL = mContext.getString(R.string.mdb_image_baseUrl);
        IMAGE_SIZE_PATH = mContext.getString(R.string.mdb_image_size_154);
    }

    /**
     * Called when RecyclerView needs a new {@link MovieViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(MovieViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(MovieViewHolder, int)
     */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //Inflate the custom layout
        View movieView = layoutInflater.inflate(R.layout.grid_item_movie, parent, false);

        //Return a view holder instance
        MovieViewHolder movieViewHolder = new MovieViewHolder(movieView);
        return movieViewHolder;
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link MovieViewHolder#posterImageView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link MovieViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     *
     * @param movieViewHolder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieViewHolder movieViewHolder, int position) {
        //Get the data model based on position
        Movie movie = mMoviesList.get(position);
        String posterPath = movie.getPosterPath();


        /*Load the image view by fetching image from picasso using the movie's poster path */
        String imagePath = IMAGE_BASE_URL+IMAGE_SIZE_PATH+movie.getPosterPath();

        //Set the views based on the data model
        ImageView posterImageView = movieViewHolder.posterImageView;
        AQuery aq = new AQuery(posterImageView);
        aq.id(R.id.grid_item_movie_poster_imageView).image(imagePath);
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    /*
     * Returns the item from a specified position
     *
     * @param - The position to retrieve from
     * @return - The movie item at position
     */
    public Movie getItem(int position){
        return mMoviesList.get(position);
    }


}

