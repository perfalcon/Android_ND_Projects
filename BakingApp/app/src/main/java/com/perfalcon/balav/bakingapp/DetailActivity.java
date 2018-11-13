package com.perfalcon.balav.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.perfalcon.balav.bakingapp.data.IngredientContract;
import com.perfalcon.balav.bakingapp.model.Baking;
import com.perfalcon.balav.bakingapp.model.Ingredient;

import java.util.ArrayList;
import java.util.ListIterator;

public class DetailActivity extends AppCompatActivity implements RecipeFragment.OnStepClickListener {
    private static final String TAG = DetailActivity.class.getSimpleName();
    public static final String RECIPE_KEY= "recipe";
    public static final String BUNDLE_KEY="bundle";
    public static final String BAKING_KEY="baking";
    public static final String BAKING_ID="baking_id";
    private  Baking baking;
    private int bakingId;
    private boolean mTwoPane;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState (outState);
        outState.putParcelable (BAKING_KEY,baking);    }

    @Override
    protected void onResume() {
        super.onResume ();
        HideNavigationBar();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_recipe_steps);
        HideNavigationBar();
        Log.v(TAG,"In OnCreate -->"+baking);
        Log.v(TAG,"In OnCreate twoPane-->"+mTwoPane);

        if(savedInstanceState != null) {
            Log.v (TAG, "Restoring State");
            if (savedInstanceState.containsKey (BAKING_KEY)) {
                baking = savedInstanceState.getParcelable (BAKING_KEY);
            }
        }else{
            Intent intent = getIntent ();
            if (intent == null) {
                closeOnError ();
            }

            baking = intent.getParcelableExtra (BAKING_KEY);
            Log.v(TAG,"baking--->"+baking);
            Log.v (TAG,"Recipe Name -->"+baking.getName ());
            /*bakingId = intent.getIntExtra (BAKING_ID,0);
            Log.v (TAG, "RECIPE CLICKED-->" + bakingId);*/

        }
        if(findViewById(R.id.android_me_linear_layout) != null) {
            // This LinearLayout will only initially exist in the two-pane tablet case
            mTwoPane = true;
            if(savedInstanceState==null){
                StepFragment stepFragment = new StepFragment ();
                stepFragment.setSteps (baking.getSteps ());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.step_detail_container, stepFragment)
                        .commit();
            }
        }else {
            mTwoPane=false;
        }
        Log.v(TAG,"In OnCreate twoPane-->"+mTwoPane);
        populateUI(baking);
        RecipeFragment recipeFragment = new RecipeFragment (baking);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.step_container, recipeFragment)
                .commit();
        deleteIngredients();
        insertIngredients(baking);
    }
    private void deleteIngredients() {
        Log.v(TAG,"deleteIngredients-->");
        Uri uri = IngredientContract.IngredientEntry.CONTENT_URI;
        int rows_deleted =  getContentResolver ().delete (uri,null,null);
        Log.v(TAG,"Rows Deleted -->"+rows_deleted);
    }
    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
    private void populateUI(Baking mBaking){
        getSupportActionBar ().setTitle (mBaking.getName ());
    }

    private void insertIngredients(Baking mBaking) {
        ContentValues contentValues = new ContentValues ();
        ListIterator it =mBaking.getIngredients ().listIterator ();
        Log.v(TAG,"Number of Ingredients-->"+mBaking.getIngredients ().size ());
        Log.v(TAG,"Ingredients -->"+mBaking.getIngredients ().toString ());
        int rows=0;
        while(it.hasNext ()){
            Ingredient ingredient = (Ingredient)it.next ();
            contentValues.put(IngredientContract.IngredientEntry.RECIPE_NAME,mBaking.getName ());
            contentValues.put(IngredientContract.IngredientEntry.INGREDIENT,ingredient.getIngredient ());
            contentValues.put(IngredientContract.IngredientEntry.MEASURE,ingredient.getMeasure ());
            contentValues.put(IngredientContract.IngredientEntry.QUANTITY,ingredient.getQuantity ());
            Uri uri = getContentResolver ().insert (IngredientContract.IngredientEntry.CONTENT_URI,contentValues);
            if(uri!=null){
                rows++;
            }
            Log.v(TAG,"INGRE--->"+rows+"."+ingredient.getIngredient ()+" "+ingredient.getQuantity ()+""+ingredient.getMeasure ());
            }

           /* if(rows>0)
            Toast.makeText (getBaseContext (),rows+ "--> ingredients added to DB",Toast.LENGTH_LONG).show ();*/

            UpdateWidget();
    }

    private void UpdateWidget() {
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName (getApplication(), HomeScreenWidgetProvider.class));
        HomeScreenWidgetProvider myWidget = new HomeScreenWidgetProvider ();
        myWidget.onUpdate(this, AppWidgetManager.getInstance(this),ids);
    }

    @Override
    public void onStepSelected(int position) {
        //Toast.makeText(this, "Position clicked = " + position, Toast.LENGTH_SHORT).show();
        Log.v (TAG,"onClick-->----|"+position +"|");
        Log.v(TAG,"In OnCreate twoPane-->"+mTwoPane);
        if (mTwoPane) {
            // Create a new stepFragment
            StepFragment stepFragment = new StepFragment ();
            stepFragment.setSteps (baking.getSteps ());
            stepFragment.setCurrentPosition (position);

            // Add the fragment to its container using a FragmentManager and a Transaction
            getSupportFragmentManager().beginTransaction()
                    .replace (R.id.step_detail_container, stepFragment)
                    .commit();
        }else{
            launchStepDetailActivity (position);
        }

    }
    private void launchStepDetailActivity(int id) {
        Intent intent = new Intent(this, StepDetail.class);
        intent.putExtra (DetailActivity.BAKING_ID,id);
        intent.putExtra(DetailActivity.BAKING_KEY,baking);
        intent.putExtra (StepDetail.STEP_ID,id);
        intent.putParcelableArrayListExtra (StepDetail.STEP_KEY, (ArrayList)baking.getSteps());
        Log.v (TAG,"Recipe sending Object.."+baking.getSteps ().get (id));
        Log.v (TAG,"Recipe sending ..."+baking.getSteps ().get (id).getDescription ());
        startActivity(intent);
    }

    public  void HideNavigationBar(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

}
