package com.perfalcon.balav.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.perfalcon.balav.bakingapp.model.Step;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.List;

public class StepDetail extends AppCompatActivity {
    public static final String TAG = StepDetail.class.getSimpleName ();
    public static final String STEP_KEY="step";
    public static final String STEP_ID="step_id";
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private int[] mButtonIDs = {R.id.btn_prev_step, R.id.btn_next_step};
    private Button[] mButtons;
    private  int current_position;
    private  List <Step> listSteps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_android_me);
        this.getSupportActionBar ().setDisplayHomeAsUpEnabled (true);


        if(savedInstanceState==null){
            Intent intent = getIntent ();
            if (intent == null) { closeOnError ();  }
            int step_id = intent.getIntExtra (STEP_ID,0);
            Log.v (TAG, "Step CLICKED-->" + step_id);
            listSteps = intent.getParcelableArrayListExtra (STEP_KEY);

            StepFragment stepFragment = new StepFragment ();
            stepFragment.setSteps (listSteps);
            stepFragment.setCurrentPosition (step_id);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_container, stepFragment)
                    .commit();
        }
    }
    public  void HideNavigationBar(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        super.onResume ();
         HideNavigationBar();
    }
    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}

