package com.example.karthick.popularmovies;

import com.example.karthick.popularmovies.domain.MovieDBResult;

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
}
