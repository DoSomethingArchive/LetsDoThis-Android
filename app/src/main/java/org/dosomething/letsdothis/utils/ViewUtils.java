package org.dosomething.letsdothis.utils;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import java.io.File;

/**
 * Created by izzyoji :) on 5/5/15.
 */
public class ViewUtils
{
    public static File getAvatarFile(Context context)
    {
        return new File(context.getExternalFilesDir("images"), "avatar.jpg");
    }

    public static Float getPxFromDip(Resources r, int dp)
    {
        return TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }
}
