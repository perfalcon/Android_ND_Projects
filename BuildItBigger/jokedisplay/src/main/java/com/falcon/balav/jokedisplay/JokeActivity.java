package com.falcon.balav.jokedisplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class JokeActivity extends AppCompatActivity {

    public static String JOKE_KEY = "Joke key";
    public static final String TAG=JokeActivity.class.getSimpleName ();
    private String sJoke="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG,"In onCreate " );
        setContentView(R.layout.activity_joke);
        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            sJoke = intent.getStringExtra(JOKE_KEY);

        }
       TextView  mJoke = findViewById(R.id.joke_textview);
        mJoke.setText(sJoke);
    }
}
