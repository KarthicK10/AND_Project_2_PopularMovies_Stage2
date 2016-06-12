package com.example.karthick.popularmovies;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karthick.popularmovies.domain.Movie;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieSynapsisFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * This fragment can be used in any activity that wants to display the synapsis of a movie
 */
public class MovieSynapsisFragment extends android.support.v4.app.Fragment {

    private static final String ARG_MOVIE = "movie";

    private Movie movie = null;

    public MovieSynapsisFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movie domain object.
     * @return A new instance of fragment MovieSynapsisFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieSynapsisFragment newInstance(Movie movie) {
        MovieSynapsisFragment fragment = new MovieSynapsisFragment();
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
        View rootView = inflater.inflate(R.layout.movie_synapsis_fragment, container, false);
                  
        if(movie != null){
            TextView synapsisTextView = (TextView) rootView.findViewById(R.id.movie_synapsis_fragment_textview);
            synapsisTextView.setText(movie.getSynapsis());
        }
        return rootView;
    }

}
