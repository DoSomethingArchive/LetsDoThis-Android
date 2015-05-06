package org.dosomething.letsdothis.ui.views;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import org.dosomething.letsdothis.ui.views.typeface.CustomTextView;

/**
 * Created by toidiu on 5/6/15.
 */
public class ActionGridSpacingDecoration extends RecyclerView.ItemDecoration
{

    public static final int GRID_SPACING = 8;
    private static      int eightDp      = 0;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        Resources r = view.getResources();
        if(view instanceof CustomTextView)
        {
            outRect.bottom = getPxFromDip(r);
        }
        if(view instanceof ImageView)
        {
            outRect.bottom = getPxFromDip(r);
            outRect.right = getPxFromDip(r);
            outRect.left = getPxFromDip(r);

            int childPosition = parent.getChildPosition(view);
            if(childPosition % 2 == 0)
            {
                outRect.right = (getPxFromDip(r) / 2);
            }
            else
            {
                outRect.left = (getPxFromDip(r) / 2);
            }
        }
    }

    private int getPxFromDip(Resources r)
    {
        if(eightDp == 0)
        {
            Float v = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, GRID_SPACING,
                                                r.getDisplayMetrics());
            eightDp = v.intValue();
        }
        return eightDp;
    }

}
