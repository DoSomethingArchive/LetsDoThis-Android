package org.dosomething.letsdothis.ui.adapters;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dosomething.letsdothis.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by izzyoji :) on 4/17/15.
 */
public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.CampaignViewHolder>
{

    private List<Integer> campaigns = new ArrayList<>();
    private CampaignClickListener campaignClickListener;

    public interface CampaignClickListener
    {
        void onCampaignClicked(Integer integer);
    }

    public CampaignAdapter(List<Integer> campaigns, CampaignClickListener campaignClickListener)
    {
        super();
        this.campaigns = campaigns;
        this.campaignClickListener = campaignClickListener;
    }

    @Override
    public CampaignViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        TextView textView = new TextView(parent.getContext());
        textView.setBackgroundResource(R.drawable.bg_selectable_black);
        return new CampaignViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(CampaignViewHolder holder, final int position)
    {
        holder.textView.setText(Integer.toString(campaigns.get(position)));
        holder.textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                campaignClickListener.onCampaignClicked(campaigns.get(position));
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return campaigns.size();
    }

    public static class CampaignViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textView;

        public CampaignViewHolder(TextView itemView)
        {
            super(itemView);
            this.textView = itemView;
        }
    }
}
