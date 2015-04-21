package org.dosomething.letsdothis.ui.adapters;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
        View v = LayoutInflater.from(parent.getContext())
                              .inflate(R.layout.item_campaign, parent, false);
        return new CampaignViewHolder(v);
    }

    //todo: instead of having repetitive layout code, use multiple view types
    @Override
    public void onBindViewHolder(final CampaignViewHolder holder, final int position)
    {
        holder.smallTextView.setText(Integer.toString(campaigns.get(position)));
        holder.largeTextView.setText(Integer.toString(campaigns.get(position)));
        holder.smallTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                holder.smallView.setVisibility(View.GONE);
                holder.largeView.setVisibility(View.VISIBLE);
                notifyItemChanged(position);
            }
        });
        holder.largeTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                holder.largeView.setVisibility(View.GONE);
                holder.smallView.setVisibility(View.VISIBLE);
                notifyItemChanged(position);
            }
        });
        holder.viewDetails.setOnClickListener(new View.OnClickListener()
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
        private View smallView;
        private TextView smallTextView;
        private View largeView;
        private TextView largeTextView;
        private View viewDetails;

        public CampaignViewHolder(View itemView)
        {
            super(itemView);
            this.smallView = itemView.findViewById(R.id.smallLayout);
            this.smallTextView = (TextView) itemView.findViewById(R.id.title_small);
            this.largeView = itemView.findViewById(R.id.largeLayout);
            this.largeTextView = (TextView) itemView.findViewById(R.id.title_large);
            this.viewDetails = itemView.findViewById(R.id.view_details);
        }
    }
}
