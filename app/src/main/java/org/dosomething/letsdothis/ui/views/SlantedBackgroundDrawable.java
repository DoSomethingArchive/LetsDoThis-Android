package org.dosomething.letsdothis.ui.views;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toidiu on 4/22/15.
 */
public class SlantedBackgroundDrawable extends ColorDrawable
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final int WIDTH_OVERSHOOT      = 50;
    public static final int HEIGHT_SHADOW_HEIGHT = 30;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private final Paint   wallpaint;
    private final Paint   shadowPaint;
    private final boolean slantedLeft;

    public SlantedBackgroundDrawable(boolean slatedLeft)
    {
        slantedLeft = slatedLeft;

        wallpaint = new Paint();
        wallpaint.setColor(Color.WHITE);
        wallpaint.setStyle(Paint.Style.FILL);
        wallpaint.setAntiAlias(true);

        shadowPaint = new Paint();
        shadowPaint.setColor(0x22000000);
        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setMaskFilter(new BlurMaskFilter(HEIGHT_SHADOW_HEIGHT, BlurMaskFilter.Blur.OUTER));

    }

    public SlantedBackgroundDrawable(boolean slatedLeft, int color)
    {
        slantedLeft = slatedLeft;

        wallpaint = new Paint();
        wallpaint.setColor(color);
        wallpaint.setStyle(Paint.Style.FILL);
        wallpaint.setAntiAlias(true);

        shadowPaint = new Paint();
        shadowPaint.setColor(0x22000000);
        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setMaskFilter(new BlurMaskFilter(HEIGHT_SHADOW_HEIGHT, BlurMaskFilter.Blur.OUTER));

    }

    @Override
    public void draw(Canvas canvas)
    {
        int height = getBounds().height();
        int width = getBounds().width();

        Path slantedPath = getSlantedPath(slantedLeft, height, width);

        canvas.drawPath(slantedPath, shadowPaint);
        canvas.drawPath(slantedPath, wallpaint);

    }

    private Path getSlantedPath(boolean slantedLeft, int height, int width)
    {
        List<Point> coord = new ArrayList<>();
        if(slantedLeft)
        {
            coord.add(new Point(- WIDTH_OVERSHOOT, 100 + HEIGHT_SHADOW_HEIGHT));
            coord.add(new Point(width + WIDTH_OVERSHOOT, 0 + HEIGHT_SHADOW_HEIGHT));
            coord.add(new Point(width + WIDTH_OVERSHOOT, height));
            coord.add(new Point(- WIDTH_OVERSHOOT, height));
        }
        else
        {
            coord.add(new Point(- WIDTH_OVERSHOOT, 0 + HEIGHT_SHADOW_HEIGHT));
            coord.add(new Point(width+ WIDTH_OVERSHOOT, 100 + HEIGHT_SHADOW_HEIGHT));
            coord.add(new Point(width + WIDTH_OVERSHOOT, height));
            coord.add(new Point(- WIDTH_OVERSHOOT, height));
        }

        Path slantedPath = new Path();
        slantedPath.reset(); // only needed when reusing this path for a new build
        for(int i = 0; i < coord.size(); i++)
        {
            Point c = coord.get(i);
            if(i == 0)
            {
                slantedPath.moveTo(c.x, c.y); // used for first point
            }
            else
            {
                slantedPath.lineTo(c.x, c.y);
            }
        }
        return slantedPath;
    }


}
