package org.dosomething.letsdothis.ui.views.typeface.preferences;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import org.dosomething.letsdothis.ui.views.typeface.TypefaceManager;

/**
 * Created by izzyoji :) on 4/30/15.
 */
public class CustomPreferenceCategory extends PreferenceCategory
{

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public CustomPreferenceCategory(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomPreferenceCategory(Context context)
    {
        super(context);
    }

    @Override
    protected void onBindView(View view)
    {
        super.onBindView(view);
        TextView titleView = (TextView) view.findViewById(android.R.id.title);
        titleView.setTypeface(
                TypefaceManager.obtainTypeface(getContext(), TypefaceManager.BRANDON_BOLD));
    }
}
