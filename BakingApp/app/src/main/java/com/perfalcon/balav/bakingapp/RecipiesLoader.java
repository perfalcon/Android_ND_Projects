package com.perfalcon.balav.bakingapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class RecipiesLoader {
  private static final String TAG = RecipiesLoader.class.getName();
    static  List<Baking> mRecipes = new ArrayList<> ();
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private Callback mCallback;
    private BakingIdlingResource mIdlingResource;
    private Context mContext;

    private String BAKING_DATA_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    interface Callback{
        void onDone(List<Baking> mRecipies);
    }

    public void downloadRecipe(Context context, final Callback callback,
                              @Nullable final BakingIdlingResource idlingResource) {
        mContext=context;
        mCallback=callback;
        mIdlingResource=idlingResource;

        /**
         * The IdlingResource is null in production as set by the @Nullable annotation which means
         * the value is allowed to be null.
         *
         * If the idle state is true, Espresso can perform the next action.
         * If the idle state is false, Espresso will wait until it is true before
         * performing the next action.
         */
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }


        // Display a toast to let the user know the images are downloading
        String text = context.getString(R.string.loading_msg);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        // Make a Nework call and get the Recipes
        sendAndRequestResponse();
    }

    public void sendAndRequestResponse() {
        mRequestQueue = Volley.newRequestQueue(mContext);
        mStringRequest = new StringRequest (Request.Method.GET, BAKING_DATA_URL, new ResponseListener (), new ErrorListener ());
        mRequestQueue.add(mStringRequest);
    }

    private class ResponseListener implements Response.Listener{
        @Override
        public void onResponse(Object response) {
            mRecipes= new GsonUtils ().populateBaking (response.toString ());
            if (mCallback != null) {
                mCallback.onDone(mRecipes);
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }
        }
    }

    private class ErrorListener implements Response.ErrorListener{
        @Override
        public void onErrorResponse(VolleyError volleyError){
            Log.i(TAG,"Error :" + volleyError.toString());
            String message="";
            if (volleyError instanceof NetworkError) {
                message = "Cannot connect to Internet...Please check your connection!";
            } else if (volleyError instanceof ServerError) {
                message = "The server could not be found. Please try again after some time!!";
            } else if (volleyError instanceof AuthFailureError) {
                message = "Cannot connect to Internet...Please check your connection!";
            } else if (volleyError instanceof ParseError) {
                message = "Parsing error! Please try again after some time!!";
            } else if (volleyError instanceof NoConnectionError) {
                message = "Cannot connect to Internet...Please check your connection!";
            } else if (volleyError instanceof TimeoutError) {
                message = "Connection TimeOut! Please check your internet connection.";
            }
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }
}
