package com.example.karthick.popularmovies;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentSeperatorFragment extends Fragment {


    public ContentSeperatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*Inflate the content separator layout */
        View rootView = inflater.inflate(R.layout.content_seperator_fragment, container, false);
        return rootView;
    }

}
