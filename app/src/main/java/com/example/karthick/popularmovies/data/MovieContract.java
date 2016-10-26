package com.example.karthick.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by KarthicK on 10/18/2016.
 *
 * Defines Table and Column names for the Movies Database
 *
 */

public final class MovieContract {

    //Define the Content Authority to be used in Content URI
    public static final String CONTENT_AUTHORIY = "com.example.karthick.popularmovies";

    //Define the base URI path
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORIY);

    //Define path for segment for favorite data
    public static final String PATH_FAVORITE = "favorite";

    private MovieContract(){
        //Suppress the constructor as this is not to be instantiated
    }

    /*Inner class to define the column names of the favorite table*/
    public static final class FavoriteEntry implements BaseColumns{

        //Table name
        public static final String TABLE_NAME = "favorite";

        //Start - Column names

        //movie id, stored as int
        public static final String COLUMN_MOVIE_ID = "movie_id";

        //title, stored as String
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";

        //poster image path, stored as String
        public static final String COLUMN_POSTER_PATH = "poster_path";

        //backdrop image path, stored as String
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        //movie overview, stored as String
        public static final String COLUMN_SYNAPSIS = "synapsis";

        //user rating, stored as Real
        public static final String COLUMN_VOTE_AVERAGE = "vote_average"; //TODO - Vote average might change. Why store it?

        //release date, stored as Long
        public static final String COLUMN_RELEASE_DATE = "release_date";

        //End - Column names

        //Content Uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE).build();

        //Cursor types
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORIY + "/" + PATH_FAVORITE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORIY + "/" + PATH_FAVORITE;

        //Method to build the favorites URI to return all favorite movies data
        public static Uri buildFavoriteUri(){
            return CONTENT_URI;
        }

        //Method to get a specific movie from the favorite movies data
        public static Uri buildFavoriteMovieUri(int movieId){
            return CONTENT_URI.buildUpon().appendPath(new Integer(movieId).toString()).build();
        }
    }

}
