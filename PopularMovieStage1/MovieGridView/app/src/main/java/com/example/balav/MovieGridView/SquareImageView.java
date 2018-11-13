package com.example.balav.MovieGridView;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

public class SquareImageView extends AppCompatImageView {
    public SquareImageView(Context context) {
        super (context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super (context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredHeight (), getMeasuredHeight ()); //to get a square image.
    }
}
