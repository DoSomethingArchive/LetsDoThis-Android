package org.dosomething.letsdothis.ui.views.typeface.preferences;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import org.dosomething.letsdothis.ui.views.typeface.CustomTypefaceSpan;
import org.dosomething.letsdothis.ui.views.typeface.TypefaceManager;
import org.jetbrains.annotations.NotNull;

/**
 * Created by izzyoji :) on 4/30/15.
 */
public class CustomPreference extends Preference
{
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomPreference(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public CustomPreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomPreference(Context context)
    {
        super(context);
    }

    @Override
    public void setTitle(CharSequence title)
    {
        super.setTitle(
                CustomTypefaceSpan.format(getContext(), title, TypefaceManager.BRANDON_REGULAR));
    }

    @Override
    public void setTitle(int titleResId)
    {
        setTitle(getContext().getString(titleResId));
    }

    @Override
    protected void onBindView(@NotNull View view)
    {
        super.onBindView(view);
        TextView titleView = (TextView) view.findViewById(android.R.id.title);
        titleView.setTypeface(TypefaceManager.obtainTypeface(getContext(), TypefaceManager.BRANDON_REGULAR));
        TextView summaryView = (TextView) view.findViewById(android.R.id.summary);
        summaryView.setTypeface(TypefaceManager.obtainTypeface(getContext(), TypefaceManager.BRANDON_REGULAR));
    }
}
