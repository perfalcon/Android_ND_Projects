package com.example.balav.moviegridstage2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.balav.moviegridstage2.utils.NetworkUtils;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private static final String TAG = TrailerAdapter.class.getSimpleName();
    private int mNumberItems;
    private List<String> listTrailers;

    public  TrailerAdapter(List<String> listTrailers){
        mNumberItems = listTrailers.size ();
        this.listTrailers=listTrailers;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        mContext = context;
        int layoutIdForListItem = R.layout.trailers_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
       TrailerAdapter.ImageViewHolder viewHolder = new TrailerAdapter.ImageViewHolder (view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        ((TrailerAdapter.ImageViewHolder)holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }


    class  ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView trailerView;
        ImageButton shareButton;

        public ImageViewHolder(View itemView) {
            super (itemView);
            trailerView = (TextView)itemView.findViewById (R.id.tv_trailer);
            itemView.setOnClickListener(TrailerAdapter.ImageViewHolder.this);

            shareButton= (ImageButton)itemView.findViewById (R.id.shareButton);


        }
        void bind(int listIndex) {
            trailerView.setText("Trailer "+String.valueOf (listIndex+1));
            shareButton.setTag (getYouTubeUri (getAdapterPosition ()).toString ());
        }

        private Uri getYouTubeUri(int position){
            Log.v ("No of Trailers -->",""+listTrailers.size ());
            Uri uri=null ;
            Log.v ("Position-->",""+position);
            Log.v ("YouTube",""+listTrailers.get (position));
            uri = NetworkUtils.buildYouTubeUrl(listTrailers.get (position));
            Log.v("YouTube -->",uri.toString ());
            return uri;
        }

        @Override
        public void onClick(View v) {
            Log.v ("onClick-->","----|"+getAdapterPosition ()+"|"+""+getYouTubeUri (getAdapterPosition ()));
            launchTrailer (getYouTubeUri (getAdapterPosition ()));
        }
        private void launchTrailer(Uri uri) {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if(isIntentAvailable (mContext,intent)){
               mContext.startActivity(intent);
            }
        }

        public  boolean isIntentAvailable(Context ctx, Intent intent) {
            final PackageManager mgr = ctx.getPackageManager();
            List<ResolveInfo> list =
                    mgr.queryIntentActivities(intent,
                            PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        }
    }
}
