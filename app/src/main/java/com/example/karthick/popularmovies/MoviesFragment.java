package com.example.karthick.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karthick.popularmovies.data.Movie;
import com.example.karthick.popularmovies.data.MovieDBResult;

import java.util.ArrayList;

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
public class MoviesFragment extends Fragment {

    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    private static final String MOVIE_API_BASE_URL = "https://api.themoviedb.org";
    private MovieAdapter moviesGridAdapter;
    private ArrayList<Movie> mMoviesList = new ArrayList<>();
    private String prevSortOrder;
    private GridLayoutManager gridLayoutManager;

    //Sort order Key for storing onSaveInstanceState
    public static final String SORT_ORDER = "sort_order";

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

    /*Method to update the movies by calling theMovieDB API */
    private void updateMovies(int page){
        Log.i(LOG_TAG, "updateMovies: Called");

        String apiKey = getString(R.string.movieDbApiKey);

        /*Get movie data from api using Retrofit */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOVIE_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDBAPI movieDBAPI = retrofit.create(MovieDBAPI.class);
        String sortOrder = getSortOrderPath();

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

    /*Refresh movies listing */
    private void refreshMoviesListing(){
        if(moviesGridAdapter != null){
            mMoviesList.clear();
            moviesGridAdapter.notifyDataSetChanged();
        }
        updateMovies(1);
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
                updateMovies(page);
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

}
