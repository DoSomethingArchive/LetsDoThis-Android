package org.dosomething.letsdothis.ui.views.typeface.preferences;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.PreferenceGroup;
import android.util.AttributeSet;

/**
 * Created by izzyoji :) on 4/30/15.
 */
public class CustomPreferenceGroup extends PreferenceGroup
{
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomPreferenceGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomPreferenceGroup(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public CustomPreferenceGroup(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
}
