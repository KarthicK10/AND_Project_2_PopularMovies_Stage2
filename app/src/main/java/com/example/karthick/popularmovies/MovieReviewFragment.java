package com.example.karthick.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karthick.popularmovies.data.Review;

/**
 * Created by KarthicK on 10/22/2016.
 *
 * Fragment to fetch and show the reviews for a movie
 */

public class MovieReviewFragment extends android.support.v4.app.Fragment{

    private static final String LOG_TAG = MovieReviewFragment.class.getSimpleName();

    //Key to get parcelable movie object from arguments
    private static final String ARG_REVIEW = "review";

    private Review review = null;

    public MovieReviewFragment(){
        //Required empty public constructor
    }

    /*
     *Static Factory method to create fragment instance with passed in arguments
     * Work around to pass arguments from an activity to a fragment
     * */
    public static MovieReviewFragment newInstance(Review review){
        MovieReviewFragment movieReviewFragment = new MovieReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_REVIEW, review);
        movieReviewFragment.setArguments(bundle);
        return movieReviewFragment;
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
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            review = (Review)getArguments().getParcelable(ARG_REVIEW);
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
        View rootView = inflater.inflate(R.layout.list_item_review, container, false);

        if(review != null){
            TextView userName = (TextView)rootView.findViewById(R.id.review_item_user_name);
            TextView content = (TextView)rootView.findViewById(R.id.review_item_content);
            userName.setText(review.getAuthor());
            content.setText(review.getContent());
        }

        return rootView;
    }
}
