package com.example.balav.moviegridstage2.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMoviesContract {
    public static final String AUTHORITY="com.example.balav.moviegridstage2";
    public static final Uri BASE_CONTENT_URI= Uri.parse ("content://"+AUTHORITY);
    public static final String PATH_FAV_MOVIES = "FAV_MOVIES";

    public static final class FavMovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon ().appendPath (PATH_FAV_MOVIES).build ();



        // FAVORITE MOVIE table and column names
        public static final String TABLE_NAME = "fav_movies";

        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final  String MOVIE_ID="movie_id";
        public static final  String TITLE="title";
        public static final  String POSTER_PATH="posterPath";
        public static final  String PLOT_SYNOPSIS="plotSynopsis";
        public static final  String RELEASE_DATE="releaseDate";
        public static final  String USER_RATING="userRating";
        public static final  String REVIEWS="reviews";
        public static final  String TRAILERS="trailers";
        public static final  String DURATION = "duration";
    }

}
