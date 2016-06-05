package com.example.karthick.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 *
 * Container for all movies.
 */
public class MoviesFragment extends Fragment {

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*Inflate the grid view*/
        View rootView = inflater.inflate(R.layout.moviesfragment, container, false);

        /*Create dummy data for testing the grid view */
        ArrayList<String> dummmyData = new ArrayList<>(Arrays.asList(
                "movie1", "movie2", "movie3", "movie4", "movie5", "movie6"
        ));

        /*Initiate an array adapter to supply text views to the grids*/
        ArrayAdapter<String> moviesGridAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.grid_item_movie, R.id.grid_item_movie_textView,
                dummmyData
        );

        GridView moviesGrid = (GridView) rootView.findViewById(R.id.gridview_movies);
        moviesGrid.setAdapter(moviesGridAdapter);

        return rootView;
    }

}
