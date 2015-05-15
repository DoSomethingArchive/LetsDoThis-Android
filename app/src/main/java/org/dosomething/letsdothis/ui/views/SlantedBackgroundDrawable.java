package org.dosomething.letsdothis.ui.views;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
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
    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private final Paint   wallpaint;
    private final Paint   shadowPaint;
    private final boolean slantedLeft;
    private final int slantHeight;
    private final int widthOvershoot;
    private final int heightShadowOvershoot;

    //could be this be refactored?
    public SlantedBackgroundDrawable(boolean slatedLeft, int color, int shadowColor, int slantHeight, int widthOvershoot, int heightShadowOvershoot)
    {

        this.slantedLeft = slatedLeft;
        this.heightShadowOvershoot = heightShadowOvershoot;
        this.slantHeight = slantHeight;
        this.widthOvershoot = widthOvershoot;

        wallpaint = new Paint();
        wallpaint.setColor(color);
        wallpaint.setStyle(Paint.Style.FILL);
        wallpaint.setAntiAlias(true);

        shadowPaint = new Paint();
        shadowPaint.setColor(shadowColor);
        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setMaskFilter(
                new BlurMaskFilter(heightShadowOvershoot, BlurMaskFilter.Blur.OUTER));
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
            coord.add(new Point(- widthOvershoot, slantHeight + heightShadowOvershoot));
            coord.add(new Point(width + widthOvershoot, heightShadowOvershoot));
            coord.add(new Point(width + widthOvershoot, height));
            coord.add(new Point(- widthOvershoot, height));
        }
        else
        {
            coord.add(new Point(- widthOvershoot, heightShadowOvershoot));
            coord.add(new Point(width + widthOvershoot, slantHeight + heightShadowOvershoot));
            coord.add(new Point(width + widthOvershoot, height));
            coord.add(new Point(- widthOvershoot, height));
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
