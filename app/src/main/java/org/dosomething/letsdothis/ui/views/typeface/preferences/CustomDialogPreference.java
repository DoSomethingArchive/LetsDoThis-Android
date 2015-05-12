package org.dosomething.letsdothis.ui.views.typeface.preferences;
import android.content.Context;
import android.preference.DialogPreference;
import android.text.TextUtils;
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
    protected void onBindDialogView(View view)
    {
        super.onBindDialogView(view);
        View dialogMessageView = view.findViewById(android.R.id.message);

        if (dialogMessageView != null) {
            final CharSequence message = getDialogMessage();
            if (! TextUtils.isEmpty(message)) {
                if (dialogMessageView instanceof TextView) {
                    ((TextView) dialogMessageView).setTypeface(TypefaceManager.obtainTypeface(getContext(), TypefaceManager.BRANDON_REGULAR));
                }

            }
        }
    }


}
