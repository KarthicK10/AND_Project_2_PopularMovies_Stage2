package com.example.karthick.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karthick.popularmovies.domain.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 *
 * Container for all movies.
 */
public class MoviesFragment extends Fragment {

    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
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
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute(page);
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

    /*The AsyncTask to fetch the movies from theMovieDB API */
    private class FetchMoviesTask extends AsyncTask<Integer, Void, ArrayList<Movie>>{

        private final String LOG_TAG = AsyncTask.class.getSimpleName();

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected ArrayList<Movie> doInBackground(Integer... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            Log.i(LOG_TAG, "doInBackground: called");
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;
            final String apiKey = getString(R.string.movieDbApiKey);
            Integer pageNumber = params[0];
            String page = pageNumber.toString();

            try{
                //Construct the URL for fetching the
                final String MOVIE_API_BASE_URL = getString(R.string.movieDbBaseUrl);
                final String API_KEY_PARAM = getString(R.string.movieDbApiKeyParam);

                Uri moviesApiUri = Uri.parse(MOVIE_API_BASE_URL + getSortOrderPath()).buildUpon()
                                    .appendQueryParameter(API_KEY_PARAM, apiKey)
                                    .appendQueryParameter("page", page)
                                    .build();
                URL url = new URL(moviesApiUri.toString());

                Log.i(LOG_TAG, "movies API Uri :" + moviesApiUri.toString());

                //Create the request to the movie db and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read the input stream into a string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null){
                    //Nothing to do
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
                Log.i(LOG_TAG, moviesJsonStr);

            } catch (IOException e){
                Log.e(LOG_TAG, "Error", e);
                return  null;
            }
            finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();
                    }catch (final IOException e){
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try{
                return getMoviesFromJSON(moviesJsonStr);
            }
            catch(JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;

        }

        /*Function to get the movies data from the JSON string */
        private ArrayList<Movie> getMoviesFromJSON (String moviesJsonStr) throws JSONException{

            ArrayList<Movie> moviesArrayList = new ArrayList<>(); // The result list of movies to be returned.

            //These are the names of the JSON object that needs to be extracted
            final String MDB_RESULTS = getString(R.string.mdb_json_results);
            final String MDB_ID = getString(R.string.mdb_json_id);
            final String MDB_ORIGINAL_TITLE = getString(R.string.mdb_json_original_title);
            final String MDB_POSTER_PATH = getString(R.string.mdb_json_poster_path);
            final String MDB_BACKDROP_PATH = getString(R.string.mdb_json_backdrop_path);
            final String MDB_OVERVIEW = getString(R.string.mdb_json_overview);
            final String MDB_RELEASE_DATE = getString(R.string.mdb_json_release_date);
            final String MDB_VOTE_AVERAGE = getString(R.string.mdb_json_vote_average);
            final String MDB_RELEASE_DATE_FORMAT = getString(R.string.mdb_json_release_date_format);

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(MDB_RESULTS);

            try{
                for (int i = 0; i <moviesArray.length() ; i++) {

                    //Get the JSON object representing the movie
                    JSONObject movie = moviesArray.getJSONObject(i);

                    //Get the movie attributes
                    int movieId = movie.getInt(MDB_ID);
                    String originalTitle = movie.getString(MDB_ORIGINAL_TITLE);
                    String posterPath = movie.getString(MDB_POSTER_PATH);
                    String backdropPath = movie.getString(MDB_BACKDROP_PATH);
                    String synapsis = movie.getString(MDB_OVERVIEW);
                    double userRating = movie.getDouble(MDB_VOTE_AVERAGE);
                    DateFormat format = new SimpleDateFormat(MDB_RELEASE_DATE_FORMAT);
                    Date releaseDate = format.parse(movie.getString(MDB_RELEASE_DATE));

                    //Add the movie to the movies array list result set.
                    moviesArrayList.add(new Movie(movieId, originalTitle, posterPath, backdropPath, synapsis, userRating, releaseDate));
                }
            }
            catch (ParseException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return  moviesArrayList;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param movies The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            Log.i(LOG_TAG, "onPostExecute: Called");
            if(movies != null){
                int currentSize = moviesGridAdapter.getItemCount();
                Log.i(LOG_TAG, "onPostExecute: currentSize : " + currentSize);
                mMoviesList.addAll(movies);
                moviesGridAdapter.notifyItemRangeInserted(currentSize, movies.size()-1);
                Log.i(LOG_TAG, "onPostExecute: notifiedItemRangeInserted");
            }
        }
    }



}
