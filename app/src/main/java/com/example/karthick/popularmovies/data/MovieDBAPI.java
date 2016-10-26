package com.example.karthick.popularmovies.data;

import com.example.karthick.popularmovies.data.MovieDBResult;
import com.example.karthick.popularmovies.data.ReviewDBResult;
import com.example.karthick.popularmovies.data.VideoDBResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by KarthicK on 8/6/2016.
 *
 * Interface to be used by Retrofit
 *
 */
public interface MovieDBAPI {
    @GET("/3/movie/{sortOrder}")
    Call<MovieDBResult> getmoviesList(@Path("sortOrder") String sortOrder, @Query("api_key") String apiKey, @Query("page") int page);

    @GET("/3/movie/{id}/reviews")
    Call<ReviewDBResult> getReviewsList(@Path("id") int movieId, @Query("api_key") String apiKey);

    @GET("/3/movie/{id}/videos")
    Call<VideoDBResult> getVideosList(@Path("id") int movieId, @Query("api_key") String apiKey);
}
