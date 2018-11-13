package com.example.balav.moviegridstage2.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.balav.moviegridstage2.Constants;
import com.example.balav.moviegridstage2.model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class JsonUtils {
    private static final String TAG = JsonUtils.class.getSimpleName();

    public static Movie parseMovieJson(String json) {

        Log.v ("json",json);
        Movie movie = new Movie ();
        try{
            JSONObject jsonObject = new JSONObject (json);
            movie.setPosterPath (jsonObject.getString (Constants.POSTER_PATH));
            Log.v (TAG,"Poster-->"+jsonObject.getString (Constants.POSTER_PATH));
            Log.v(TAG,"ID -->"+jsonObject.getInt (Constants.MOVIE_ID));
            movie.setId (jsonObject.getInt (Constants.MOVIE_ID));
            movie.setTitle (jsonObject.getString (Constants.ORIGINAL_TITLE));
            Log.v (TAG, jsonObject.getString (Constants.ORIGINAL_TITLE));
            movie.setPlotSynopsis (jsonObject.getString (Constants.PLOT_SYNOPSIS));
            movie.setReleaseDate (jsonObject.getString (Constants.RELEASE_DATE));
            movie.setUserRating (jsonObject.getDouble (Constants.USER_RATING));
            movie.setTrailers (jsonObject.getString (Constants.TRAILER_VIDEO));
            movie.setReviews(jsonObject.getString (Constants.REVIEWS));
            movie.setDuration (jsonObject.getInt(Constants.DURATION));
        }catch (Exception e){
            Log.v(TAG,e.getMessage ());
            return null;
        }
        return movie;
    }
    public static List<String> getPosterImages(String json){
        List<String> listPosterImages = new ArrayList<> ();
        try{
            JSONObject jsonObject = new JSONObject (json);
            List<String> listMovies = new ArrayList<> ();
            JSONArray movies =  jsonObject.getJSONArray ("results");
            Log.v ("Movies -->",""+movies.length ());
            for(int i=0;i<movies.length ();i++){
                JSONObject jsonMovie = movies.getJSONObject (i);
                Log.v (TAG,"Poster-->"+jsonMovie.getString ("poster_path"));
                listPosterImages.add (jsonMovie.getString ("poster_path").replaceFirst ("/",""));
            }
        }catch (Exception e){
            Log.v(TAG,e.getMessage ());
            return null;
        }
        return listPosterImages;
    }

    public static List<Integer> getMoviesIDs(String movieResults) {
        List<Integer> listIDs = new ArrayList<> ();
        try{
            JSONObject jsonObject = new JSONObject (movieResults);
            List<String> listMovies = new ArrayList<> ();
            JSONArray movies =  jsonObject.getJSONArray ("results");
            Log.v ("Movies -->",""+movies.length ());
            for(int i=0;i<movies.length ();i++){
                JSONObject jsonMovie = movies.getJSONObject (i);
                Log.v (TAG,"Poster-->"+jsonMovie.getInt ("id"));
                listIDs.add (jsonMovie.getInt ("id"));
            }
        }catch (Exception e){
            Log.v(TAG,e.getMessage ());
            return null;
        }
        return listIDs;
    }
    public static List<String> getVideoKeys(String videos){
        List<String> listVideoKeys= new ArrayList<String> ();
        try{
            JSONArray trailers =  new JSONObject (videos).getJSONArray ("results");
            for(int i=0; i<trailers.length (); i++){
                JSONObject jsonTrailer = trailers.getJSONObject (i);
                listVideoKeys.add (jsonTrailer.getString (Constants.KEY));
            }
        }
        catch (Exception e){
            Log.v(TAG,e.getMessage ());
            return null;
        }
        Log.v (TAG,"VideoKeys-->"+ TextUtils.join(",", listVideoKeys));
        return listVideoKeys;
    }

    public static JSONArray getReviews(String reviews){
        JSONArray newReviews = new JSONArray ();
        try{
            JSONArray jsonArrayReviews =  new JSONObject (reviews).getJSONArray ("results");
            Log.v (TAG,"No of Reviews-->"+jsonArrayReviews.length ());
            for(int i=0; i<jsonArrayReviews.length (); i++){
                JSONObject jsonReview = jsonArrayReviews.getJSONObject (i);
                newReviews.put(new JSONObject ().put (Constants.CONTENT,jsonReview.getString (Constants.CONTENT)));
            }
            Log.v (TAG,"list  Reviews-->"+ newReviews.join ("|||"));
        }
        catch (Exception e){
            Log.v(TAG,e.getMessage ());
            //return null;
        }
        return newReviews;
    }
}
