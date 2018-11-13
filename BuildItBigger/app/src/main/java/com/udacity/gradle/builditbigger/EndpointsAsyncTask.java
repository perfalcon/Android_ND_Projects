package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.Log;
import android.widget.Toast;

import com.falcon.balav.jokedisplay.JokeActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

import static com.falcon.balav.jokedisplay.JokeActivity.JOKE_KEY;

public class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
private static MyApi myApiService = null;
private Context context;
public static final String TAG = EndpointsAsyncTask.class.getSimpleName ();
public static final String ERROR_MSG="Oops Connection Error ... Please Try again!!!";

@Override
protected String doInBackground(Pair<Context, String>... params) {
        context = params[0].first;
        String flavor = params[0].second;
        Log.v(TAG," Called from: "+flavor);
        if(myApiService == null) {  // Only do this once
        MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
        new AndroidJsonFactory (), null)
        // options for running against local devappserver
        // - 10.0.2.2 is localhost's IP address in Android emulator
        // - turn off compression when running against local devappserver
        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer () {
@Override
public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
        abstractGoogleClientRequest.setDisableGZipContent(true);
        }
        });
        // end options for devappserver

        myApiService = builder.build();
        }

        try {
                return myApiService.joke ().execute().getJoke();
        } catch (IOException e) {
                Log.v(TAG, "Exception Caught, reason:"+ e.getMessage ());
                return null;
        }
        }

@Override
protected void onPostExecute(String result) {
        Log.v(TAG,"post execute -->"+result);
        Intent intent = new Intent(context, JokeActivity.class);
        intent.putExtra(JOKE_KEY, result);
        context.startActivity(intent);
        }
}
