package com.example.karthick.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karthick.popularmovies.data.Review;

import java.util.ArrayList;

/**
 * Created by KarthicK on 10/22/2016.
 *
 * Fragment to fetch and show the reviews for a movie
 */

public class MovieReviewFragment extends android.support.v4.app.Fragment{

    private static final String LOG_TAG = MovieReviewFragment.class.getSimpleName();

    private Review mReview = null;

    private ArrayList<Review> mReviewArrayList = new ArrayList<>();

    public MovieReviewFragment(){
        //Required empty public constructor
    }

    /*
     *Static Factory method to create fragment instance with passed in arguments
     * Work around to pass arguments from an activity to a fragment
     * */
    public static MovieReviewFragment newInstance(Review review, ArrayList<Review> reviewArrayList){
        MovieReviewFragment movieReviewFragment = new MovieReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Review.REVIEW_PARCEL_KEY, review);
        bundle.putParcelableArrayList(Review.REVIEW_LIST_PARCEL_KEY, reviewArrayList);
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
            mReview = (Review)getArguments().getParcelable(Review.REVIEW_PARCEL_KEY);
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
        View rootView = inflater.inflate(R.layout.list_item_review, container, false);

        if(mReview != null){
            TextView userName = (TextView)rootView.findViewById(R.id.review_item_user_name);
            TextView content = (TextView)rootView.findViewById(R.id.review_item_content);
            userName.setText(mReview.getAuthor());
            content.setText(mReview.getContent());
            content.setMaxLines(3);
            content.setEllipsize(TextUtils.TruncateAt.END);
        }

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create Intent and pass to MovieAllReviewsActivity
                Intent intent = new Intent(getActivity(), MovieAllReviewsActivity.class);
                intent.putParcelableArrayListExtra(Review.REVIEW_LIST_PARCEL_KEY, mReviewArrayList);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
