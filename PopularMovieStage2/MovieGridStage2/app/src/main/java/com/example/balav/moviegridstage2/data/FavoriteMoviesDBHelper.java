package com.example.balav.moviegridstage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.balav.moviegridstage2.data.FavoriteMoviesContract;

public class FavoriteMoviesDBHelper extends SQLiteOpenHelper {

    private static final String TAG = FavoriteMoviesDBHelper.class.getSimpleName();
    // The name of the database
    private static final String DATABASE_NAME = "favMoviesDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;


    // Constructor
    FavoriteMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
          }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + FavoriteMoviesContract.FavMovieEntry.TABLE_NAME + " (" +
                FavoriteMoviesContract.FavMovieEntry._ID                + " INTEGER PRIMARY KEY, " +
                FavoriteMoviesContract.FavMovieEntry.MOVIE_ID + " INTEGER NOT NULL, " +
                FavoriteMoviesContract.FavMovieEntry.TITLE    + " TEXT NOT NULL,"+
                FavoriteMoviesContract.FavMovieEntry.PLOT_SYNOPSIS +" TEXT NOT NULL,"+
                FavoriteMoviesContract.FavMovieEntry.POSTER_PATH+" TEXT NOT NULL,"+
                FavoriteMoviesContract.FavMovieEntry.RELEASE_DATE+" TEXT NOT NULL,"+
                FavoriteMoviesContract.FavMovieEntry.USER_RATING+" TEXT NOT NULL,"+
                FavoriteMoviesContract.FavMovieEntry.DURATION+" INTEGER NOT NULL,"+
                FavoriteMoviesContract.FavMovieEntry.TRAILERS+" TEXT NOT NULL,"+
                FavoriteMoviesContract.FavMovieEntry.REVIEWS+" TEXT NOT NULL"+");";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.FavMovieEntry.TABLE_NAME);
      onCreate(db);
    }
}
