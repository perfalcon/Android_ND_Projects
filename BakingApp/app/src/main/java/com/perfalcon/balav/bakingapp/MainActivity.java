package com.perfalcon.balav.bakingapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.perfalcon.balav.bakingapp.IdlingResource.BakingIdlingResource;
import com.perfalcon.balav.bakingapp.model.Baking;
import com.perfalcon.balav.bakingapp.utils.GsonUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipiesLoader.Callback {

    private static final String TAG = MainActivity.class.getName();
    public List<Baking> mBaking;

    // The Idling Resource which will be null in production.
    @Nullable
    private BakingIdlingResource mIdlingResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

    }

    /**
     * Only called from test, creates and returns a new {@link BakingIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public BakingIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new BakingIdlingResource ();
        }
        return mIdlingResource;
    }

    @Override
    protected void onStart() {
        super.onStart ();
        new RecipiesLoader ().downloadRecipe (this, (RecipiesLoader.Callback) MainActivity.this,mIdlingResource);
    }

    protected  void displayBakingDetails(){
        for (Baking baking : mBaking) {
            Log.v(TAG,"Baking -->"+baking.toString ());
        }
        loadRecipesView();
    }

    private void loadRecipesView(){
        RecyclerView rvRecipe = findViewById(R.id.rv_recipe);
        int no_cols= calculateNoOfColumns(this);

        if(no_cols>1){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, no_cols);
            rvRecipe.setLayoutManager (gridLayoutManager);
        }else{
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvRecipe.setLayoutManager (layoutManager);
        }
        rvRecipe.setHasFixedSize(true);
        RecipeAdapter recipeAdapter = new RecipeAdapter (mBaking);
        rvRecipe.setAdapter (recipeAdapter);
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    @Override
    public void onDone(List<Baking> mRecipes) {
        mBaking =mRecipes;
        displayBakingDetails();
    }
}
