package com.example.karthick.popularmovies.domain;

/**
 * Created by KarthicK on 6/11/2016.
 *
 * Interface defining the contract between
 * the popular movies application and theMovieDB service
 *
 * This defines the constants for call paths
 * and agreed upon JSON element names in the api call result
 */
public interface TheMovieDBAPIContract {

    //Need not mention static final as this is an interface

    String movieDbBaseUrl  = "https://api.themoviedb.org/3";
    String movieDbApiKey  = "a6e98d8141a8b5f0c82333f52d90bffd";
    String movieDbApiKeyParam  = "api_key";
    String movieDbPopular  = "/movie/popular";
    String movieDbTopRated  = "/movie/top_rated";

    String mdb_json_results  = "results";
    String mdb_json_id  = "id";
    String mdb_json_original_title  = "original_title";
    String mdb_json_poster_path  = "poster_path";
    String mdb_json_backdrop_path  = "backdrop_path";
    String mdb_json_overview  = "overview";
    String mdb_json_release_date  = "release_date";
    String mdb_json_vote_average  = "vote_average";
    String mdb_json_release_date_format  = "yyyy-mm-dd";

    String mdb_image_baseUrl  = "http://image.tmdb.org/t/p/";
    String mdb_image_size_92 = "w92/";
    String mdb_image_size_154 = "w154/";
    String mdb_image_size_185 = "w185/";
    String mdb_image_size_342 = "w342/";
    String mdb_image_size_500 = "w500/";
    String mdb_image_size_780 = "w780/";
    String mdb_image_size_original = "original/";

}
