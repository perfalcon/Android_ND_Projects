package com.perfalcon.balav.bakingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.perfalcon.balav.bakingapp.model.Baking;
import com.perfalcon.balav.bakingapp.model.Step;

import java.util.List;

public class RecipeFragment extends Fragment {
    private static final String TAG = RecipeFragment.class.getSimpleName();
    private List<Step> mSteps;
    private Baking mBaking;
    public RecipeFragment(){

    }
@SuppressLint("ValidFragment")
public RecipeFragment(Baking baking){
        mBaking = baking;
}
OnStepClickListener mCallback;
    public interface OnStepClickListener{
    void onStepSelected(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach (context);
        try {
            mCallback = (OnStepClickListener) context;
        }catch (ClassCastException e){
            throw  new ClassCastException (context.toString ()
                    +"must  implement OnStepClickListener");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState != null) {
            Log.v (TAG, "Restoring State");
            if (savedInstanceState.containsKey (DetailActivity.BAKING_KEY)) {
                mBaking = savedInstanceState.getParcelable (DetailActivity.BAKING_KEY);
            }
        }

        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        ListView listView = rootView.findViewById (R.id.lv_steps);

        StepAdapter mStepAdapter = new StepAdapter (getContext (),mBaking.getSteps ());
        listView.setAdapter (mStepAdapter);

        // Set a click listener on the gridView and trigger the callback onImageSelected when an item is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Trigger the callback method and pass in the position that was clicked
                Log.v(TAG,"from [onItemClick] --Position-->"+position);
                mCallback.onStepSelected (position);
            }
        });
        return rootView;
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState (outState);
        Log.v (TAG, "Saving the State");
        outState.putParcelable (DetailActivity.BAKING_KEY,mBaking);
    }
}
