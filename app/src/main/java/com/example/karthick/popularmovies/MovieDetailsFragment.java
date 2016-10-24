package com.example.karthick.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.example.karthick.popularmovies.data.Movie;
import com.example.karthick.popularmovies.data.MovieContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must
 * Use the {@link MovieDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * This fragment can be reused in any activity that needs to show the basic movie details
 * Ex: An activity which lists all the movies with the basic details.
 */
public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();

    private Movie movie = null;

    private static final int FAVORITE_CHECK_LOADER = 1;

    ImageView favIconImage;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movie domain object.
     * @return A new instance of fragment MovieDetailsFragment.
     */
    public static MovieDetailsFragment newInstance(Movie movie) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Movie.MOVIE_PARCEL_KEY, movie);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  It is also useful for fragments that use
     * {@link #setRetainInstance(boolean)} to retain their instance,
     * as this callback tells the fragment when it is fully associated with
     * the new activity instance.  This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(FAVORITE_CHECK_LOADER, null,  this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = getArguments().getParcelable(Movie.MOVIE_PARCEL_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        /*Inflate movie detail fragment layout */
        View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);

        if(movie != null){

            final String IMAGE_BASE_URL = getString(R.string.mdb_image_baseUrl);
            final String backdropImageSize = getString(R.string.mdb_image_size_780);
            final String thumbnailSize = getString(R.string.mdb_image_size_185);

            /*Fill the backdrop image view */
            ImageView backDropImageView = (ImageView) rootView.findViewById(R.id.movie_detail_fragment_backdrop_image);
            String imagePath_backdrop = IMAGE_BASE_URL+backdropImageSize+movie.getBackdropPath();
            AQuery aqBackDrop = new AQuery(backDropImageView);
            aqBackDrop.id(R.id.movie_detail_fragment_backdrop_image).image(imagePath_backdrop);

            /*Show the thumbnail image*/
            ImageView thumbnailImageView = (ImageView) rootView.findViewById(R.id.movie_detail_fragment_thumbnail_image);
            String imagePath_thumbnail = IMAGE_BASE_URL+thumbnailSize+movie.getPosterPath();
            AQuery aqThumbnail = new AQuery(thumbnailImageView);
            aqThumbnail.id(R.id.movie_detail_fragment_thumbnail_image).image(imagePath_thumbnail);

            /*Populate the movie details */
            LinearLayout detailsLayout = (LinearLayout) rootView.findViewById(R.id.movie_detail_fragment_details_layout);
            /*Show Original Title */
            TextView originalTitleText = (TextView) rootView.findViewById(R.id.movie_detail_fragment_details_layout_original_title);
            originalTitleText.setText(movie.getOriginalTitle());
            /*Show Release Date */
            TextView releaseDateText = (TextView) detailsLayout.findViewById(R.id.movie_detail_fragment_details_layout_release_date);
            DateFormat dateFormat = new SimpleDateFormat(getString(R.string.movie_detail_release_date_format));
            releaseDateText.setText( dateFormat.format( movie.getReleaseDate() ) );
            /*Show Rating Bar */
            RatingBar ratingBar = (RatingBar) detailsLayout.findViewById(R.id.movie_detail_fragment_details_layout_ratingbar);
            ratingBar.setRating((float)movie.getUserRating()/2);
            /*Show User Rating*/ //TODO - Should we even show it in number? as we are anyway showing in stars? Does the user care?
            TextView userRatingText = (TextView) detailsLayout.findViewById(R.id.movie_detail_fragment_details_layout_user_rating);
            userRatingText.setText(String.format("%.2f", movie.getUserRating() ) + getString(R.string.movie_detail_rating_suffix));
            /*Get favIcon Imageview*/
            favIconImage = (ImageView) rootView.findViewById(R.id.fav_icon);

        }
        return rootView;
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri favoriteMovieUri = MovieContract.FavoriteEntry.buildFavoriteMovieUri(movie.getId());
        return new android.support.v4.content.CursorLoader(
                getContext(),
                favoriteMovieUri,
                new String[] {MovieContract.FavoriteEntry.COLUMN_MOVIE_ID},
                MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ?",
                new String[] {new Integer(movie.getId()).toString()},
                null
        );
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {@link android.app.FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p>
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link Cursor}
     * and you place it in a {@link CursorAdapter}, use
     * the {@link CursorAdapter#CursorAdapter(Context,
     * Cursor, int)} constructor <em>without</em> passing
     * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link Cursor} from a {@link CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link CursorAdapter}, you should use the
     * {@link CursorAdapter#swapCursor(Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        final int removeFavId = R.mipmap.ic_remove_fav;
        final int addFavId = R.mipmap.ic_add_fav;
       /*Show the fav icon with corresponding image*/
        if(data.moveToFirst()){
            favIconImage.setImageResource(removeFavId);
            //Set on click listener to remove movie from favoirte on click of fav icon
            favIconImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toggle Fav Icon
                    favIconImage.setImageResource(addFavId);

                    //Remove movie from favorites
                    getContext().getContentResolver().delete(
                            MovieContract.FavoriteEntry.buildFavoriteMovieUri(movie.getId()),
                            MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{ new Integer(movie.getId()).toString() }
                            );
                    Toast toast = Toast.makeText(getContext(), "Removed from Favorites", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
        else{
            favIconImage.setImageResource(addFavId);
            //Set on click listener to add movie to favoirte on click of fav icon
            favIconImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toggle Fav Icon
                    favIconImage.setImageResource(removeFavId);

                    //Create Content Values
                    ContentValues favoriteValues = new ContentValues();
                    favoriteValues.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID, movie.getId());
                    favoriteValues.put(MovieContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
                    favoriteValues.put(MovieContract.FavoriteEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
                    favoriteValues.put(MovieContract.FavoriteEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
                    favoriteValues.put(MovieContract.FavoriteEntry.COLUMN_SYNAPSIS, movie.getSynapsis());
                    favoriteValues.put(MovieContract.FavoriteEntry.COLUMN_VOTE_AVERAGE, movie.getUserRating());
                    favoriteValues.put(MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate().getTime());

                    getContext().getContentResolver().insert(MovieContract.FavoriteEntry.buildFavoriteUri(), favoriteValues);
                    Toast toast = Toast.makeText(getContext(), "Added to Favorites", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
