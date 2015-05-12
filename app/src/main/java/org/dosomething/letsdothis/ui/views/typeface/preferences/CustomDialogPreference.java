package org.dosomething.letsdothis.ui.views.typeface.preferences;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import org.dosomething.letsdothis.ui.views.typeface.TypefaceManager;

/**
 * Created by izzyoji :) on 5/12/15.
 */
public class CustomDialogPreference extends DialogPreference
{
    public CustomDialogPreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomDialogPreference(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onBindView(View view)
    {
        super.onBindView(view);
        final TextView titleView = (TextView) view.findViewById(android.R.id.title);
        if(titleView != null)
        {
            titleView.setTypeface(
                    TypefaceManager.obtainTypeface(getContext(), TypefaceManager.BRANDON_REGULAR));
        }

        final TextView summaryView = (TextView) view.findViewById(android.R.id.summary);
        if(summaryView != null)
        {
            summaryView.setTypeface(
                    TypefaceManager.obtainTypeface(getContext(), TypefaceManager.BRANDON_REGULAR));
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult)
    {
        super.onDialogClosed(positiveResult);

        if(positiveResult)
        {
            SharedPreferences.Editor editor = getEditor();
            editor.putBoolean(getKey(), true);
            editor.commit();
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue)
    {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
    }
}
