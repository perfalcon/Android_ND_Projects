package com.perfalcon.balav.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.perfalcon.balav.bakingapp.model.Baking;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter {
    private static final String TAG = RecipeAdapter.class.getSimpleName();
    private int mNumberItems;
    private Context mContext;
    private List<Baking> mBaking;

    public RecipeAdapter(List<Baking> bakingList){
        Log.v (TAG,"Size of BakingList -->"+bakingList.size ());
        mNumberItems = bakingList.size ();
        mBaking = bakingList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        mContext = context;
        int layoutIdForListItem = R.layout.recipe_card;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        RecipeViewHolder viewHolder = new RecipeViewHolder (view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.v(TAG, "#" + position);
        ((RecipeViewHolder)holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView reviewViewText;
        ImageView imageView;


        public RecipeViewHolder(View itemView) {
            super (itemView);
            reviewViewText = itemView.findViewById (R.id.text_recipe);
            itemView.setOnClickListener (this);
            imageView = itemView.findViewById(R.id.recipeImage);
        }
        void bind(int listIndex) {
            Log.v (TAG, "Baking-->"+mBaking.get (listIndex));
            Log.v (TAG, "Recipe Name-->"+mBaking.get (listIndex).getName ());
            reviewViewText.setText (mBaking.get (listIndex).getName ());
            Picasso.with(mContext).load(R.drawable.baking1).into(imageView);
        }


        @Override
        public void onClick(View v) {
            Log.v ("onClick-->","----|"+getAdapterPosition ()+"|");
            launchDetailActivity (getAdapterPosition ());
        }
        private void launchDetailActivity(int id) {
            Intent intent = new Intent(mContext, DetailActivity.class);
            //Intent intent = new Intent(mContext, RecipeStepsActivity.class);

            intent.putExtra (DetailActivity.BAKING_ID,id);

            intent.putExtra(DetailActivity.BAKING_KEY,mBaking.get(id));
            Log.v (TAG,"Recipe sending Object.."+mBaking.get (id));
            Log.v (TAG,"Recipe sending ..."+mBaking.get (id).getName ());
            mContext.startActivity(intent);
        }
    }
}
