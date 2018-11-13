package com.perfalcon.balav.bakingapp;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.perfalcon.balav.bakingapp.model.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

public class StepFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = StepFragment.class.getSimpleName ();
    public static final String STEP_KEY="step";
    public static final String STEP_ID="step_id";
    public static final String PLAYER_STATE="player_state";
    public static final String PLAYER_POSITION="player_position";
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private int[] mButtonIDs = {R.id.btn_prev_step, R.id.btn_next_step};
    private Button[] mButtons;
    private  int current_position;
    private boolean  isPlayWhenReady;
    private List<Step> listSteps;
    private View rootView;
    TextView mRecipeName;
    public long currentMediaPosition = C.TIME_UNSET;
    Uri videoURI;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG,"in Step Detail");
        int step_id=0;
        if(savedInstanceState!=null){
            Log.v (TAG, "Restoring State");
            current_position = savedInstanceState.getInt (STEP_ID);
            listSteps =savedInstanceState.getParcelableArrayList (STEP_KEY);
            currentMediaPosition = savedInstanceState.getLong(PLAYER_POSITION);
            isPlayWhenReady = savedInstanceState.getBoolean (PLAYER_STATE);

        }
        rootView = inflater.inflate(R.layout.step_detail, container, false);
        if(listSteps!=null && listSteps.size ()>0 ){
            Log.v(TAG,"step--->"+listSteps.get (current_position));
            Log.v (TAG,"Step Name -->"+listSteps.get (current_position).getShortDescription ());
            populateUI(listSteps.get (current_position));
         }
        return rootView;
    }

    public void setSteps(List<Step> listSteps){     this.listSteps =listSteps;    }
    public void setCurrentPosition(int position){      this.current_position = position;    }

    private void populateUI(Step step) {
        Log.v(TAG,"Calling Populate UI -->"+step.toString ());
        if(!isLandScape ()){
            mRecipeName = rootView.findViewById(R.id.tv_step_description);
            mRecipeName.setText(step.getDescription ());
            // Initialize the buttons with the composers names.
            mButtons = initializeButtons();
        }

        // Initialize the player view.
        mPlayerView = rootView.findViewById(R.id.playerView);
        Log.v (TAG,"The Video-->"+step.getVideoURL ());
        // Initialize the player.
        if(step.getVideoURL ()!=null && !step.getVideoURL ().isEmpty ()){
            Log.v (TAG,"Updating the Video-->"+step.getVideoURL ());
            mPlayerView.setVisibility (View.VISIBLE);
            videoURI=Uri.parse(step.getVideoURL ());
            initializePlayer(videoURI);
        }else {
            mExoPlayer=null;
            Log.v(TAG,"No Video -->ExoPlayer-->"+mExoPlayer);
            mPlayerView.setVisibility (View.INVISIBLE);
        }
    }
    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     *
     */
    private void initializePlayer(Uri mediaUri) {
        // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(rootView.getContext (), "BakingApp");
        MediaSource mediaSource = new ExtractorMediaSource (mediaUri, new DefaultDataSourceFactory (
                rootView.getContext (), userAgent), new DefaultExtractorsFactory (), null, null);
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector ();
            LoadControl loadControl = new DefaultLoadControl ();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(rootView.getContext (), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            if (currentMediaPosition != C.TIME_UNSET) mExoPlayer.seekTo(currentMediaPosition);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(isPlayWhenReady);
        }

    }


    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    private void hardReleasePlayer() {
        if (mExoPlayer != null) {
            currentMediaPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void softReleasePlayer(){
        if (mExoPlayer != null) {
            currentMediaPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer(videoURI);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mExoPlayer != null && videoURI != null) {
            initializePlayer(videoURI);
            mExoPlayer.seekTo(currentMediaPosition);
        }
    }

    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hardReleasePlayer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        softReleasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        softReleasePlayer();
    }


    @Override
    public void onPause() {
        super.onPause();
        softReleasePlayer();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState (outState);
        super.onSaveInstanceState (outState);
        Log.v (TAG, "Saving the State");
        outState.putInt (STEP_ID,current_position);
        outState.putIntegerArrayList (STEP_KEY,(ArrayList)listSteps);
        if(mExoPlayer != null) {
            currentMediaPosition = mExoPlayer.getCurrentPosition();
            outState.putLong(PLAYER_POSITION, currentMediaPosition);
        }
        outState.putBoolean(PLAYER_STATE, mExoPlayer.getPlayWhenReady());
    }

    /**
     * Initializes the button to the correct views, and sets the text to the composers names,
     * and set's the OnClick listener to the buttons.
     *
     *
     * @return The Array of initialized buttons.
     */
    private Button[] initializeButtons() {
        Button[] buttons = new Button[mButtonIDs.length];
        for (int i = 0; i < buttons.length; i++) {
            Button currentButton = rootView.findViewById(mButtonIDs[i]);
            buttons[i] = currentButton;
            currentButton.setOnClickListener (this);
        }
        return buttons;
    }
    public boolean isLandScape(){
        return (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }
    public boolean isPotrait(){
        return (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
    }

    @Override
    public void onClick(View v) {
        // Get the button that was pressed.
        Button pressedButton = (Button) v;
        Log.v(TAG, "Pressed Button-->"+pressedButton.toString ());
        Log.v(TAG,"Button ID -->"+pressedButton.getId ());
        Log.v(TAG,"Current Postion-->"+current_position);
        if(pressedButton.getId() == mButtonIDs[0]){ // prev
            if(current_position<=0){
                current_position=0;
            }else{
                current_position--;
            }
        }else if(pressedButton.getId () == mButtonIDs[1]){//next
            if(current_position>=listSteps.size ()-1){
                current_position=listSteps.size ()-1;
            }else{
                current_position++;
            }
        }
        if (mExoPlayer != null) {
            mExoPlayer.stop();
        }
        Log.v(TAG,"Updaed Current Postion-->"+current_position);
        populateUI(listSteps.get (current_position));
    }
}
