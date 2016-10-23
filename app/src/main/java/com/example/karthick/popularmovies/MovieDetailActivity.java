package com.example.karthick.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.karthick.popularmovies.data.Movie;
import com.example.karthick.popularmovies.data.Review;
import com.example.karthick.popularmovies.data.ReviewDBResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*

* This Activity is to show the details of a movie in different sections(fragments)
 *
 * This Gets the movie domain object from the Intent and passes it to the fragment classes
 * which in turn create the different fragments for this activity.
 * These fragments are added to a linear layout inside a scroll view to enable the user
 * to scroll through the different sections.
 *
 * A content separator fragment (A HTML <HR> equivalent) is added between each of the fragments.
 *
 * Thus this activity has the scope of adding more sections to the scroll in future.
*/

public class MovieDetailActivity extends AppCompatActivity {

    private static final String MOVIE_API_BASE_URL = "https://api.themoviedb.org";

    private static final String LOG_TAG = AppCompatActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);

        if(savedInstanceState == null){

            /*Get the movie object from the Intent
            * and pass it to the fragments*/
            Intent movieDetailsIntent = getIntent();
            if(movieDetailsIntent != null && movieDetailsIntent.hasExtra(Movie.MOVIE_PARCEL_KEY)) {

                /*Get the movie object from the Intent*/
                final Movie movie = (Movie) movieDetailsIntent.getParcelableExtra(Movie.MOVIE_PARCEL_KEY);

                /*Pass movie object to the fragment construction */
                MovieDetailsFragment movieDetailFragment = MovieDetailsFragment.newInstance(movie);
                MovieSynapsisFragment movieSynapsisFragment = MovieSynapsisFragment.newInstance(movie);

                /*Get Reviews data from the API using Retrofit */
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(MOVIE_API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                MovieDBAPI movieDBAPI = retrofit.create(MovieDBAPI.class);

                String apiKey = getString(R.string.movieDbApiKey);

                Call<ReviewDBResult> call = movieDBAPI.getReviewsList(movie.getId(), apiKey);

                /*Iterate reviews result to create Review fragments */
                call.enqueue(new Callback<ReviewDBResult>() {
                    @Override
                    public void onResponse(Call<ReviewDBResult> call, Response<ReviewDBResult> response) {
                        int statusCode = response.code();
                        Log.i(LOG_TAG, "Retrofit status code :" + statusCode);
                        if (statusCode == 200){
                            ReviewDBResult reviewDBResult = response.body();
                            ArrayList<Review> reviewArrayList = reviewDBResult.getReviewArrayList();
                            ArrayList<MovieReviewsFragment> movieReviewsFragments = new ArrayList<MovieReviewsFragment>();
                            /* for each review create a review fragment*/
                            for(Review review : reviewArrayList){
                                movieReviewsFragments.add(MovieReviewsFragment.newInstance(movie));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewDBResult> call, Throwable t) {
                        Log.e(LOG_TAG, t.getMessage());
                    }
                });


                /*Add the fragments*/
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movie_details_layout, movieDetailFragment)
                        .add(R.id.movie_details_layout, new ContentSeperatorFragment())
                        .add(R.id.movie_details_layout, movieSynapsisFragment)
                        .add(R.id.movie_details_layout, new ContentSeperatorFragment())
                        .commit();
            }

        }
        
    }
}
