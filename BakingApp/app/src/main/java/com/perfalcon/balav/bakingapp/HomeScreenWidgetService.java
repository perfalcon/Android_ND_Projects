package com.perfalcon.balav.bakingapp;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.perfalcon.balav.bakingapp.data.IngredientContract;

public class HomeScreenWidgetService extends Service {
    private static final String TAG = HomeScreenWidgetService.class.getSimpleName ();
    private String mRecipeName="";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public HomeScreenWidgetService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        RemoteViews views = new RemoteViews (getPackageName (), R.layout.homescreen_widget_provider);
        views.setTextViewText (R.id.widget_recipe,mRecipeName);
        views.setTextViewText (R.id.widget_text_ingredients,getIngredientsText());
        ComponentName theWidget = new ComponentName(this, HomeScreenWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(theWidget, views);
        return super.onStartCommand (intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private String getIngredientsText() {
        StringBuilder sb=new StringBuilder ();
        Uri PLANT_URI = IngredientContract.IngredientEntry.CONTENT_URI;
        Cursor cursor = getContentResolver ().query(
                PLANT_URI,
                null,
                null,
                null,
                null
        );
        String RecipeName="";
        Log.v (TAG, "count-->" + cursor.getCount ());
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for(int i=0;i<cursor.getCount ();i++) {
                int idIndex = cursor.getColumnIndex (IngredientContract.IngredientEntry._ID);
                int recipeIndex = cursor.getColumnIndex (IngredientContract.IngredientEntry.RECIPE_NAME);
                int ingredientIndex = cursor.getColumnIndex (IngredientContract.IngredientEntry.INGREDIENT);
                int quantityIndex = cursor.getColumnIndex (IngredientContract.IngredientEntry.QUANTITY);
                int measureIndex = cursor.getColumnIndex (IngredientContract.IngredientEntry.MEASURE);
                RecipeName = cursor.getString (recipeIndex);
                String ingredient = cursor.getString (ingredientIndex);
                String measure = cursor.getString (measureIndex);
                double quantity = cursor.getDouble (quantityIndex);
                sb.append (String.valueOf (cursor.getLong (idIndex)) + "." + ingredient + " " + String.valueOf (quantity) +" "+ measure + "\n");
                cursor.moveToNext ();
            }
        }
        Log.v(TAG,"Ingredient Text -->"+sb.toString ());
        mRecipeName = RecipeName;
        return sb.toString ();
    }



}