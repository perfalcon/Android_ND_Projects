package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {

        //json.toString ();
        Log.v ("json",json.toString ());
        Sandwich sandwich = new Sandwich ();
        try{

            JSONObject jsonObject = new JSONObject(json);
            JSONObject name = (JSONObject) jsonObject.get("name");
            sandwich.setMainName (name.getString ("mainName"));
            List<String> listAlsoKnownAs = new ArrayList<> ();
            JSONArray alsoKnownAs =  name.getJSONArray ("alsoKnownAs");
            for(int i=0;i<alsoKnownAs.length ();i++){
                listAlsoKnownAs.add (alsoKnownAs.getString (i));
            }
            sandwich.setAlsoKnownAs (listAlsoKnownAs);
            sandwich.setPlaceOfOrigin ( jsonObject.getString ("placeOfOrigin"));
            sandwich.setDescription (jsonObject.getString ("description"));
            sandwich.setImage (jsonObject.getString ("image"));
            JSONArray ingredients = (JSONArray) jsonObject.get("ingredients");
            List<String> listIngredients= new ArrayList<> ();
            for (int i = 0; i < ingredients.length(); i++) {
                listIngredients.add (ingredients.getString (i));
            }
            sandwich.setIngredients (listIngredients);

        }catch (Exception e){
            Log.v("exception ",e.getMessage ());
            return null;
        }
        return sandwich;
    }
}
