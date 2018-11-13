package com.perfalcon.balav.bakingapp.data;

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

public class IngredientContentProvider extends ContentProvider {

    public static final int INGREDIENTS = 100;
    //public static final int PLANT_WITH_ID = 101;

    // Declare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String TAG = IngredientContentProvider.class.getName();

    private IngredientDbHelper mIngredientDbHelper;

    public static UriMatcher buildUriMatcher() {
        // Initialize a UriMatcher
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Add URI matches
        uriMatcher.addURI(IngredientContract.AUTHORITY, IngredientContract.PATH_INGREDIENTS, INGREDIENTS);
        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        Context context = getContext ();
        mIngredientDbHelper = new IngredientDbHelper (context);
        Log.v (TAG,"in constructor---> "+mIngredientDbHelper);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase sqLiteDatabase =mIngredientDbHelper.getReadableDatabase ();
        int match = sUriMatcher.match (uri);
        Cursor retCursor;
        switch (match){
            case INGREDIENTS:
                retCursor = sqLiteDatabase.query (IngredientContract.IngredientEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
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
        final SQLiteDatabase sqLiteDatabase = mIngredientDbHelper.getWritableDatabase ();
        int match = sUriMatcher.match (uri);
        Uri returnUri;
        switch (match){
            case INGREDIENTS:
                long id = sqLiteDatabase.insert (IngredientContract.IngredientEntry.TABLE_NAME,null,values);
                if(id>0){
                    returnUri= ContentUris.withAppendedId (IngredientContract.IngredientEntry.CONTENT_URI,id);
                }
                else{
                    throw new android.database.SQLException ("Failed to insert row into"+uri);
                }
                Log.v(TAG,"Number of Records inserted -->"+id);
                break;
            default:
                throw new UnsupportedOperationException ("Unkown uri "+uri);
        }
        getContext ().getContentResolver ().notifyChange (uri,null);


        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase sqLiteDatabase = mIngredientDbHelper.getWritableDatabase ();
        int match = sUriMatcher.match (uri);
        int rows_deleted = 0;
        switch (match) {
            case INGREDIENTS:
                rows_deleted = sqLiteDatabase.delete (IngredientContract.IngredientEntry.TABLE_NAME, null, null);
                break;
            default:
                throw new UnsupportedOperationException ("Unknown uri: " + uri);
        }
        if (rows_deleted != 0) {
            getContext ().getContentResolver ().notifyChange (uri, null);
        }
        return rows_deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
