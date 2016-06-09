package com.example.karthick.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.karthick.popularmovies.domain.Movie;
import com.example.karthick.popularmovies.domain.MoviesSortOrder;

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

    private MovieAdapter moviesGridAdapter;


    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*Inflate the grid view*/
        View rootView = inflater.inflate(R.layout.movies_fragment, container, false);

        /*Initiate an array adapter to supply text views to the grids*/
        moviesGridAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());

        GridView moviesGrid = (GridView) rootView.findViewById(R.id.gridview_movies);
        moviesGrid.setAdapter(moviesGridAdapter);
        moviesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openMovieDetailsIntent = new Intent(getActivity(), MovieDetailActivity.class);
                Movie movie = moviesGridAdapter.getItem(position);
                openMovieDetailsIntent.putExtra(Movie.MOVIE_PARCEL_KEY, movie);
                startActivity(openMovieDetailsIntent);
            }
        });

        return rootView;
    }

    private class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>>{

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
        protected ArrayList<Movie> doInBackground(Void... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;
            String apiKey = "a6e98d8141a8b5f0c82333f52d90bffd";
            MoviesSortOrder moviesSortOrder = MoviesSortOrder.POPULAR;

            try{
                //Construct the URL for fetching the
                final String MOVIE_API_BASE_URL = "https://api.themoviedb.org/3/movie/popular";
                final String API_KEY_PARAM = "api_key";

                Uri moviesApiUri = Uri.parse(MOVIE_API_BASE_URL).buildUpon()
                                    //.appendPath(moviesSortOrder.getEndPoint())
                                    .appendQueryParameter(API_KEY_PARAM, apiKey)
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

            ArrayList<Movie> moviesArrayList = new ArrayList<Movie>(); // The result list of movies to be returned.

            //These are the names of the JSON object that needs to be extracted
            final String MDB_RESULTS = "results";
            final String MDB_ID = "id";
            final String MDB_ORIGINAL_TITLE = "original_title";
            final String MDB_POSTER_PATH = "poster_path";
            final String MDB_BACKDROP_PATH = "backdrop_path";
            final String MDB_OVERVIEW = "overview";
            final String MDB_RELEASE_DATE = "release_date";
            final String MDB_VOTE_AVERAGE = "vote_average";

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
                    DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
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
            if(movies != null){
                moviesGridAdapter.clear();
                for (Movie movie: movies) {
                    moviesGridAdapter.add(movie);
                }
            }
        }
    }



}
