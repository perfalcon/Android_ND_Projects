package com.perfalcon.balav.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.perfalcon.balav.bakingapp.model.Baking;
import com.perfalcon.balav.bakingapp.model.Step;

import java.util.List;

public class RecipeStepsActivity extends AppCompatActivity  {
    private static final String TAG = RecipeStepsActivity.class.getSimpleName();
    private Baking mBaking;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_recipe_steps);

        Intent intent = getIntent ();
        if (intent == null) {
            closeOnError ();
        }

        mBaking = intent.getParcelableExtra (DetailActivity.BAKING_KEY);
        Log.v(TAG,"baking--->"+mBaking);
        Log.v (TAG,"Recipe Name -->"+mBaking.getName ());
        int baking_id = intent.getIntExtra (DetailActivity.BAKING_ID,0);
        Log.v (TAG, "RECIPE CLICKED-->" + baking_id);

        RecyclerView rvRecipeSteps = (RecyclerView)findViewById (R.id.steps_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        rvRecipeSteps.setLayoutManager (layoutManager);
        rvRecipeSteps.setHasFixedSize(true);
        SimpleItemRecyclerViewAdapter recipeStepsAdapter = new SimpleItemRecyclerViewAdapter (this, mBaking.getSteps ());
        rvRecipeSteps.setAdapter (recipeStepsAdapter);
    }
    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }


    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private static final String TAG = StepAdapter.class.getSimpleName();
        private int mNumberItems;
        private Context mContext;
        private List<Step> mStep;

        public SimpleItemRecyclerViewAdapter(RecipeStepsActivity recipeStepsActivity, List<Step> stepList) {
            mStep = stepList;
            mNumberItems=stepList.size ();
        }

        @NonNull
        @Override
        public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from (parent.getContext ())
                    .inflate (R.layout.steps_card, parent, false);
            return new ViewHolder (view);

        }

        @Override
        public void onBindViewHolder(@NonNull SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            Log.v(TAG, "#" + position);
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return mNumberItems;
        }
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView stepText;

            ViewHolder(View view) {
                super (view);
                stepText = (TextView)itemView.findViewById (R.id.tv_step);
            }

            public void bind(int position) {
                Log.v (TAG, "Step-->"+mStep.get (position));
                Log.v (TAG, "Step Desc-->"+mStep.get (position).getShortDescription ());
                stepText.setText (mStep.get (position).getShortDescription ());

            }
        }
    }
}
