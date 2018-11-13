package com.example.balav.moviegridstage2;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;

import com.example.balav.moviegridstage2.model.Movie;
import com.example.balav.moviegridstage2.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private int mNumberItems;
    private List<String> listPosterUrls;
    private List<Integer> listIDs;
    private String optionSelected;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        mContext = context;
        int layoutIdForListItem = R.layout.movie_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ImageViewHolder viewHolder = new ImageViewHolder(view);

        return viewHolder;
    }

    public MovieAdapter(Context c, List<String> listPosterImages, List<Integer> listIDs, String optionSelected) {
        mNumberItems = listPosterImages.size ();
        this.listPosterUrls =listPosterImages;
        this.listIDs=listIDs;
        this.optionSelected=optionSelected;
        Log.v(TAG,"MovieAdapter --> optionSelected-->"+optionSelected);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        Log.d(TAG, "#" + position);
        ((ImageViewHolder)holder).bind(position);
    }



    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class  ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       // TextView listItemNumberView;
        ImageView imageView;
        public ImageViewHolder(View itemView) {
            super (itemView);
            imageView = (ImageView)itemView.findViewById (R.id.iv_movie_image);
            itemView.setOnClickListener(ImageViewHolder.this);
        }
        void bind(int listIndex) {
                   Picasso.with (mContext)
                    .load (moviePoster (listIndex))
                    .into (imageView);
        }

        private Uri moviePoster(int position){
            Log.v ("PosterUrls Size -->",""+listPosterUrls.size ());
            Uri uri=null ;
            Log.v ("Postion-->",""+position);
            Log.v ("moviePoster PosterUrl",""+listPosterUrls.get (position));
            uri = NetworkUtils.buildImageUrl (listPosterUrls.get (position),NetworkUtils.IMAGE_SIZE_342);
            Log.v("moviePoster -->",uri.toString ());
            return uri;
        }

        @Override
        public void onClick(View v) {
            Log.v ("onClick-->","----|"+getAdapterPosition ()+"|");
            launchDetailActivity (listIDs.get (getAdapterPosition ()));
        }
        private void launchDetailActivity(int id) {
            Intent intent = new Intent(mContext, DetailActivity.class);
            Movie movie = new Movie();
            movie.setId(id);
            intent.putExtra(DetailActivity.MOVIE_ID,movie);
            if(optionSelected!=null && !optionSelected.isEmpty ()){
                intent.putExtra (DetailActivity.OPTION_SELECTED,optionSelected);
            }
            mContext.startActivity(intent);
        }
    }
}
