package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.falcon.balav.jokedisplay.JokeActivity;
import com.udacity.gradle.builditbigger.IdlingResource.BuildItBiggerIdlingResource;
//import com.falcon.balav.lib.Jokes;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName ();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {
        Toast.makeText(this, "derp", Toast.LENGTH_SHORT).show();
    }

   /* public void launchJokeActivity(View view) {
        Log.v(TAG, "about to call the jokeActivity");
      *//*  Intent intent = new Intent (this,JokeActivity.class);
        Jokes  jokeSource = new Jokes ();
        String joke = jokeSource.getJoke ();
        intent.putExtra(JokeActivity.JOKE_KEY, joke);
        startActivity(intent);*//*
        Log.v(TAG, " jokeActivity started");
    }*/


}
