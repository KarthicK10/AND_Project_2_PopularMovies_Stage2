package com.example.karthick.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by KarthicK on 10/23/2016.
 */

public class MoreVideosLinkFragment extends Fragment {
    private static final String LOG_TAG = MoreVideosLinkFragment.class.getSimpleName();

    public MoreVideosLinkFragment(){
        //required empty public constructor
    }

    /* Static factory method */
    public static MoreVideosLinkFragment createInstance(){
        MoreVideosLinkFragment moreVideosLinkFragment = new MoreVideosLinkFragment();
        return moreVideosLinkFragment;
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
        TextView moreTrailersTextView = (TextView)rootView.findViewById(R.id.more_videos_text);
        moreTrailersTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent movieTrailersIntent = new Intent(getActivity(), MovieVideosActivity.class);
                startActivity(movieTrailersIntent);
            }
        });
        return rootView;
    }
}
