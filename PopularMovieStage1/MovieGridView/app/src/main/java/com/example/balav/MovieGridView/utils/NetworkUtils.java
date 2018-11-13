/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.balav.MovieGridView.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.example.balav.MovieGridView.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_BASE_URL =
            "https://api.themoviedb.org/3/movie/";
    private static final String POPULAR="popular";
    private static final String TOP_RATED="top_rated";
    private static final String MOVIE="movie";
    private static final String API_KEY="api_key";
    private static final String API_VAL= BuildConfig.API_VAL;
    private static final String IMAGE_BASE_URL="http://image.tmdb.org/t/p/";
    private static final String IMAGE_WIDTH_185="w185";
    private static final String IMAGE_WIDTH_342="w342";
    private static final String IMAGE_WIDTH_500="w500";

    public static final int IMAGE_SIZE_185=185;
    public static final int IMAGE_SIZE_342=342;
    public static final int IMAGE_SIZE_500=500;






    /**
     * Builds the URL used to talk to the weather server using a location. This location is based
     * on the query capabilities of the weather provider that we are using.
     *
     * @param movies  specifies what to query for : Top_rated or most popular.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String movies) {
        Uri builtUri;
        switch (movies){
            case POPULAR:
                 builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath (POPULAR)
                        .appendQueryParameter(API_KEY,API_VAL)
                        .build();
                break;
            case TOP_RATED:
                 builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath (TOP_RATED)
                        .appendQueryParameter(API_KEY,API_VAL)
                        .build();
                break;
            default:
                 builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath (POPULAR)
                        .appendQueryParameter(API_KEY,API_VAL)
                        .build();
                 break;
        }
        URL url = null;
        try{
            url=new URL (builtUri.toString());
            }catch (MalformedURLException e){
            e.printStackTrace();
        }
        if(url !=null){
            Log.v(TAG,"url-->"+url.toString ());
        }
        return url;
    }
    public static Uri buildImageUrl(String image, int imageSize) {
        Uri builtImageUri;

        String strImageSize=IMAGE_WIDTH_185;
        switch (imageSize){
            case IMAGE_SIZE_185:
                strImageSize = IMAGE_WIDTH_185;break;
            case IMAGE_SIZE_342:
                strImageSize = IMAGE_WIDTH_342;break;
            case IMAGE_SIZE_500:
                strImageSize = IMAGE_WIDTH_500;break;
            default:
                strImageSize=IMAGE_WIDTH_185;break;
        }

                builtImageUri = Uri.parse (IMAGE_BASE_URL).buildUpon ()
                        .appendPath (strImageSize)
                        .appendPath (image)
                        .build ();
        URL url = null;
        try{
            url=new URL (builtImageUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        if(url !=null){
            Log.v(TAG,"url-->"+url.toString ());
        }
        return builtImageUri;
    }
    public static URL buildMovieUrl(int id) {
        Uri movieUri;
        movieUri = Uri.parse (MOVIE_BASE_URL).buildUpon ()
                .appendPath (Integer.toString (id))
                .appendQueryParameter(API_KEY,API_VAL)
                .build ();
        URL url = null;
        try{
            url=new URL (movieUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        if(url !=null){
            Log.v(TAG,"url-->"+url.toString ());
        }
        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        Log.v(TAG,"start of getresponsefrom HTTP URLt");

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner (in);
            scanner.useDelimiter("\\A");
            Log.v(TAG,"before-scanner-next");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                String response=scanner.next ();
                Log.v(TAG,"response text-->"+response);
                //return scanner.next();
                return response;
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean isConnected(Context context){

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}