package com.example.karthick.popularmovies.domain;

/**
 * Created by KarthicK on 6/5/2016.
 */
public enum MoviesSortOrder{

    POPULAR("/movie/popular"),
    TOP_RATED(" /movie/top_rated");

    private final String endPoint;

    MoviesSortOrder(String endPoint){
        this.endPoint = endPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }
}