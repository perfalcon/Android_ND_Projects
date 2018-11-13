package com.perfalcon.balav.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.perfalcon.balav.bakingapp.data.IngredientContract;

import static com.perfalcon.balav.bakingapp.data.IngredientContract.IngredientEntry.CONTENT_URI;

public class HomeScreenWidgetProvider extends AppWidgetProvider {
    private static final String TAG = HomeScreenWidgetProvider.class.getSimpleName ();

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.v(TAG , "In onUpdate");
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews (context.getPackageName (), R.layout.homescreen_widget_provider);

            fillIngredientsText(context,views);

            Intent intent = new Intent(context, MainActivity.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent=PendingIntent.getActivity (context,0,intent,0);
            views.setOnClickPendingIntent (R.id.widget_text_ingredients,pendingIntent);
            appWidgetManager.updateAppWidget (appWidgetId, views);
        }
    }
    private void fillIngredientsText(Context mContext, RemoteViews views) {
        StringBuilder sb=new StringBuilder ();
        Uri PLANT_URI = CONTENT_URI;
        Cursor cursor = mContext.getContentResolver ().query(
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
        views.setTextViewText (R.id.widget_text_ingredients,sb.toString ());
        views.setTextViewText (R.id.widget_recipe,RecipeName);
    }
}
