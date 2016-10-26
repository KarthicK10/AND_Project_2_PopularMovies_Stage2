package com.example.karthick.popularmovies.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.karthick.popularmovies.R;
import com.example.karthick.popularmovies.adapter.ReviewAdapter;
import com.example.karthick.popularmovies.data.Review;

import java.util.ArrayList;

/**
 * Created by KarthicK on 10/22/2016.
 *
 * Fragment to fetch and show the reviews for a movie
 */

public class MovieAllReviewsFragment extends android.support.v4.app.Fragment{

    private static final String LOG_TAG = MovieAllReviewsFragment.class.getSimpleName();

    private ArrayList<Review> mReviewArrayList = new ArrayList<>();

    public MovieAllReviewsFragment(){
        //Required empty public constructor
    }

    /*
     *Static Factory method to create fragment instance with passed in arguments
     * Work around to pass arguments from an activity to a fragment
     * */
    public static MovieAllReviewsFragment newInstance(ArrayList<Review> reviewArrayList){
        MovieAllReviewsFragment movieAllReviewsFragment = new MovieAllReviewsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Review.REVIEW_LIST_PARCEL_KEY, reviewArrayList);
        movieAllReviewsFragment.setArguments(bundle);
        return movieAllReviewsFragment;
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Context)} (Activity)} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p>
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mReviewArrayList = getArguments().getParcelableArrayList(Review.REVIEW_LIST_PARCEL_KEY);
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate movie reviews fragment layout
        View rootView = inflater.inflate(R.layout.movie_reviews_fragment, container, false);

        //Find list view for reviews
        ListView reviewsListView = (ListView) rootView.findViewById(R.id.listview_reviews);

        //Initialize custom array adapter
        ReviewAdapter reviewAdapter = new ReviewAdapter(getActivity(), mReviewArrayList);

        //Bind adapter and adapter view
        reviewsListView.setAdapter(reviewAdapter);

        return rootView;
    }
}
