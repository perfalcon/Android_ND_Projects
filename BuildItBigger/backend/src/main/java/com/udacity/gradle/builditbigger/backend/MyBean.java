package com.udacity.gradle.builditbigger.backend;
import com.falcon.balav.lib.Jokes;

/** The object model for the data we are sending through endpoints */
public class MyBean {
    private Jokes mJokes;
    MyBean(){
        mJokes = new Jokes ();
    }
    public String getJoke() {
        return mJokes.getJoke();
    }
}