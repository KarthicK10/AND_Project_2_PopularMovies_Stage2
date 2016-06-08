package com.example.karthick.popularmovies.domain;

import java.util.Date;

/**
 * Created by KarthicK on 6/6/2016.
 */
public class Movie {

    private int id;
    private String originalTitle;
    private String posterPath;
    private String backdropPath;
    private String synapsis;
    private double userRating;
    private Date releaseDate;

    public Movie (int id, String originalTitle, String posterPath, String backdropPath, String synapsis, double userRating, Date releaseDate){
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.synapsis = synapsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getSynapsis() {
        return synapsis;
    }

    public double getUserRating() {
        return userRating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
}
