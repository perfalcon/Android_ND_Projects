package com.example.balav.moviegridstage2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.balav.moviegridstage2.data.FavoriteMoviesContract.FavMovieEntry.TABLE_NAME;

public class FavoriteMoviesContentProvider extends ContentProvider {
    public static final int FAV_MOVIES=100;
    public static final int FAV_MOVIE_ID=101;
    private static final String TAG = FavoriteMoviesContentProvider.class.getSimpleName ();

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher (UriMatcher.NO_MATCH);
        uriMatcher.addURI (FavoriteMoviesContract.AUTHORITY,FavoriteMoviesContract.PATH_FAV_MOVIES,FAV_MOVIES);
        uriMatcher.addURI (FavoriteMoviesContract.AUTHORITY,FavoriteMoviesContract.PATH_FAV_MOVIES+"/#",FAV_MOVIE_ID);
        return uriMatcher;
    }

    private FavoriteMoviesDBHelper favoriteMoviesDBHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext ();
        favoriteMoviesDBHelper = new FavoriteMoviesDBHelper (context);
        Log.v (TAG,"in constructor---> "+favoriteMoviesDBHelper);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase sqLiteDatabase = favoriteMoviesDBHelper.getReadableDatabase ();
        int match = sUriMatcher.match (uri);
        Cursor retCursor;
        switch (match){
            case FAV_MOVIES:
                retCursor= sqLiteDatabase.query (TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case FAV_MOVIE_ID:
                // Get the id from the URI
                String movie_id = uri.getPathSegments().get(1);
                Log.v(TAG,"Movie ID -->"+movie_id);
                // Selection is the _ID column = ?, and the Selection args = the row ID from the URI
                String mSelection = FavoriteMoviesContract.FavMovieEntry.MOVIE_ID+"=?";
                String[] mSelectionArgs = new String[]{movie_id};

                // Construct a query as you would normally, passing in the selection/args
                retCursor =  sqLiteDatabase.query(TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException ("Unkonw uri "+uri);
        }
        retCursor.setNotificationUri (getContext ().getContentResolver (),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase sqLiteDatabase = favoriteMoviesDBHelper.getWritableDatabase ();
        int match = sUriMatcher.match (uri);
        Uri returnUri;
        switch (match){
            case FAV_MOVIES:
                long id = sqLiteDatabase.insert (TABLE_NAME,null,values);
                if(id>0){
                    returnUri = ContentUris.withAppendedId (FavoriteMoviesContract.FavMovieEntry.CONTENT_URI,id);
                }else{
                    throw new android.database.SQLException ("Failed to insert row into"+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException ("Unkown uri "+uri);
        }
        getContext ().getContentResolver ().notifyChange (uri,null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = favoriteMoviesDBHelper.getWritableDatabase ();
        int match = sUriMatcher.match (uri);
        int rows_deleted=0;
        switch (match){
            case FAV_MOVIES:
                break;
            case FAV_MOVIE_ID:
                String move_id = uri.getPathSegments ().get (1);
                String mSelection = FavoriteMoviesContract.FavMovieEntry.MOVIE_ID+"=?";
                String[] mSelectionArgs=new String[]{move_id};
                rows_deleted = db.delete (TABLE_NAME,mSelection,mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }if(rows_deleted!=0){
            getContext ().getContentResolver ().notifyChange (uri,null);
        }
        return rows_deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
