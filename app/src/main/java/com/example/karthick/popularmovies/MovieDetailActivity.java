package com.example.karthick.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.karthick.popularmovies.domain.Movie;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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


    /**
     * A simple {@link Fragment} subclass.
     * Fragment to show the movie details like rating and release date
     * and a movie thumbnail image
     */
    public static class MovieDetailFragment extends Fragment {

        final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        String imageSizePath = "w780/";

        public MovieDetailFragment() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {

            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            /*Inflate movie detail fragment layout */
            View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);

            /*Get the movie object from the Intent */
            Intent openMovieDetailsIntent = getActivity().getIntent();
            if(openMovieDetailsIntent != null && openMovieDetailsIntent.hasExtra(Movie.MOVIE_PARCEL_KEY)){
                Movie movie = (Movie) openMovieDetailsIntent.getParcelableExtra(Movie.MOVIE_PARCEL_KEY);

                /*Fill the backdrop image view */
                ImageView backDropImageView = (ImageView) rootView.findViewById(R.id.movie_detail_fragment_backdrop_image);
                String picassoPath = IMAGE_BASE_URL+imageSizePath+movie.getBackdropPath();
                Picasso.with(getContext()).load(picassoPath).into(backDropImageView);

                /*Populate the movie details */
                LinearLayout detailsLayout = (LinearLayout) rootView.findViewById(R.id.movie_detail_fragment_details_layout);
                /*Show Original Title */
                TextView originalTitleText = (TextView) detailsLayout.findViewById(R.id.movie_detail_fragment_details_layout_original_title);
                originalTitleText.setText(movie.getOriginalTitle());
                /*Show User Rating*/
                TextView userRatingText = (TextView) detailsLayout.findViewById(R.id.movie_detail_fragment_details_layout_user_rating);
                userRatingText.setText(String.format("%.2f", movie.getUserRating()));
                /*Show Release Date */
                TextView releaseDateText = (TextView) detailsLayout.findViewById(R.id.movie_detail_fragment_details_layout_release_date);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                releaseDateText.setText(dateFormat.format(movie.getReleaseDate()));
            }
            return rootView;
        }

    }

    public static class MovieSynapsisFragment extends Fragment {

        public MovieSynapsisFragment() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.movie_synapsis_fragment, container, false);
            Intent openMovieDetailsIntent = getActivity().getIntent();
            if(openMovieDetailsIntent != null && openMovieDetailsIntent.hasExtra(Movie.MOVIE_PARCEL_KEY)){
                Movie movie = (Movie) openMovieDetailsIntent.getParcelableExtra(Movie.MOVIE_PARCEL_KEY);
                TextView synapisTextView = (TextView) rootView.findViewById(R.id.movie_synapsis_fragment_textview);
                synapisTextView.setText(movie.getSynapsis());
            }
            return rootView;
        }

    }

}
