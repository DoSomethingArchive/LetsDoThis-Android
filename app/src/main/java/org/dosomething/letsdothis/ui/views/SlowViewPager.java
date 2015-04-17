package org.dosomething.letsdothis.ui.views;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by izzyoji :) on 4/15/15.
 */
public class SlowViewPager extends ViewPager
{

    public SlowViewPager(Context context)
    {
        super(context);
        init();
    }

    public SlowViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    /*
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private void init()
    {
        try
        {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            FixedSpeedScroller slowScroller = new FixedSpeedScroller(getContext(),
                                                                     new DecelerateInterpolator());
            scroller.set(this, slowScroller);
        }
        catch(Exception ignored)
        {
        }
    }

    private class FixedSpeedScroller extends Scroller
    {

        private int duration = 700;

        public FixedSpeedScroller(Context context)
        {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator)
        {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel)
        {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration)
        {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, this.duration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy)
        {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, duration);
        }

        public void setScrollDuration(int duration)
        {
            this.duration = duration;
        }
    }

}
