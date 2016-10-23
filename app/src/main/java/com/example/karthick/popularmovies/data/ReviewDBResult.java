package com.example.karthick.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by KarthicK on 10/22/2016.
 *
 * Represents a JSON response for Review API Call
 */

public class ReviewDBResult {

    @SerializedName("id")
    private int id;

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private ArrayList<Review> reviewArrayList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Review> getReviewArrayList() {
        return reviewArrayList;
    }

    public void setReviewArrayList(ArrayList<Review> reviewArrayList) {
        this.reviewArrayList = reviewArrayList;
    }
}
