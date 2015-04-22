package org.dosomething.letsdothis.ui.views;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toidiu on 4/22/15.
 */
public class SlantedBackgroundDrawable extends ColorDrawable
{

    public static final int WIDTH_OVERSHOOT   = 50;
    public static final int HEIGHT_UNDERSHOOT = 30;
    private final Paint wallpaint;
    private final Paint shadowPaint;

    public SlantedBackgroundDrawable()
    {
        wallpaint = new Paint();
        wallpaint.setColor(Color.WHITE);
        wallpaint.setStyle(Paint.Style.FILL);
        wallpaint.setAntiAlias(true);

        shadowPaint = new Paint();
        shadowPaint.setColor(0x22000000);
        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setMaskFilter(new BlurMaskFilter(HEIGHT_UNDERSHOOT, BlurMaskFilter.Blur.OUTER));

    }

    @Override
    public void draw(Canvas canvas)
    {
        int height = getBounds().height();
        int width = getBounds().width();


        List<PathCoordinates> coord = new ArrayList<>();
        coord.add(new PathCoordinates(- WIDTH_OVERSHOOT, 100 + HEIGHT_UNDERSHOOT));
        coord.add(new PathCoordinates(width + WIDTH_OVERSHOOT, 0 + HEIGHT_UNDERSHOOT));
        coord.add(new PathCoordinates(width + WIDTH_OVERSHOOT, height));
        coord.add(new PathCoordinates(- WIDTH_OVERSHOOT, height));

        Path wallpath = new Path();
        wallpath.reset(); // only needed when reusing this path for a new build
        for(int i = 0; i < coord.size(); i++)
        {
            PathCoordinates c = coord.get(i);
            if(i == 0)
            {
                wallpath.moveTo(c.x, c.y); // used for first point
            }
            else
            {
                wallpath.lineTo(c.x, c.y);
            }
        }

        canvas.drawPath(wallpath, shadowPaint);
        canvas.drawPath(wallpath, wallpaint);

    }

    private static class PathCoordinates
    {
        Integer x;
        Integer y;

        private PathCoordinates(Integer x, Integer y)
        {
            this.x = x;
            this.y = y;
        }
    }

}
