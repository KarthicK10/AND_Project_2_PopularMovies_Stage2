package com.example.karthick.popularmovies.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karthick.popularmovies.R;
import com.example.karthick.popularmovies.activity.MovieVideosActivity;
import com.example.karthick.popularmovies.data.Video;

import java.util.ArrayList;

/**
 * Created by KarthicK on 10/23/2016.
 */

public class MoreVideosLinkFragment extends Fragment {
    private static final String LOG_TAG = MoreVideosLinkFragment.class.getSimpleName();

    private ArrayList<Video> mVideoArrayList = new ArrayList<>();

    public MoreVideosLinkFragment(){
        //required empty public constructor
    }

    /* Static factory method */
    public static MoreVideosLinkFragment createInstance(ArrayList<Video> movieVideosList){
        MoreVideosLinkFragment moreVideosLinkFragment = new MoreVideosLinkFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Video.VIDEO_LIST_PARCEL_KEY, movieVideosList);
        moreVideosLinkFragment.setArguments(bundle);
        return moreVideosLinkFragment;
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Context)} and before
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mVideoArrayList = getArguments().getParcelableArrayList(Video.VIDEO_LIST_PARCEL_KEY);
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.more_videos_link_fragment, container, false);

        /*Add onClickListener to go to Movie Trailers screen */
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent movieTrailersIntent = new Intent(getActivity(), MovieVideosActivity.class);
                movieTrailersIntent.putParcelableArrayListExtra(Video.VIDEO_LIST_PARCEL_KEY, mVideoArrayList);
                startActivity(movieTrailersIntent);
            }
        });
        return rootView;
    }
}
