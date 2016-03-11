package org.dosomething.letsdothis.utils;
import android.content.Context;
import android.content.res.Resources;
import android.util.Patterns;
import android.util.TypedValue;

import org.dosomething.letsdothis.R;

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

    /**
     * Helper function to format the displayed username.
     *
     * @param context Context
     * @param firstName User first name
     * @param lastInitial User last initial, if any
     * @return String
     */
    public static String formatUserDisplayName(Context context, String firstName, String lastInitial) {
        // Sanitizing the first name because of rare cases where user's email was in that field
        String first = firstName != null ? firstName : "";
        if (Patterns.EMAIL_ADDRESS.matcher(first).matches()) {
            first = context.getResources().getString(R.string.user_default_fname);
        }

        String last = "";
        if (lastInitial != null && ! lastInitial.isEmpty()) {
            last = lastInitial + ".";
        }

        return String.format("%s %s", first, last).trim();
    }
}
