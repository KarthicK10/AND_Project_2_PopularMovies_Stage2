package com.example.karthick.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.karthick.popularmovies.data.Movie;
import com.example.karthick.popularmovies.data.RetrofitAPIProvider;
import com.example.karthick.popularmovies.data.Review;
import com.example.karthick.popularmovies.data.ReviewDBResult;
import com.example.karthick.popularmovies.data.Video;
import com.example.karthick.popularmovies.data.VideoDBResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private static final String LOG_TAG = AppCompatActivity.class.getSimpleName();

    ArrayList<Video> mVideoArrayList = new ArrayList<>();
    ArrayList<Review> mReviewArrayList = new ArrayList<>();

    private boolean firstCreation = true; //Flags if activity is created for first time or on screen rotation. Helps to use values from savedInstanceState

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);

        if(savedInstanceState == null || !savedInstanceState.containsKey(Video.VIDEO_LIST_PARCEL_KEY) ){

            /*Get the movie object from the Intent
            * and pass it to the fragments*/
            Intent movieDetailsIntent = getIntent();
            if(movieDetailsIntent != null && movieDetailsIntent.hasExtra(Movie.MOVIE_PARCEL_KEY)) {

                /*Get the movie object from the Intent*/
                final Movie movie = (Movie) movieDetailsIntent.getParcelableExtra(Movie.MOVIE_PARCEL_KEY);

                /*Pass movie object to the fragment construction */
                MovieDetailsFragment movieDetailFragment = MovieDetailsFragment.newInstance(movie);
                MovieSynapsisFragment movieSynapsisFragment = MovieSynapsisFragment.newInstance(movie);

                /*Add the fragments*/
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movie_details_fragment_holder, movieDetailFragment)
                        .add(R.id.movie_synapsis_fragment_holder, movieSynapsisFragment)
                        .commit();

                /*Get Reviews data from the API using Retrofit */
                getReviewsFromApi(movie);

                /*Get trailer/teaser videos data from the API using Retrofit */
                getVideosFromApi(movie);
            }

        }else{
            mVideoArrayList = savedInstanceState.getParcelableArrayList(Video.VIDEO_LIST_PARCEL_KEY);
            firstCreation = false;
        }
        
    }

    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "onStart");
        super.onStart();
        if(!firstCreation){
            setOnClickListenerForBackdropImage();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Video.VIDEO_LIST_PARCEL_KEY, mVideoArrayList);
    }

    /*Method to get Reviews data from the API using Retrofit
                * and add those to fragments*/
    private void getReviewsFromApi(Movie movie){
        String apiKey = getString(R.string.movieDbApiKey);
        Call<ReviewDBResult> reviewsCall = RetrofitAPIProvider.getMovieDBAPI().getReviewsList(movie.getId(), apiKey);

        /*Iterate reviews result to create Review fragments */
        reviewsCall.enqueue(new Callback<ReviewDBResult>() {
            @Override
            public void onResponse(Call<ReviewDBResult> call, Response<ReviewDBResult> response) {
                int statusCode = response.code();
                Log.i(LOG_TAG, "Retrofit status code for reviews :" + statusCode);
                if (statusCode == 200){
                    ReviewDBResult reviewDBResult = response.body();
                    mReviewArrayList = reviewDBResult.getReviewArrayList();
                    /* for each review add a review fragment*/
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    for(Review review : mReviewArrayList){
                        transaction.add(R.id.reviews_fragment_holder, MovieReviewFragment.newInstance(review, mReviewArrayList));
                    }
                    transaction.commit();
                }
            }
            @Override
            public void onFailure(Call<ReviewDBResult> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }

    /*Method to get Videos data from the API using Retrofit
    * and those to the fragments*/
    private void getVideosFromApi(Movie movie){
        String apiKey = getString(R.string.movieDbApiKey);
        Call<VideoDBResult> videosCall = RetrofitAPIProvider.getMovieDBAPI().getVideosList(movie.getId(), apiKey);
        /*Get videos list*/
        videosCall.enqueue(new Callback<VideoDBResult>() {
            @Override
            public void onResponse(Call<VideoDBResult> call, Response<VideoDBResult> response) {
                int statusCode = response.code();
                Log.i(LOG_TAG, "Retrofit status code for Videos :" + statusCode);
                if(statusCode == 200){
                    VideoDBResult videoDBResult = response.body();
                    mVideoArrayList = videoDBResult.getVideoArrayList();
                    //Set onClickListener for backdrop image
                    setOnClickListenerForBackdropImage();
                    if(mVideoArrayList.size() > 1){
                        //Add more videos fragment
                        addMoreVideosFragment();
                    }
                }
            }
            @Override
            public void onFailure(Call<VideoDBResult> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }

    private void setOnClickListenerForBackdropImage(){
        /* Rootview of detail activity
        to access the backdrop image view of movie details fragment
        */
        final ViewGroup rootViewOfDetailActivity = (ViewGroup) this.findViewById(R.id.movie_detail_container);
        //Get the first trailer video
        if(!mVideoArrayList.isEmpty()) {
            final Video firstTrailerVideo = mVideoArrayList.get(0);
            if(firstTrailerVideo != null && firstTrailerVideo.getKey() != null){
                //Get backdrop image view
                ImageView backDropImageView = (ImageView)rootViewOfDetailActivity.findViewById(R.id.movie_detail_fragment_backdrop_image);
                                /*Set on click listener for backdrop image to show trailer on youtube */
                backDropImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/")
                                .buildUpon().appendPath("watch").build()
                                .buildUpon().appendQueryParameter("v", firstTrailerVideo.getKey())
                                .build());
                        startActivity(youTubeIntent);
                    }
                });
            }
        }

    }

    private void addMoreVideosFragment(){
        Log.i(LOG_TAG, "Addig More Videos fragment");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.more_videos_fragment_holder, MoreVideosLinkFragment.createInstance(mVideoArrayList))
                .commit();
    }
}
