package com.perfalcon.balav.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IngredientDbHelper extends SQLiteOpenHelper {
    private static final String TAG = IngredientDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "baking.db";
    private static final int VERSION = 1;

    public IngredientDbHelper(Context context) {
        super (context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + IngredientContract.IngredientEntry.TABLE_NAME + " (" +
                IngredientContract.IngredientEntry._ID                + " INTEGER PRIMARY KEY, " +
                IngredientContract.IngredientEntry.RECIPE_NAME + " TEXT NOT NULL, " +
                IngredientContract.IngredientEntry.INGREDIENT    + " TEXT NOT NULL,"+
                IngredientContract.IngredientEntry.MEASURE +" TEXT NOT NULL,"+
                IngredientContract.IngredientEntry.QUANTITY+" REAL NOT NULL" +");";
        db.execSQL(CREATE_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + IngredientContract.IngredientEntry.TABLE_NAME);
        onCreate(db);
    }
}
