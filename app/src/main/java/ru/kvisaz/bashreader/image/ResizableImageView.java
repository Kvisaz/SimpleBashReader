package ru.kvisaz.bashreader.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * ImageView to fit the parent width, and to adapt it's size to keep the aspect ratio.
 * http://stackoverflow.com/questions/5554682/android-imageview-adjusting-parents-height-and-fitting-width
 */
public class ResizableImageView extends ImageView {

    public ResizableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        Drawable d = getDrawable();

        if(d!=null){
            // ceil not round - avoid thin vertical gaps along the left/right edges
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            float inHeight = (float) d.getIntrinsicHeight();
            float inWidth =  (float) d.getIntrinsicWidth();

//           if(inHeight<=inWidth)
               height = (int) Math.ceil((float) width * inHeight / inWidth);



            setMeasuredDimension(width, height);
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}
