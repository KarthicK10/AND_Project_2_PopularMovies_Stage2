package com.example.karthick.popularmovies.domain;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by KarthicK on 8/7/2016.
 *
 * The result from Retrofit call to themoviedb api
 */
public class MovieDBResult {

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private ArrayList<Movie> moviesList;

    public int getPage() {
        return page;
    }

    public ArrayList<Movie> getMoviesList() {
        return moviesList;
    }
}
