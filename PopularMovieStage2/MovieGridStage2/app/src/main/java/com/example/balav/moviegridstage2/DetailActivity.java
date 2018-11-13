package com.example.balav.moviegridstage2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.balav.moviegridstage2.data.FavoriteMoviesContentProvider;
import com.example.balav.moviegridstage2.data.FavoriteMoviesContract;
import com.example.balav.moviegridstage2.model.Movie;
import com.example.balav.moviegridstage2.utils.JsonUtils;
import com.example.balav.moviegridstage2.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements  CompoundButton.OnCheckedChangeListener {
    private static final String TAG = DetailActivity.class.getSimpleName();
    public static final String MOVIE_ID = "movie_id";
    private static final int DEFAULT_ID = -1;
    public static final String OPTION_SELECTED="optionSelected";
    private static final int MOVIE_LOADER_ID=1;
    private static final  String MOVIE_KEY="movie";
    private Movie movie;
    private FavoriteMoviesContentProvider favoriteMoviesContentProvider;
    private Cursor cursor;
    private boolean bFav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_detail);
        ImageView posterIv = findViewById(R.id.poster_iv);

        if(savedInstanceState != null) {
            Log.v (TAG, "Restoring State");
            if(savedInstanceState.containsKey(MOVIE_KEY)){
              movie =savedInstanceState.getParcelable (MOVIE_KEY);
            }
            if(savedInstanceState.containsKey (OPTION_SELECTED)){
                bFav=savedInstanceState.getBoolean (OPTION_SELECTED);
            }
            populateUI(movie);
            showMoviePoster ();
        }else {

            Intent intent = getIntent ();
            if (intent == null) {
                closeOnError ();
            }
            Movie fromIntent = intent.getParcelableExtra (MOVIE_ID);
            if (fromIntent == null) {
                closeOnError ();
            }
            int movie_id = fromIntent.getId ();


            Log.v (TAG, "OPTION_SELECTED-->" + intent.getStringExtra (OPTION_SELECTED));
            bFav = intent.getStringExtra (OPTION_SELECTED).equals ("favorites");

            Log.v (TAG, "bFav-->" + bFav);


            if (NetworkUtils.isConnected (DetailActivity.this)) {
                loadMovie (movie_id);
            } else {
                Log.v (TAG, "NO INTERNET CONNECTION ... but can show Favorite Movies");
                if (bFav) {//if Favorite movies selected
                    loadMovie (movie_id);
                } else {
                    posterIv.setVisibility (View.INVISIBLE);
                    populateUI_NoConnection ();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState (outState);
        Log.v (TAG, "Saving the State");
        outState.putParcelable (MOVIE_KEY,movie);
        outState.putBoolean (OPTION_SELECTED, bFav);
    }
    private void populateUI_NoConnection(){
        TextView mTitle = (TextView) findViewById(R.id.title_tv);
        TextView mPlotSynopsis = (TextView) findViewById(R.id.plot_synopsis_tv);
        TextView mUserRating = (TextView)findViewById (R.id.user_rating_tv);
        TextView mReleaseDate = (TextView)findViewById (R.id.relase_date_tv);
        mTitle.setText ("Please Check your Internet Connection");
        mTitle.setTextSize (16);
        mTitle.setTextColor (Color.RED);
        mPlotSynopsis.setVisibility (View.INVISIBLE);
        mUserRating.setVisibility (View.INVISIBLE);
        mReleaseDate.setVisibility (View.INVISIBLE);

        ((TextView)findViewById (R.id.duration_tv)).setVisibility (View.INVISIBLE);
        ((TextView)findViewById (R.id.trailer_label)).setVisibility (View.INVISIBLE);
        ((TextView)findViewById (R.id.review_label)).setVisibility (View.INVISIBLE);
        ((ToggleButton)findViewById (R.id.favorite_tb)).setVisibility (View.INVISIBLE);
        ((RecyclerView)findViewById (R.id.rv_trailers)).setVisibility (View.INVISIBLE);
        ((RecyclerView)findViewById (R.id.rv_reviews)).setVisibility (View.INVISIBLE);
    }
    public  void OnShareClicked(View v){
        String shareText = (String) v.getTag ();
        Log.v(TAG,"getTag --> shareText-->"+shareText);
        String mimeType ="text/plain";
        String title="Share...";
        ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(shareText)
                .startChooser();
    }

    private void populateUI(Movie movie) {
        TextView mTitle = (TextView) findViewById(R.id.title_tv);
        TextView mPlotSynopsis = (TextView) findViewById(R.id.plot_synopsis_tv);
        TextView mUserRating = (TextView)findViewById (R.id.user_rating_tv);
        TextView mReleaseDate = (TextView)findViewById (R.id.relase_date_tv);
        TextView mDuration=(TextView)findViewById (R.id.duration_tv);
        mTitle.setText (movie.getTitle ());
        mPlotSynopsis.setText (movie.getPlotSynopsis ());
        mUserRating.setText (Double.toString(movie.getUserRating ())+"/10");
        mReleaseDate.setText (movie.getReleaseDate ().split ("-")[0]);
        mDuration.setText (String.valueOf (movie.getDuration ()+"min"));
        loadTrailerView();
        loadReviewView();

        ToggleButton toggle = (ToggleButton) findViewById(R.id.favorite_tb);
        if(bFav){
            toggle.setChecked (true);
        }else{
            //check the movie is in favorite DB
            if(isMovieInFavDB (movie.getId ())){
                toggle.setChecked (true);
            }
        }
        toggle.setOnCheckedChangeListener(DetailActivity.this);
        favoriteMoviesContentProvider = new FavoriteMoviesContentProvider ();
    }

    private boolean isMovieInFavDB(int movie_id){
        Log.v(TAG,"isMovieInDB-->"+movie_id);
        Cursor cursor= getContentResolver ().query (FavoriteMoviesContract.FavMovieEntry.CONTENT_URI.buildUpon ().appendPath (Integer.toString (movie_id)).build (),
                null,
                null,
                null,
                null);
        if(cursor.getCount ()>0){
            cursor.moveToNext ();
            int movieIdIndex= cursor.getColumnIndex (FavoriteMoviesContract.FavMovieEntry.MOVIE_ID);
            boolean flag=cursor.getInt (movieIdIndex)== movie_id;
            cursor.close ();
            return flag;
        }
        cursor.close ();
        return false;
    }

    private void loadTrailerView(){
        RecyclerView  rvTrailer = (RecyclerView) findViewById(R.id.rv_trailers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTrailer.setLayoutManager (layoutManager);
        List<String> listTrailers = Arrays.asList (movie.getTrailers ().split (","));
        Context context = DetailActivity.this;
        TrailerAdapter adapterTrailer =  new TrailerAdapter (listTrailers);
        rvTrailer.setAdapter (adapterTrailer);
    }

    private void loadReviewView(){
        RecyclerView  rvReview = (RecyclerView) findViewById(R.id.rv_reviews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvReview.setLayoutManager (layoutManager);
        List<String> listReviews= new ArrayList<String> ();
        Log.v (TAG,"Review -->"+movie.getReviews ());
        try {
            JSONArray jsonArrayReviews =  new JSONArray (movie.getReviews ());
            for(int i=0; i<jsonArrayReviews.length (); i++){
                JSONObject jsonReview = jsonArrayReviews.getJSONObject (i);
                listReviews.add (jsonReview.getString (Constants.CONTENT));
            }
        } catch (JSONException e) {
            e.printStackTrace ();
            Log.v (TAG,"Exception occurred while retrieving reviews ... ");
        }
        Context context = DetailActivity.this;
        ReviewAdapter adapterReview =  new ReviewAdapter (listReviews);
        rvReview.setAdapter (adapterReview);
    }


    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void loadMovie(int movie_id){
        new DetailActivity .FetchMovie ().execute(Integer.toString (movie_id));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            Log.v(TAG,"Marked the movie as favorite-->"+movie.getTitle ());
            //Check if it exists already in the DB, otherwise insert
            if(!isMovieInFavDB (movie.getId ())){
                Log.v (TAG,"onCheckChanges-->"+movie.getId()+" --"+movie.getTitle ());
                insertMovie ();
            }

        }else{
            Log.v(TAG,"Un Marked the movie as favorite-->"+movie.getTitle ());
            //Have to Delete from DB.
            if(!deleteMovieInFavDB (movie.getId ())){
                //if movie is not deleted from Fav DB, keep Favorite status unchanged.
                ToggleButton toggle = (ToggleButton) findViewById(R.id.favorite_tb);
                toggle.setChecked (true);
            }
        }
    }

    private boolean deleteMovieInFavDB(int movie_id){
        Log.v(TAG,"deleteMovieInDB-->"+movie_id);
        Uri uri = FavoriteMoviesContract.FavMovieEntry.CONTENT_URI;
        uri = uri.buildUpon ().appendPath (Integer.toString (movie_id)).build ();
        int rows_deleted= getContentResolver ().delete (uri,null,null);
        return rows_deleted>0;
    }

    private void insertMovie(){
        ContentValues contentValues = new ContentValues ();
        contentValues.put (FavoriteMoviesContract.FavMovieEntry.MOVIE_ID,movie.getId ());
        contentValues.put (FavoriteMoviesContract.FavMovieEntry.TITLE,movie.getTitle ());
        contentValues.put(FavoriteMoviesContract.FavMovieEntry.PLOT_SYNOPSIS,movie.getPlotSynopsis ());
        contentValues.put(FavoriteMoviesContract.FavMovieEntry.POSTER_PATH,movie.getPosterPath ());
        contentValues.put(FavoriteMoviesContract.FavMovieEntry.RELEASE_DATE,movie.getReleaseDate ());
        contentValues.put(FavoriteMoviesContract.FavMovieEntry.USER_RATING,movie.getUserRating ());
        contentValues.put(FavoriteMoviesContract.FavMovieEntry.TRAILERS,movie.getTrailers ());
        contentValues.put(FavoriteMoviesContract.FavMovieEntry.REVIEWS,movie.getReviews ());
        contentValues.put(FavoriteMoviesContract.FavMovieEntry.DURATION,movie.getDuration ());

        Uri uri = getContentResolver ().insert (FavoriteMoviesContract.FavMovieEntry.CONTENT_URI,contentValues);
        if(uri!=null){
            Toast.makeText (getBaseContext (),R.string.fav_add,Toast.LENGTH_LONG).show ();
        }
        finish();
    }



    public class FetchMovie extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params){
            String movie_id = params[0];
            URL movieUrl = NetworkUtils.buildMovieUrl (Integer.parseInt (movie_id));
            URL trailerUrl = NetworkUtils.buildTrailerUrl (Integer.parseInt (movie_id));
            URL reviewUrl= NetworkUtils.buildReviewUrl (Integer.parseInt (movie_id));
            String movieResults=null;
            String trailers=null;
            String reviews = null;
            JSONObject jsonObject =null;
            try{
                if(bFav){
                    cursor= getContentResolver ().query (FavoriteMoviesContract.FavMovieEntry.CONTENT_URI.buildUpon ().appendPath (movie_id).build (),
                            null,
                            null,
                            null,
                            null);
                    Log.v (TAG, "cursor -->"+cursor.getCount ());
                }else{
                    movieResults= NetworkUtils.getResponseFromHttpUrl(movieUrl);
                    Log.v(TAG,"movieResults---->"+movieResults);
                    jsonObject = new JSONObject (movieResults);
                    List<String> listVideoKeys=JsonUtils.getVideoKeys (NetworkUtils.getResponseFromHttpUrl(trailerUrl));
                    jsonObject.put(Constants.TRAILER_VIDEO, TextUtils.join(",",listVideoKeys));
                    jsonObject.put(Constants.REVIEWS,JsonUtils.getReviews (NetworkUtils.getResponseFromHttpUrl(reviewUrl)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace ();
            } catch (Exception e){
                e.printStackTrace ();
            }
            return jsonObject != null?jsonObject.toString ():"";
        }

        @Override
        protected void onPostExecute(String movieResults){
            Context context = DetailActivity.this;
            if(bFav){
                movie = createMovieFromCursor(cursor);
            }else {
                Log.v("onPostExecute-->",movieResults);
                movie= JsonUtils.parseMovieJson (movieResults);
            }
            populateUI(movie);
            showMoviePoster ();
        }

        public Movie createMovieFromCursor(Cursor c){
            cursor.moveToFirst();
            int posterPathIndex = cursor.getColumnIndex(FavoriteMoviesContract.FavMovieEntry.POSTER_PATH);
            int movieIdIndex= cursor.getColumnIndex (FavoriteMoviesContract.FavMovieEntry.MOVIE_ID);
            int titleIndex=cursor.getColumnIndex (FavoriteMoviesContract.FavMovieEntry.TITLE);
            int plotSynopsisIndex=cursor.getColumnIndex (FavoriteMoviesContract.FavMovieEntry.PLOT_SYNOPSIS);
            int releaseDateIndex = cursor.getColumnIndex (FavoriteMoviesContract.FavMovieEntry.RELEASE_DATE);
            int userRatingIndex=cursor.getColumnIndex (FavoriteMoviesContract.FavMovieEntry.USER_RATING);
            int trailersIndex=cursor.getColumnIndex (FavoriteMoviesContract.FavMovieEntry.TRAILERS);
            int reviewsIndex=cursor.getColumnIndex (FavoriteMoviesContract.FavMovieEntry.REVIEWS);
            int durationIndex=cursor.getColumnIndex (FavoriteMoviesContract.FavMovieEntry.DURATION);
            Movie movie = new Movie ();
            movie.setId (cursor.getInt (movieIdIndex));
            movie.setTitle (cursor.getString (titleIndex));
            movie.setPlotSynopsis (cursor.getString (plotSynopsisIndex));
            movie.setPosterPath (cursor.getString (posterPathIndex));
            movie.setReleaseDate (cursor.getString (releaseDateIndex));
            movie.setUserRating (Double.parseDouble((cursor.getString (userRatingIndex))));
            movie.setTrailers (cursor.getString (trailersIndex));
            movie.setReviews (cursor.getString (reviewsIndex));
            movie.setDuration (cursor.getInt (durationIndex));
            return movie;
        }


    }


    public void showMoviePoster(){
        Picasso.with(DetailActivity.this)
                .load(NetworkUtils.buildImageUrl (movie.getPosterPath ().replaceFirst ("/",""),NetworkUtils.IMAGE_SIZE_500))
                //.placeholder(R.drawable.user_placeholder)
                //.error(R.drawable.user_placeholder_error)
                .into((ImageView)findViewById(R.id.poster_iv));
    }
}
