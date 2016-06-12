package com.example.karthick.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.karthick.popularmovies.domain.Movie;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must
 * Use the {@link MovieDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * This fragment can be reused in any activity that needs to show the basic movie details
 * Ex: An activity which lists all the movies with the basic details.
 */
public class MovieDetailsFragment extends Fragment {

    private static final String ARG_MOVIE = "movie";

    private Movie movie = null;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movie domain object.
     * @return A new instance of fragment MovieDetailsFragment.
     */
    public static MovieDetailsFragment newInstance(Movie movie) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = getArguments().getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*Inflate movie detail fragment layout */
        View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);

        if(movie != null){

            final String IMAGE_BASE_URL = getString(R.string.mdb_image_baseUrl);
            final String backdropImageSize = getString(R.string.mdb_image_size_780);
            final String thumbnailSize = getString(R.string.mdb_image_size_185);

            /*Fill the backdrop image view */
            ImageView backDropImageView = (ImageView) rootView.findViewById(R.id.movie_detail_fragment_backdrop_image);
            String picassoPath_backdrop = IMAGE_BASE_URL+backdropImageSize+movie.getBackdropPath();
            Picasso.with(getContext()).load(picassoPath_backdrop).into(backDropImageView);

            /*Show the thumbnail image*/
            ImageView thumbnailImageView = (ImageView) rootView.findViewById(R.id.movie_detail_fragment_thumbnail_image);
            String picassoPath_thumbnail = IMAGE_BASE_URL+thumbnailSize+movie.getPosterPath();
            Picasso.with(getContext()).load(picassoPath_thumbnail).into(thumbnailImageView);

            /*Populate the movie details */
            LinearLayout detailsLayout = (LinearLayout) rootView.findViewById(R.id.movie_detail_fragment_details_layout);
            /*Show Original Title */
            TextView originalTitleText = (TextView) detailsLayout.findViewById(R.id.movie_detail_fragment_details_layout_original_title);
            originalTitleText.setText(movie.getOriginalTitle());
            /*Show Release Date */
            TextView releaseDateText = (TextView) detailsLayout.findViewById(R.id.movie_detail_fragment_details_layout_release_date);
            DateFormat dateFormat = new SimpleDateFormat(getString(R.string.movie_detail_release_date_format));
            releaseDateText.setText(dateFormat.format(movie.getReleaseDate()));
            /*Show Rating Bar */
            RatingBar ratingBar = (RatingBar) detailsLayout.findViewById(R.id.movie_detail_fragment_details_layout_ratingbar);
            ratingBar.setRating((float)movie.getUserRating()/2);
            /*Show User Rating*/
            TextView userRatingText = (TextView) detailsLayout.findViewById(R.id.movie_detail_fragment_details_layout_user_rating);
            userRatingText.setText(String.format("%.2f", movie.getUserRating()/2 ));

        }
        return rootView;
    }


}
