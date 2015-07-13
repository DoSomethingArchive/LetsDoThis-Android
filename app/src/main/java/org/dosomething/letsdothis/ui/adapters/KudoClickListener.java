package org.dosomething.letsdothis.ui.adapters;
import android.view.View;

import org.dosomething.letsdothis.data.Kudos;

/**
* Created by izzyoji :) on 5/15/15.
*/
public abstract class KudoClickListener implements View.OnClickListener
{
    protected Kudos kudos;

    public KudoClickListener(int i)
    {
        kudos = Kudos.values()[i];
    }
}
