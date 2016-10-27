package com.example.karthick.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by KarthicK on 10/18/2016.
 *
 * Manages the local database for movies data
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    //Database Version. Important - if you change the database schema, you must increment the version number.
    public static final int DATABASE_VERSION = 5;

    //Datatbase name
    static final String DATABASE_NAME = "movies.db";

    //Log tag
    private static final String LOG_TAG = MovieDBHelper.class.getSimpleName();

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     * @param context to use to open or create the database
     */
    public MovieDBHelper(Context context) {
       /*
        * @param name    of the database file, or null for an in-memory database
        * @param factory to use for creating cursor objects, or null for the default
        * @param version number of the database (starting at 1); if the database is older,
        *                {@link #onUpgrade} will be used to upgrade the database; if the database is
        *                newer, {@link #onDowngrade} will be used to downgrade the database
        */
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create table statement for the favorites table
        final String SQL_CREATE_FAVORTIE_TABLE = "CREATE TABLE " + MovieContract.FavoriteEntry.TABLE_NAME + " (" +
                MovieContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL UNIQUE," +
                MovieContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL," +
                MovieContract.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL," +
                MovieContract.FavoriteEntry.COLUMN_BACKDROP_PATH + " TEXT," +
                MovieContract.FavoriteEntry.COLUMN_SYNAPSIS + " TEXT," +
                MovieContract.FavoriteEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL," +
                MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE + " REAL NOT NULL);";

        Log.i(LOG_TAG, SQL_CREATE_FAVORTIE_TABLE);

        db.execSQL(SQL_CREATE_FAVORTIE_TABLE);

    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
