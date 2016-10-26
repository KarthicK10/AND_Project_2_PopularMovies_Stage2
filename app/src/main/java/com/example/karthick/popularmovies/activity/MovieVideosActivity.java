package com.example.karthick.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.karthick.popularmovies.fragment.MovieVideosFragment;
import com.example.karthick.popularmovies.R;
import com.example.karthick.popularmovies.data.Video;

import java.util.ArrayList;

public class MovieVideosActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieVideosActivity.class.getSimpleName();
    ArrayList<Video> mVideoArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_videos_activity);

        if(savedInstanceState == null){
            /*Get trailers list from Intent */
            Intent intent = getIntent();
            if(intent != null && intent.hasExtra(Video.VIDEO_LIST_PARCEL_KEY)){
                mVideoArrayList = intent.getParcelableArrayListExtra(Video.VIDEO_LIST_PARCEL_KEY);
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_videos_activity_layout, MovieVideosFragment.createInstance(mVideoArrayList))
                    .commit();
        }

    }

}
