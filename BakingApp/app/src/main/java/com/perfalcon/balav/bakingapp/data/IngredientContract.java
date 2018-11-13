package com.perfalcon.balav.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class IngredientContract {
    public static final String AUTHORITY="com.perfalcon.balav.bakingapp";
    public static final Uri BASE_CONTENT_URI= Uri.parse ("content://"+AUTHORITY);
    public static final String PATH_INGREDIENTS = "ingredients";

    public static final long INVALID_RECIPE_ID = -1;

public static final class IngredientEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon ().appendPath (PATH_INGREDIENTS).build ();

        // FAVORITE MOVIE table and column names
        public static final String TABLE_NAME = "ingredients";

        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final  String RECIPE_NAME="recipe_name";
        public static final  String QUANTITY="quantity";
        public static final  String MEASURE="measure";
        public static final  String INGREDIENT="ingredient";
    }
}
