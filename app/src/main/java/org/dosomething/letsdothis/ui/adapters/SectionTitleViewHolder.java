package org.dosomething.letsdothis.ui.adapters;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

/**
* Created by izzyoji :) on 4/30/15.
*/
public class SectionTitleViewHolder extends RecyclerView.ViewHolder
{
    protected final TextView textView;

    public SectionTitleViewHolder(TextView itemView)
    {
        super(itemView);
        textView = itemView;
    }
}
