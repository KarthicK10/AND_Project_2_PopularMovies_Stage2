package com.example.karthick.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

/**
 * Created by KarthicK on 10/20/2016.
 *
 * Utilities class to help the test cases
 */

public class TestUtilities extends AndroidTestCase {

    /* static method to provide dummy content values for the favorite entry*/
    static ContentValues createFavoriteValues (){
        ContentValues testValues = new ContentValues();

        testValues.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID, 333484);
        testValues.put(MovieContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE, "The Magnificent Seven");
        testValues.put(MovieContract.FavoriteEntry.COLUMN_SYNAPSIS, "A big screen remake of John Sturges' classic western The Magnificent Seven, itself a remake of Akira Kurosawa's Seven Samurai. Seven gun men in the old west gradually come together to help a poor village against savage thieves.");
        testValues.put(MovieContract.FavoriteEntry.COLUMN_POSTER_PATH, "/z6BP8yLwck8mN9dtdYKkZ4XGa3D.jpg");
        testValues.put(MovieContract.FavoriteEntry.COLUMN_BACKDROP_PATH, "/T3LrH6bnV74llVbFpQsCBrGaU9.jpg");
        testValues.put(MovieContract.FavoriteEntry.COLUMN_VOTE_AVERAGE, 4.57);
        testValues.put(MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE, "2016-09-14");

        return testValues;
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }
}
