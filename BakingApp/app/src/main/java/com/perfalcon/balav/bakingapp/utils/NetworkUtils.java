package com.perfalcon.balav.bakingapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.perfalcon.balav.bakingapp.model.Baking;

import java.util.List;

///NOT USiNG this ...as 7/25
public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private Context mContext;
    private List<Baking> mBaking;
    private String mResponseString;

    private String BAKING_DATA_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    public String sendAndRequestResponse(Context context) {
        mContext = context;
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(mContext);
        //String Request initialized
        mStringRequest = new StringRequest (Request.Method.GET, BAKING_DATA_URL, new ResponseListener(), new ErrorListener());
        mRequestQueue.add(mStringRequest);
        return mResponseString;
    }

    private class ResponseListener implements Response.Listener{
        @Override
        public void onResponse(Object response) {
            Toast.makeText(mContext.getApplicationContext (),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
          // mBaking= new GsonUtils ().populateBaking (response.toString ());
            mResponseString = response.toString ();
        }
    }

    private class ErrorListener implements Response.ErrorListener{
        @Override
        public void onErrorResponse(VolleyError error){
            Log.i(TAG,"Error :" + error.toString());
        }
    }
    /**
     * @param context
     * @return
     */
    public static boolean isConnected(Context context){

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}
