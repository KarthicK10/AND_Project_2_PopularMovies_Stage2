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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 *
 * Container for all movies.
 */
public class MoviesFragment extends Fragment {

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

        /*Create dummy data for testing the grid view */
        ArrayList<Movie> moviesList = new ArrayList<Movie>();
        moviesList.add(new Movie(1, "Movie1", "posterPath", "synapsis...", 9.5f, new Date()));
        moviesList.add(new Movie(2, "Movie1", "posterPath", "synapsis...", 9.5f, new Date()));
        moviesList.add(new Movie(3, "Movie1", "posterPath", "synapsis...", 9.5f, new Date()));
        moviesList.add(new Movie(4, "Movie1", "posterPath", "synapsis...", 9.5f, new Date()));
        moviesList.add(new Movie(5, "Movie1", "posterPath", "synapsis...", 9.5f, new Date()));
        moviesList.add(new Movie(6, "Movie1", "posterPath", "synapsis...", 9.5f, new Date()));
        moviesList.add(new Movie(7, "Movie1", "posterPath", "synapsis...", 9.5f, new Date()));
        moviesList.add(new Movie(8, "Movie1", "posterPath", "synapsis...", 9.5f, new Date()));
        moviesList.add(new Movie(9, "Movie1", "posterPath", "synapsis...", 9.5f, new Date()));
        moviesList.add(new Movie(10, "Movie1", "posterPath", "synapsis...", 9.5f, new Date()));
        moviesList.add(new Movie(11, "Movie1", "posterPath", "synapsis...", 9.5f, new Date()));
        moviesList.add(new Movie(12, "Movie1", "posterPath", "synapsis...", 9.5f, new Date()));


        /*Initiate an array adapter to supply text views to the grids*/
        MovieAdapter moviesGridAdapter = new MovieAdapter(getActivity(), moviesList);

        GridView moviesGrid = (GridView) rootView.findViewById(R.id.gridview_movies);
        moviesGrid.setAdapter(moviesGridAdapter);
        moviesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openMovieDetailsIntent = new Intent(getActivity(), MovieDetailActivity.class);
                startActivity(openMovieDetailsIntent);
            }
        });

        return rootView;
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, String[]>{

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
        protected String[] doInBackground(String... params) {

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



            return null;

        }

    }



}
