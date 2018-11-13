package com.example.balav.MovieGridView;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.balav.MovieGridView.model.Movie;
import com.example.balav.MovieGridView.R;
import com.example.balav.MovieGridView.utils.JsonUtils;
import com.example.balav.MovieGridView.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = JsonUtils.class.getSimpleName();
    public static final String MOVIE_ID = "movie_id";
    private static final int DEFAULT_ID = -1;
    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_detail);


        ImageView posterIv = findViewById(R.id.poster_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int movie_id = intent.getIntExtra(MOVIE_ID, DEFAULT_ID);
        if (movie_id == DEFAULT_ID) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }
        if(NetworkUtils.isConnected (DetailActivity.this)){
            loadMovie (movie_id);

        }else{
            posterIv.setVisibility (View.INVISIBLE);populateUI_NoConnection();

        }





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

        ((TextView) findViewById(R.id.title_label_tv)).setVisibility (View.INVISIBLE);
        ((TextView) findViewById(R.id.plot_synopsis_label_tv)).setVisibility (View.INVISIBLE);
        ((TextView)findViewById (R.id.user_rating_label_tv)).setVisibility (View.INVISIBLE);
        ((TextView)findViewById (R.id.relase_date_label_tv)).setVisibility (View.INVISIBLE);


    }
    private void populateUI(Movie movie) {
        TextView mTitle = (TextView) findViewById(R.id.title_tv);
        TextView mPlotSynopsis = (TextView) findViewById(R.id.plot_synopsis_tv);
        TextView mUserRating = (TextView)findViewById (R.id.user_rating_tv);
        TextView mReleaseDate = (TextView)findViewById (R.id.relase_date_tv);
        mTitle.setText (movie.getTitle ());
        mPlotSynopsis.setText (movie.getPlotSynopsis ());
        mUserRating.setText (Double.toString(movie.getUserRating ()));
        mReleaseDate.setText (movie.getReleaseDate ());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void loadMovie(int movie_id){
        new DetailActivity .FetchMovie ().execute(Integer.toString (movie_id));
    }

    public class FetchMovie extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params){
            String movie_id = params[0];
            URL movieUrl = NetworkUtils.buildMovieUrl (Integer.parseInt (movie_id));
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
            Context context = DetailActivity.this;
            movie= JsonUtils.parseMovieJson (movieResults);
            Log.v(TAG,"--->"+movie.getTitle ());
            Log.v(TAG,"-->"+movie.getPlotSynopsis ());
            populateUI(movie);

            Picasso.with(DetailActivity.this)
                    .load(NetworkUtils.buildImageUrl (movie.getPosterPath ().replaceFirst ("/",""),NetworkUtils.IMAGE_SIZE_500))
                    //.placeholder(R.drawable.user_placeholder)
                    //.error(R.drawable.user_placeholder_error)
                    .into((ImageView)findViewById(R.id.poster_iv));
        }
    }
}
