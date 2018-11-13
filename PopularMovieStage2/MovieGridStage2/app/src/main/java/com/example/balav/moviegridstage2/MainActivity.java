package com.example.balav.moviegridstage2;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.balav.moviegridstage2.data.FavoriteMoviesContract;
import com.example.balav.moviegridstage2.utils.JsonUtils;
import com.example.balav.moviegridstage2.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<String> listPosterUrls;
    private List<Integer> listIDs;
    private static final String MOVIE_SORT_POPULAR="popular";
    private static final String MOVIE_SORT_TOP_RATED="top_rated";
    private static final String MOVIE_SORT_FAVORITES="favorites";


    private static final int MOVIE_LOADER_ID = 0;

    private MovieAdapter mAdapter;
    private RecyclerView mImageGrid;
    private Cursor mMovieData;

    private String optionSelected=MOVIE_SORT_POPULAR;


    private static final String GRID_SCROLL_POSITION="grid_scroll_position";
    private static final String LIST_IDS_KEY="list_ids_key";
    private static final String LIST_POSTER_URLS="list_poster_urls";
    private static final int NUM_LIST_ITEMS = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        if(savedInstanceState != null){
            Log.v(TAG,"Restoring State");
            loadGridView ();
            if(savedInstanceState.containsKey(GRID_SCROLL_POSITION)){
                mImageGrid.getLayoutManager ().onRestoreInstanceState (savedInstanceState.getParcelable (GRID_SCROLL_POSITION));
            }
            if(savedInstanceState.containsKey (LIST_POSTER_URLS)){
                listPosterUrls = savedInstanceState.getStringArrayList (LIST_POSTER_URLS);
            }
            if(savedInstanceState.containsKey (LIST_IDS_KEY)){
                listIDs=savedInstanceState.getIntegerArrayList (LIST_IDS_KEY);
            }
            hookAdapterGrid();
        }else{
            loadMovies(MOVIE_SORT_POPULAR);
       }

    }


    private void loadGridView(){
        Log.v(TAG,"Calling loadGridView-->option-->"+optionSelected);
        int no_cols= calculateNoOfColumns(this);
        Log.v(TAG,"No of cols-->"+no_cols);
        mImageGrid = findViewById(R.id.rv_numbers);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, no_cols);
        mImageGrid.setLayoutManager(gridLayoutManager);
        mImageGrid.setHasFixedSize(true);
    }

    private void hookAdapterGrid(){
        mAdapter = new MovieAdapter(this,listPosterUrls,listIDs,optionSelected);
        mImageGrid.setAdapter(mAdapter);
    }

    private void loadMovies(String movie_type){
        if(NetworkUtils.isConnected (MainActivity.this)){
            displayErrorMessage(View.INVISIBLE);
            if(movie_type.equals (MOVIE_SORT_FAVORITES)){
                Log.v (TAG,"if of movie type-->"+movie_type);
                //getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
                activateLoader ();
            }else{
                new FetchMovieTask ().execute(movie_type);
            }
        }else {
            Log.v (TAG,"NO INTERNET CONNECTION -- else");
            if(movie_type.equals (MOVIE_SORT_FAVORITES)){
                Toast.makeText(this, R.string.internet_error_message, Toast.LENGTH_SHORT).show();
               activateLoader ();
            }else{
                displayErrorMessage(View.VISIBLE);
            }
        }
    }

    private void activateLoader(){
        Log.v(TAG,"[activateLoader]");
        if(getSupportLoaderManager ().hasRunningLoaders ()){
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        }else{
            getSupportLoaderManager ().restartLoader (MOVIE_LOADER_ID, null, this);
        }
    }

     private void displayErrorMessage(int flag){
        Log.v ("MainActivity","displayErrorMessage");
        TextView mMsg = findViewById(R.id.messae_tv);
            mMsg.setVisibility (flag);
            mMsg.setTextColor (Color.RED);
       /* if(flag == View.INVISIBLE){
            mImageGrid.setVisibility (View.VISIBLE);  //.setAlpha (0);
        }else{
            mImageGrid.setVisibility (View.INVISIBLE);//.setAlpha (1);
        }*/


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.v (TAG,"on create loader");
        return  new AsyncTaskLoader<Cursor> (this) {

            // Initialize a Cursor, this will hold all the task data
           Cursor mMovieData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mMovieData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data
                try{
                    return getContentResolver ().query (FavoriteMoviesContract.FavMovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                }catch(Exception e){
                    Log.e(TAG,"failed to asynchronously load data");
                    e.printStackTrace ();
                    return null;
                }


            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(mMovieData);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.v (TAG,"[onLoadFinished]--");
        if(optionSelected.equals (MOVIE_SORT_FAVORITES)){
            if(data.getCount ()>0){
                loadMoviesFromCursor(data);
            }else{
                    Toast.makeText(this, R.string.no_favorites_message, Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void loadMoviesFromCursor(Cursor data) {
        Log.v (TAG,"loadMovieFromCursor--> Cursor Size-->"+data.getCount ());
        int posterPathIndex = data.getColumnIndex(FavoriteMoviesContract.FavMovieEntry.POSTER_PATH);
        int movieIdIndex= data.getColumnIndex (FavoriteMoviesContract.FavMovieEntry.MOVIE_ID);
        List<String> posterUrls=new ArrayList<String> ();
        List<Integer> movieIds = new ArrayList<Integer> ();
        Log.v(TAG,"Current Position of cursor"+data.getPosition ());
        data.moveToFirst ();
        for(int i=0;i<data.getCount ();i++){
            Log.v (TAG,"[loadMoviesFromCursor]-->"+data.getInt (movieIdIndex)+"-----"+data.getString (posterPathIndex));
            posterUrls.add (data.getString (posterPathIndex).replaceFirst ("/",""));
            movieIds.add (data.getInt (movieIdIndex));
            data.moveToNext ();
            Log.v(TAG,"while --- Current Position of cursor"+data.getPosition ());
        }
        listPosterUrls = posterUrls;
        listIDs=movieIds;
        loadGridView();
        hookAdapterGrid ();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
       // loadMoviesFromCursor(data);
    }


    public class FetchMovieTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params){
            String movie_type = params[0];
            URL movieUrl = NetworkUtils.buildUrl(movie_type);
            String movieResults=null;
            try{
                movieResults= NetworkUtils.getResponseFromHttpUrl(movieUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieResults;
        }
        @Override
        protected void onPostExecute(String movieResults){
            Log.v("onPostExecute-->",movieResults.toString ());
            Context context = MainActivity.this;
            listPosterUrls= JsonUtils.getPosterImages (movieResults);
            listIDs = JsonUtils.getMoviesIDs (movieResults);
            Log.v ("PosterUrlsSize--->", ""+listPosterUrls.size ());
            loadGridView();
            hookAdapterGrid();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_preferences,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_popular:
                optionSelected=MOVIE_SORT_POPULAR;
                loadMovies (MOVIE_SORT_POPULAR);
                break;
            case R.id.action_top_rated:
                optionSelected=MOVIE_SORT_TOP_RATED;
                loadMovies (MOVIE_SORT_TOP_RATED);
                break;
            case R.id.action_favorites:
                optionSelected=MOVIE_SORT_FAVORITES;
                loadMovies (MOVIE_SORT_FAVORITES);
                break;
            default:
                optionSelected=MOVIE_SORT_POPULAR;
                loadMovies (MOVIE_SORT_POPULAR);
                break;
        }
        Log.v (TAG,"onOptionsItemSelected optionSelected-->"+optionSelected);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy ();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState (outState);
        Log.v(TAG,"Saving the State");

        outState.putParcelable (GRID_SCROLL_POSITION, mImageGrid.getLayoutManager ().onSaveInstanceState ());
        outState.putStringArrayList (LIST_IDS_KEY,(ArrayList)listIDs);
        outState.putStringArrayList (LIST_POSTER_URLS,(ArrayList)listPosterUrls);

    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }
}
