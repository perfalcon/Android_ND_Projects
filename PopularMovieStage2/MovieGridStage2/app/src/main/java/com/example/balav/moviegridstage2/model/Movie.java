package com.example.balav.moviegridstage2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{

    private int id;
    private String title;
    private String posterPath;
    private String plotSynopsis; //overview
    private String releaseDate;
    private double userRating;
    private String reviews; //json String
    private String trailers; //json String

    protected Movie(Parcel in) {
        id = in.readInt ();
        title = in.readString ();
        posterPath = in.readString ();
        plotSynopsis = in.readString ();
        releaseDate = in.readString ();
        userRating = in.readDouble ();
        reviews = in.readString ();
        trailers = in.readString ();
        duration = in.readInt ();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie> () {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie (in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    private int duration;

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getTrailers() {
        return trailers;
    }

    public void setTrailers(String trailers) {
        this.trailers = trailers;
    }

    /**

     * No args constructor for use in serialization
     */
    public Movie() {
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt (id);
        dest.writeString (title);
        dest.writeString (posterPath);
        dest.writeString (plotSynopsis);
        dest.writeString (releaseDate);
        dest.writeDouble (userRating);
        dest.writeString (reviews);
        dest.writeString (trailers);
        dest.writeInt (duration);
    }
}
