package com.example.karthick.popularmovies.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by KarthicK on 10/22/2016.
 *
 * Represents a Review
 */

public class Review {

    @SerializedName("id")
    private String review_id;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    public Review(String review_id, String author, String content) {
        this.review_id = review_id;
        this.author = author;
        this.content = content;
    }

    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
