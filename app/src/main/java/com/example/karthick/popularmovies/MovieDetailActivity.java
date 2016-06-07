package com.example.karthick.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_details_container, new MovieDetailFragment())
                    .add(R.id.movie_details_container, new MovieSynapsisFragment())
                    .commit();
        }
    }
}
