package com.example.karthick.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.karthick.popularmovies.data.Movie;

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
                Movie movie = (Movie) movieDetailsIntent.getParcelableExtra(Movie.MOVIE_PARCEL_KEY);

                /*Pass movie object to the fragment construction */
                MovieDetailsFragment movieDetailFragment = MovieDetailsFragment.newInstance(movie);
                MovieSynapsisFragment movieSynapsisFragment = MovieSynapsisFragment.newInstance(movie);

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
