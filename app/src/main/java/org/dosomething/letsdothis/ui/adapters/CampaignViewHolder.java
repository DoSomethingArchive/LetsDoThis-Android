package org.dosomething.letsdothis.ui.adapters;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.dosomething.letsdothis.R;

/**
 * Created by izzyoji :) on 7/10/15.
 */
public class CampaignViewHolder extends RecyclerView.ViewHolder
{
    protected TextView  title;
    protected TextView  callToAction;
    protected ImageView imageView;
    protected ImageView close;

    public CampaignViewHolder(View itemView)
    {
        super(itemView);
        this.imageView = (ImageView) itemView.findViewById(R.id.image);
        this.title = (TextView) itemView.findViewById(R.id.title);
        this.callToAction = (TextView) itemView.findViewById(R.id.call_to_action);
        this.close = (ImageView) itemView.findViewById(R.id.close);
    }
}
