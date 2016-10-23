package com.example.karthick.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MovieVideosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_videos_activity);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_details_activity_layout, new MovieVideosFragment())
                    .commit();
        }

    }

}
