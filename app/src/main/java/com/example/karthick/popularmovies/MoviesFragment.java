package com.example.karthick.popularmovies;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.example.karthick.popularmovies.data.Movie;
import com.example.karthick.popularmovies.data.MovieContract;
import com.example.karthick.popularmovies.data.MovieDBResult;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 *
 * Container for all movies.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    private static final String MOVIE_API_BASE_URL = "https://api.themoviedb.org";
    private MovieAdapter moviesGridAdapter;
    private ArrayList<Movie> mMoviesList = new ArrayList<>();
    private String prevSortOrder;
    private GridLayoutManager gridLayoutManager;

    //Sort order Key for storing onSaveInstanceState
    public static final String SORT_ORDER = "sort_order";

    //Loader ID
    private static final int MOVIE_LOADER = 1;

    private static final String[] FAVORITE_MOVIE_COLUMNS = {
            MovieContract.FavoriteEntry._ID,
            MovieContract.FavoriteEntry.COLUMN_MOVIE_ID,
            MovieContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE,
            MovieContract.FavoriteEntry.COLUMN_POSTER_PATH,
            MovieContract.FavoriteEntry.COLUMN_BACKDROP_PATH,
            MovieContract.FavoriteEntry.COLUMN_SYNAPSIS,
            MovieContract.FavoriteEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE
    };

    //These indices are tied to FAVORITE_MOVIE_COLUMNS !Important
    static final int COL_FAVORITE_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_ORIGINAL_TITLE = 2;
    static final int COL_POSTER_PATH = 3;
    static final int COL_BACKDROP_PATH = 4;
    static final int COL_SYNAPSIS = 5;
    static final int COL_VOTE_AVERAGE = 6;
    static final int COL_RELEASE_DATE = 7;

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.i(LOG_TAG, "onCreate: Called");
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null || !savedInstanceState.containsKey(Movie.MOVIES_LIST_PARCEL_KEY) ){

            /*
             * This is the first creation and therefore state cannot be retrieved from savedInstanceState
             */
            refreshMoviesListing();
        }
        else{
            /* On Screen orientation change,
                the fragment gets recreated but the state can be restored from savedInstanceState
             */
            mMoviesList = savedInstanceState.getParcelableArrayList(Movie.MOVIES_LIST_PARCEL_KEY);

            /*Check if sort order is changed*/
            Log.i("previous Sort Order: " , savedInstanceState.getString(SORT_ORDER));
            Log.i("current sort order : ", getSortOrderPath());
            prevSortOrder = savedInstanceState.getString(SORT_ORDER);
        }

    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        Log.i(LOG_TAG, "onStart: Called");
        super.onStart();
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link MainActivity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "OnResume Called");
        if(prevSortOrder != null && !prevSortOrder.equals(getSortOrderPath())){
            Log.i(LOG_TAG, "SortOrderChanged: refreshing Movies Listing");
            refreshMoviesListing();
            prevSortOrder = getSortOrderPath();
        }

    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>This corresponds to {@link MovieDetailActivity#onSaveInstanceState(Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(LOG_TAG, "onSaveInstanceState: Called");
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Movie.MOVIES_LIST_PARCEL_KEY, mMoviesList);
        prevSortOrder = getSortOrderPath();
        Log.i("Saving sort order : ", getSortOrderPath());
        outState.putString(SORT_ORDER, prevSortOrder);
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
        // get favorite movies from local db using loader and content provider
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    /*Method to update the movies by calling theMovieDB API */
    private void fetchMoviesFromAPI(int page){
        Log.i(LOG_TAG, "fetchMoviesFromAPI: Called");

        String sortOrder = getSortOrderPath();
        Log.i(LOG_TAG, sortOrder);

        if(sortOrder != null && !sortOrder.equals(getString(R.string.sort_order_favorite_value))){
            String apiKey = getString(R.string.movieDbApiKey);

        /*Get movie data from api using Retrofit */
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MOVIE_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MovieDBAPI movieDBAPI = retrofit.create(MovieDBAPI.class);

            Call<MovieDBResult> call = movieDBAPI.getmoviesList(sortOrder, apiKey, page);

            call.enqueue(new Callback<MovieDBResult>() {
                @Override
                public void onResponse(Call<MovieDBResult> call, Response<MovieDBResult> response) {
                    int statusCode = response.code();
                    MovieDBResult movieDBResult = response.body();
                    Log.i(LOG_TAG, "Retrofit call Status code : " + statusCode);
                    ArrayList<Movie> moviesList = movieDBResult.getMoviesList();
                    if(moviesList != null && getActivity() != null && isAdded()){
                        int currentSize = moviesGridAdapter.getItemCount();
                        mMoviesList.addAll(moviesList);
                        moviesGridAdapter.notifyItemRangeInserted(currentSize, moviesList.size()-1);
                    }
                }

                @Override
                public void onFailure(Call<MovieDBResult> call, Throwable t) {
                    Log.i(LOG_TAG, t.getMessage());
                }
            });
        }
    }

    /*Refresh movies listing
    * Refreshes from local DB or API call based on current sort order preference
    * */
    private void refreshMoviesListing(){
        if(moviesGridAdapter != null){
            mMoviesList.clear();
            moviesGridAdapter.notifyDataSetChanged();
        }

        String sortOrder = getSortOrderPath();
        Log.i(LOG_TAG, sortOrder);

        if(sortOrder != null){
            if(sortOrder.equals(getString(R.string.sort_order_favorite_value))){
                //Get favorite movies from local DB
                getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
            }
            else{
                //Get movies from theMovieDB API
                fetchMoviesFromAPI(1);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(LOG_TAG, "onCreateView: Called");

        /*Inflate the grid view layout*/
        View rootView = inflater.inflate(R.layout.movies_fragment, container, false);

        /*Initiate a Recycler View adapter to supply text views to the grids*/
        moviesGridAdapter = new MovieAdapter(mMoviesList, getContext());

        //Lookup the recycler view in the layout
        RecyclerView moviesGrid = (RecyclerView) rootView.findViewById(R.id.recycler_view_movies);
        //Attach the adapter to the recycler view
        moviesGrid.setAdapter(moviesGridAdapter);
        //Set Layout manager to position the items
        gridLayoutManager =
                new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.grid_columns));
        moviesGrid.setLayoutManager(gridLayoutManager);

        //To improve smooth scrolling
        moviesGrid.setHasFixedSize(true);

        /*Add OnTouchListener to show movie detail activity on click of movie image */
        moviesGrid.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent openMovieDetailsIntent = new Intent(getActivity(), MovieDetailActivity.class);
                        Movie movie = moviesGridAdapter.getItem(position);
                        openMovieDetailsIntent.putExtra(Movie.MOVIE_PARCEL_KEY, movie);
                        startActivity(openMovieDetailsIntent);
                    }
                })
        );

        /*Add Scroll Listener to implement endless scrolling */
        moviesGrid.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.i(LOG_TAG, "onLoadMore : page :" + page);
                fetchMoviesFromAPI(page);
            }
        });

        /*Add decorator for spacing between grid items */
        RecyclerView.ItemDecoration spacingDecoration = new SpacesItemDecoration(16);
        moviesGrid.addItemDecoration(spacingDecoration);

       return rootView;
    }

    /*
    * Method to get the sort order path from the shared preferences.
    * */
    private String getSortOrderPath(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getString(getString(R.string.pref_sort_order_key), getString(R.string.pref_sort_order_default));
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
        Uri favoriteMovieUri = MovieContract.FavoriteEntry.buildFavoriteUri();
        return new android.support.v4.content.CursorLoader(getActivity(),
                favoriteMovieUri,
                FAVORITE_MOVIE_COLUMNS,
                null,
                null,
                null);
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
        Log.i(LOG_TAG, "onLoadFinished called");
        String sortOrder = getSortOrderPath();
        Log.i(LOG_TAG, sortOrder);
        //To avoid the loader refreshing the data on screen when sort order is not favorites
        if(sortOrder != null && sortOrder.equals(getString(R.string.sort_order_favorite_value))){
            ArrayList<Movie> moviesList = new ArrayList<>();
            if(data != null && data.moveToFirst()){
                do{
                    moviesList.add(new Movie(
                            data.getInt(COL_MOVIE_ID),
                            data.getString(COL_ORIGINAL_TITLE),
                            data.getString(COL_POSTER_PATH),
                            data.getString(COL_BACKDROP_PATH),
                            data.getString(COL_SYNAPSIS),
                            data.getDouble(COL_VOTE_AVERAGE), //TODO Vote average is not live data. Could have changed by now.
                            new Date() //TODO get actual release date
                    ));
                }while(data.moveToNext());
                mMoviesList.clear();
                mMoviesList.addAll(moviesList);
                moviesGridAdapter.notifyDataSetChanged();
                }
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
        Log.d(LOG_TAG, "onLoaderReset Called");
       // mMoviesList.clear();
        //moviesGridAdapter.notifyDataSetChanged();
    }
}
