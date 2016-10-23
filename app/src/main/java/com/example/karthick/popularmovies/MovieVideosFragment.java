package com.example.karthick.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.karthick.popularmovies.data.Video;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieVideosFragment extends Fragment {

    private static final String LOG_TAG = MovieVideosFragment.class.getSimpleName();

    private ArrayList<Video> mVideoArrayList = new ArrayList<>();

    public MovieVideosFragment() {
        //Required empty constructor
    }

    /* Static factory method */
    public static MovieVideosFragment createInstance(ArrayList<Video> movieVideosList){
        MovieVideosFragment movieVideosFragment = new MovieVideosFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Video.VIDEO_LIST_PARCEL_KEY, movieVideosList);
        movieVideosFragment.setArguments(bundle);
        return movieVideosFragment;
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Context)} } and before
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
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mVideoArrayList = getArguments().getParcelableArrayList(Video.VIDEO_LIST_PARCEL_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        //Inflate movie videos fragment
        View rootView = inflater.inflate(R.layout.movie_videos_fragment, container, false);

        //Find list view for trailers
        ListView videosListView = (ListView) rootView.findViewById(R.id.listview_trailers);

        //Initialize custom array adapter
        final TrailerAdapter trailerAdapter = new TrailerAdapter(getActivity(), mVideoArrayList);

        //Bind adapter and adapter view
        videosListView.setAdapter(trailerAdapter);

        /*Set onClick Listener to the list items to launch video in YouTube */
        videosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Video trailer = (Video) trailerAdapter.getItem(position);
                Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/")
                        .buildUpon().appendPath("watch").build()
                        .buildUpon().appendQueryParameter("v", trailer.getKey())
                        .build());
                startActivity(youTubeIntent);
            }
        });

        return rootView;
    }
}
