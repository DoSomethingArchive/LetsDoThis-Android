package org.dosomething.letsdothis.ui.views.typeface.preferences;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import org.dosomething.letsdothis.ui.views.typeface.TypefaceManager;
import org.jetbrains.annotations.NotNull;

/**
 * Created by izzyoji :) on 4/30/15.
 */
public class CustomCheckBoxPreference extends CheckBoxPreference
{
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomCheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomCheckBoxPreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomCheckBoxPreference(Context context)
    {
        super(context);
    }

    public CustomCheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onBindView(@NotNull View view)
    {
        super.onBindView(view);
        TextView checkBox = (TextView) view.findViewById(android.R.id.title);
        checkBox.setTypeface(
                TypefaceManager.obtainTypeface(getContext(), TypefaceManager.BRANDON_REGULAR));
    }
}
