package com.example.karthick.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.HashSet;

/**
 * Created by KarthicK on 10/20/2016.
 *
 * Unit Tests for Database operations
 */

public class TestDB extends AndroidTestCase {

    public static final String LOG_TAG = AndroidTestCase.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }


    /* Test case to test the table creation*/
    public void testCreateDB() throws Throwable{
        //Create a hashset of all table names
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieContract.FavoriteEntry.TABLE_NAME);

        //Get SQLite DB
        SQLiteDatabase db = new MovieDBHelper(this.mContext).getWritableDatabase();

        //Check if DB is open
        assertTrue("Error: DB is not open", db.isOpen());

        //Check if there are tables
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table'", null);
        assertTrue("Error: No Tables created", c.moveToFirst());

        //Check if all the tables we need are created
        do{
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        assertTrue("Error: All required tables are not created", tableNameHashSet.isEmpty());

        //Check if favorite table has the right columns
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.FavoriteEntry.TABLE_NAME + ")", null);
        assertTrue("Error: Favorite table not created", c.moveToFirst());
        final HashSet<String> favoriteColumnHashSet = new HashSet<String>();
        favoriteColumnHashSet.add(MovieContract.FavoriteEntry._ID);
        favoriteColumnHashSet.add(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID);
        favoriteColumnHashSet.add(MovieContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE);
        favoriteColumnHashSet.add(MovieContract.FavoriteEntry.COLUMN_SYNAPSIS);
        favoriteColumnHashSet.add(MovieContract.FavoriteEntry.COLUMN_POSTER_PATH);
        favoriteColumnHashSet.add(MovieContract.FavoriteEntry.COLUMN_BACKDROP_PATH);
        favoriteColumnHashSet.add(MovieContract.FavoriteEntry.COLUMN_VOTE_AVERAGE);
        favoriteColumnHashSet.add(MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE);
        int columnNameIndex = c.getColumnIndex("name");
        do{
            favoriteColumnHashSet.remove(c.getString(columnNameIndex));
            Log.i(LOG_TAG, c.getString(columnNameIndex));
        }while (c.moveToNext());
        assertTrue("Error: Favorite table is not created with all columns", favoriteColumnHashSet.isEmpty());

        //close cursor
        c.close();

        //close db
        db.close();
    }

    public void testFavoriteTable(){
        SQLiteDatabase db = null;
        Cursor c = null;
        long favoirteRowId = -1L;
        try{
            //Get writable database
            db = new MovieDBHelper(this.mContext).getWritableDatabase();

            //check if db is open
            assertTrue("Error: DB not open", db.isOpen());

            //Create content values for insert
            ContentValues favoriteValues = TestUtilities.createFavoriteValues();

            //Insert content values
            favoirteRowId = db.insert(
                    MovieContract.FavoriteEntry.TABLE_NAME,
                    null,
                    favoriteValues
            );

            //Check if insert was successful
            assertTrue("Error: Insert failed", favoirteRowId != -1);

            //Query db and get the inserted row
            c = db.query(
                    MovieContract.FavoriteEntry.TABLE_NAME,
                    null, // all columns
                    MovieContract.FavoriteEntry._ID + " = ?",
                    new String[] {new Long(favoirteRowId).toString()},
                    null,
                    null,
                    null
                    );

            //Check if record returned
            assertTrue("Error: Inserted record not returned", c.moveToFirst());

            //Check if record values match what was inserted
            TestUtilities.validateCurrentRecord("Error: Favorite query validation falied.", c, favoriteValues);

        }
        catch (Exception e){
            fail("Error : " + e.getMessage());
        }
        finally {
            //Finally close the cursor and the database
            if(c != null){
                c.close();
            }
            if(db != null){
                db.close();
            }
        }
    }


}
