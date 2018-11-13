package com.example.balav.MovieGridView.utils;

import android.util.Log;

import com.example.balav.MovieGridView.model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class JsonUtils {
    private static final String TAG = JsonUtils.class.getSimpleName();

    public static Movie parseMovieJson(String json) {

        Log.v ("json",json.toString ());
        Movie movie = new Movie ();
        try{
            JSONObject jsonObject = new JSONObject (json);
            movie.setPosterPath (jsonObject.getString ("poster_path"));
            Log.v (TAG,"Poster-->"+jsonObject.getString ("poster_path"));
            movie.setId (jsonObject.getInt ("id"));
            movie.setTitle (jsonObject.getString ("original_title"));
            Log.v (TAG, jsonObject.getString ("original_title"));
            movie.setPlotSynopsis (jsonObject.getString ("overview"));
            movie.setReleaseDate (jsonObject.getString ("release_date"));
            movie.setUserRating (jsonObject.getDouble ("vote_average"));

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
}
