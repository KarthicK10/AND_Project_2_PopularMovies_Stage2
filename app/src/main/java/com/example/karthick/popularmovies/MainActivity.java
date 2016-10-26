package com.example.karthick.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private boolean mTwoPane;

    ArrayList<Video> mVideoArrayList = new ArrayList<>();
    ArrayList<Review> mReviewArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "On Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.movie_detail_container) == null){
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }else{
            //Two-pane mode
            mTwoPane = true;
            //Show the detail view by adding or replacing detail fragment to this activity
            if(savedInstanceState == null){
                /* Show the detail view
               by adding or replacing all the fragments of the detail activity
               into the container for details in this activity
                This basically means MainActivity has to do here
                the work done by DetailActivity in its onCreate
             */
            /*Pass movie object to the fragment construction
                MovieDetailsFragment movieDetailFragment = new MovieDetailsFragment();
                MovieSynapsisFragment movieSynapsisFragment = new MovieSynapsisFragment();
                */

                /*Add the fragments*/
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_details_fragment_holder, new MovieDetailsFragment())
                        .commit();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_synapsis_fragment_holder, new MovieSynapsisFragment())
                        .commit();

            }
        }
    }

    @Override
    protected void onStop() {
        Log.i(LOG_TAG, "On Stop");
        super.onStop();
    }

    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    @Override
    protected void onStart() {
        Log.i(LOG_TAG, "On Start");
        super.onStart();
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p/>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p/>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p/>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p/>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p/>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * DetailFragmentCallback for when an item has been selected.
     *
     * @param movie
     */
    @Override
    public void onItemSelected(Movie movie) {
        if(!mTwoPane){
            Intent openMovieDetailsIntent = new Intent(this, MovieDetailActivity.class);
            openMovieDetailsIntent.putExtra(Movie.MOVIE_PARCEL_KEY, movie);
            startActivity(openMovieDetailsIntent);
        }
        else{
            //In Two pane mode
            /* Show the detail view
               by adding or replacing all the fragments of the detail activity
               into the container for details in this activity
                This basically means MainActivity has to do here
                the work done by DetailActivity in its onCreate
             */
            /*Pass movie object to the fragment construction */
            MovieDetailsFragment movieDetailFragment = MovieDetailsFragment.newInstance(movie);
            MovieSynapsisFragment movieSynapsisFragment = MovieSynapsisFragment.newInstance(movie);

                /*Add the fragments*/
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_details_fragment_holder, movieDetailFragment)
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_synapsis_fragment_holder, movieSynapsisFragment)
                    .commit();

            /*Remove existing reviews fragment*/
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.reviews_fragment_holder, new MovieReviewFragment())
                    .commit();

            /*Get Reviews data from the API using Retrofit */
            getReviewsFromApi(movie);

                /*Get trailer/teaser videos data from the API using Retrofit */
            getVideosFromApi(movie);

        }
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
        final ViewGroup rootViewOfDetailActivity = (ViewGroup) this.findViewById(R.id.movie_details_fragment_holder);
        //Get the first trailer video
        if(!mVideoArrayList.isEmpty()){
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
