package com.example.balav.moviegridstage2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.balav.moviegridstage2.utils.NetworkUtils;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private static final String TAG = ReviewAdapter.class.getSimpleName();
    private int mNumberItems;
    private List<String> listReviews;

    public ReviewAdapter(List<String> listReviews){
        mNumberItems = listReviews.size ();
        this.listReviews=listReviews;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        mContext = context;
        int layoutIdForListItem = R.layout.reviews_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
       ReviewViewHolder viewHolder = new ReviewViewHolder (view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        ((ReviewViewHolder)holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder  {

        TextView reviewViewText, reviewViewNumber;

        public ReviewViewHolder(View itemView) {
            super (itemView);
            reviewViewNumber = (TextView)itemView.findViewById (R.id.tv_review_number);
            reviewViewText =  (TextView)itemView.findViewById (R.id.tv_review);

        }
        void bind(int listIndex) {
            reviewViewNumber.setText(String.valueOf (listIndex+1));
            Log.v (TAG, "reviews-->"+listReviews.get (listIndex));
            reviewViewText.setText (listReviews.get (listIndex));

        }


    }
}
