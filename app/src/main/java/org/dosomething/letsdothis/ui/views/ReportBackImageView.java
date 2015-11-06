package org.dosomething.letsdothis.ui.views;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by izzyoji :) on 5/7/15.
 */
public class ReportBackImageView extends ImageView
{
    public ReportBackImageView(Context context)
    {
        super(context);
    }

    public ReportBackImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ReportBackImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Set height to be same as the measured width.
        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }
}
