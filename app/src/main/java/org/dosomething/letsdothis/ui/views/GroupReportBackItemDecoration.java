package org.dosomething.letsdothis.ui.views;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.adapters.GroupAdapter;

/**
 * Created by izzyoji :) on 5/7/15.
 */
public class GroupReportBackItemDecoration extends RecyclerView.ItemDecoration
{
    private static final int GRID_SPACING = R.dimen.padding_small;
    private static      int eightDp      = 0;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        Resources r = view.getResources();

        if(view instanceof ReportBackImageView)
        {
            outRect.bottom = getPxFromEightDp(r);

            int positionOfActionButtons = ((GroupAdapter)parent.getAdapter()).getStartPositionOfReportBacks();
            int childPosition = parent.getChildPosition(view);
            boolean reportBacksStartOnOdd = positionOfActionButtons % 2 == 0;

            if((reportBacksStartOnOdd && childPosition % 2 == 0) || (!reportBacksStartOnOdd && childPosition % 2 == 1))
            {
                outRect.left = (getPxFromEightDp(r) / 2);
                outRect.right = getPxFromEightDp(r);
            }
            else
            {
                outRect.right = (getPxFromEightDp(r) / 2);
                outRect.left = getPxFromEightDp(r);
            }
        }
    }

    private int getPxFromEightDp(Resources r)
    {
        if(eightDp == 0)
        {
            eightDp = r.getDimensionPixelSize(GRID_SPACING);
        }
        return eightDp;
    }
}
