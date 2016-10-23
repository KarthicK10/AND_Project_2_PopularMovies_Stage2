package com.example.karthick.popularmovies;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentSeperatorFragment extends Fragment {

    private static final String LOG_TAG = ContentSeperatorFragment.class.getSimpleName();

    public ContentSeperatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");

        /*Inflate the content separator layout */
        View rootView = inflater.inflate(R.layout.content_seperator_fragment, container, false);
        return rootView;
    }

}
