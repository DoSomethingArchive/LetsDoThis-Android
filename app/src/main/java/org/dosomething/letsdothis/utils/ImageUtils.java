package org.dosomething.letsdothis.utils;
import android.content.Context;

import java.io.File;

/**
 * Created by izzyoji :) on 5/5/15.
 */
public class ImageUtils
{
    public static File getAvatarFile(Context context)
    {
        return new File(context.getFilesDir(), "avatar.jpg");
    }

}
