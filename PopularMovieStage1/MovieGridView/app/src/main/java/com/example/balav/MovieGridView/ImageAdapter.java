package com.example.balav.MovieGridView;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.balav.MovieGridView.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> listPosterUrls;
    private final LayoutInflater mInflater;
    public ImageAdapter(Context c, List<String> listPosterImages) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
       this.listPosterUrls =listPosterImages;
    }

    public int getCount() {
        return listPosterUrls.size ();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    // create a new ImageView for each item referenced by the Adapter
    public View  getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
//           imageView = new ImageView(mContext);
//           imageView.setLayoutParams(new ViewGroup.LayoutParams(300, 300));
//           imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            convertView = mInflater.inflate(R.layout.activity_grid_item, parent, false);
            imageView = convertView.findViewById (R.id.picture);
           // imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            //convertView.setTag(R.id.picture, convertView.findViewById(R.id.picture));

          }else {
            imageView = (ImageView) convertView;
            //imageView = (ImageView) convertView.findViewById (R.id.picture);
        }
        //imageView.setImageResource(mThumbIds[position]);
        if(position<listPosterUrls.size ()) {
            Picasso.with (mContext)
                    .load (moviePoster (position))
                    .into (imageView);
        }
        return imageView;
    }

    private Uri moviePoster(int position){
        Log.v ("PosterUrls Size -->",""+listPosterUrls.size ());
        Uri uri=null ;
        Log.v ("Postion-->",""+position);
        Log.v ("moviePoster PosterUrl",""+listPosterUrls.get (position));
        uri =NetworkUtils.buildImageUrl (listPosterUrls.get (position),NetworkUtils.IMAGE_SIZE_342);
        Log.v("moviePoster -->",uri.toString ());
      return uri;
    }

}