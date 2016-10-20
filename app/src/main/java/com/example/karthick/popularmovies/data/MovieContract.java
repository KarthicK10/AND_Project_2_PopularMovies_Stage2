package com.example.karthick.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by KarthicK on 10/18/2016.
 *
 * Defines Table and Column names for the Movies Database
 *
 */

public class MovieContract {

    //Path to access the favorites table
    public static final String PATH_FAVORITE = "favorite";

    /*Inner class to define the column names of the favorite table*/
    public static final class FavoriteEntry implements BaseColumns{
        //Table name
        public static final String TABLE_NAME = "favorite";

        //Start - Column names

        //movie id, stored as int
        public static final String COLUMN_MOVIE_ID = "movie_id";

        //title, stored as String
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";

        //title, stored as String
        public static final String COLUMN_POSTER_PATH = "poster_path";

        //title, stored as String
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        //title, stored as String
        public static final String COLUMN_SYNAPSIS = "synapsis";

        //title, stored as String
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        //title, stored as String
        public static final String COLUMN_RELEASE_DATE = "release_date";

        //End - Column names

    }

}
