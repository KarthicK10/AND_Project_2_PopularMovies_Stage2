package com.example.karthick.popularmovies.data;

import com.example.karthick.popularmovies.MovieDBAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by KarthicK on 10/24/2016.
 */

public class RetrofitAPIProvider {
    private static final String MOVIE_API_BASE_URL = "https://api.themoviedb.org";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(MOVIE_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final MovieDBAPI movieDBAPI = retrofit.create(MovieDBAPI.class);

    public static MovieDBAPI getMovieDBAPI(){
        return movieDBAPI;
    }
}
