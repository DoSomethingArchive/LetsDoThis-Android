package org.dosomething.letsdothis.ui.adapters;
import android.view.View;

import org.dosomething.letsdothis.data.Kudo;

/**
* Created by izzyoji :) on 5/15/15.
*/
public abstract class KudoClickListener implements View.OnClickListener
{
    protected Kudo kudo;

    public KudoClickListener(int i)
    {
        kudo = Kudo.values()[i];
    }
}
