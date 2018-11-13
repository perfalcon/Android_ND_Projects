package com.perfalcon.balav.bakingapp;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.perfalcon.balav.bakingapp.model.Step;

import java.util.List;

public class StepAdapter extends BaseAdapter
         {
             private static final String TAG = StepAdapter.class.getSimpleName();
             private int mNumberItems;
             private Context mContext;
             private List<Step> mStep;
             public  StepAdapter(Context context, List<Step> stepList){
                 Log.v (TAG,"Size of Steps -->"+stepList.size ());
                 mNumberItems = stepList.size ();
                 mStep =stepList;
                 mContext = context;
             }

             @Override
             public int getCount() {
                 return mNumberItems;
             }

             @Override
             public Object getItem(int position) {
                 return null;
             }

             @Override
             public long getItemId(int position) {
                 return 0;
             }

             @Override
             public View getView(int position, View convertView, ViewGroup parent) {
                 TextView stepText;
                 if(convertView == null){
                     stepText = new TextView (mContext);
                 }else{
                     stepText=(TextView)convertView;
                 }
                 Log.v (TAG, "Step-->"+mStep.get (position));
                 Log.v (TAG, "Step Desc-->"+mStep.get (position).getShortDescription ());
                 stepText.setText (String.valueOf (position+1)+". "+mStep.get(position).getShortDescription ());
                 stepText.setTextSize (25);
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                     stepText.setTextAlignment (View.TEXT_ALIGNMENT_VIEW_START);
                 }
                 return stepText;
             }
}
