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

    private static float eightDp = 0;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        Resources r = view.getResources();
        if(view instanceof CustomTextView)
        {
            getPxFromDip(r);
            outRect.bottom = (int) (eightDp);
        }
        if(view instanceof ImageView)
        {
            getPxFromDip(r);
            outRect.bottom = (int) (eightDp);
            outRect.right = (int) eightDp;
            outRect.left = (int) eightDp;

            int childPosition = parent.getChildPosition(view);
            if(childPosition % 2 == 0)
            {
                outRect.right = (int) (eightDp / 2);
            }
            else
            {
                outRect.left = (int) (eightDp / 2);
            }
        }
    }

    private void getPxFromDip(Resources r)
    {
        if(eightDp == 0)
        {
            eightDp = TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics());
        }
    }

}
