package com.example.karthick.popularmovies.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by KarthicK on 6/6/2016.
 */
public class Movie implements Parcelable{

    public static final String MOVIE_PARCEL_KEY = "MOVIE_PARCEL";

    private int id;
    private String originalTitle;
    private String posterPath;
    private String backdropPath;
    private String synapsis;
    private double userRating;
    private Date releaseDate;


    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param out  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(originalTitle);
        out.writeString(posterPath);
        out.writeString(backdropPath);
        out.writeString(synapsis);
        out.writeDouble(userRating);
        out.writeLong(releaseDate.getTime());
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>(){
        /**
         * Create a new instance of the Parcelable class, instantiating it
         * from the given Parcel whose data had previously been written by
         * {@link Parcelable#writeToParcel Parcelable.writeToParcel()}.
         *
         * @param in The Parcel to read the object's data from.
         * @return Returns a new instance of the Parcelable class.
         */
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        /**
         * Create a new array of the Parcelable class.
         *
         * @param size Size of the array.
         * @return Returns an array of the Parcelable class, with every entry
         * initialized to null.
         */
        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in){
        id = in.readInt();
        originalTitle = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        synapsis = in.readString();
        userRating = in.readDouble();
        releaseDate = new Date(in.readLong());
    }

    public Movie (int id, String originalTitle, String posterPath, String backdropPath, String synapsis, double userRating, Date releaseDate){
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.synapsis = synapsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getSynapsis() {
        return synapsis;
    }

    public double getUserRating() {
        return userRating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
}
