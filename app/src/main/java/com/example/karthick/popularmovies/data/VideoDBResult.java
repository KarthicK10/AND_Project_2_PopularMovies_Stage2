package com.example.karthick.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by KarthicK on 10/23/2016.
 */

public class VideoDBResult {

    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private ArrayList<Video> videoArrayList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Video> getVideoArrayList() {
        return videoArrayList;
    }

    public void setVideoArrayList(ArrayList<Video> videoArrayList) {
        this.videoArrayList = videoArrayList;
    }
}
