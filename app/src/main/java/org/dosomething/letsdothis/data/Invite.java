package org.dosomething.letsdothis.data;
import android.content.Intent;
import android.content.res.Resources;

import org.dosomething.letsdothis.R;

/**
 * Created by izzyoji :) on 5/1/15.
 */
public class Invite
{
    public String title;
    public String details;
    public String code;

    public static Intent buildShareIntent(Resources resources, String title, String code)
    {
        String shareBody = resources.getString(R.string.share_invite, title, code);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                               resources.getString(R.string.app_name));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        return Intent.createChooser(sharingIntent, resources.getString(R.string.invite_using));
    }
}
