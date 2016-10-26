package com.example.karthick.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.karthick.popularmovies.fragment.MovieAllReviewsFragment;
import com.example.karthick.popularmovies.R;
import com.example.karthick.popularmovies.data.Review;

import java.util.ArrayList;

/**
 * Created by KarthicK on 10/23/2016.
 *
 * All Reviews screen
 */

public class MovieAllReviewsActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieAllReviewsActivity.class.getSimpleName();

    private ArrayList<Review> mReviewArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);

        //Inflate the reviews activity layout
        setContentView(R.layout.movie_reviews_activity);

        if(savedInstanceState == null){
            //Get reviews list from Intent
            Intent intent = getIntent();
            if(intent != null && intent.hasExtra(Review.REVIEW_LIST_PARCEL_KEY)){
                mReviewArrayList = intent.getParcelableArrayListExtra(Review.REVIEW_LIST_PARCEL_KEY);
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_reviews_activity_layout, MovieAllReviewsFragment.newInstance(mReviewArrayList))
                    .commit();
        }
    }
}
