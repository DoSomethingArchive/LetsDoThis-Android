package org.dosomething.letsdothis.ui.adapters;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by izzyoji :) on 4/17/15.
 */
public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.CampaignViewHolder>
{

    public static final int               VIEW_TYPE_CAMPAIGN          = 0;
    public static final int               VIEW_TYPE_CAMPAIGN_EXPANDED = 1;
    private             ArrayList<Object> campaigns                   = new ArrayList<>();
    private CampaignClickListener campaignClickListener;
    private int                   selectedPosition = -1;

    public interface CampaignClickListener
    {
        void onCampaignClicked(int campaignId);

        void onCampaignExpanded(int position);
    }

    public CampaignAdapter(List<Campaign> campaigns, CampaignClickListener campaignClickListener)
    {
        super();
        this.campaigns.addAll(campaigns);
        this.campaignClickListener = campaignClickListener;
    }

    @Override
    public CampaignViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch(viewType)
        {
            case VIEW_TYPE_CAMPAIGN_EXPANDED:
                View bigLayout = LayoutInflater.from(parent.getContext())
                                               .inflate(R.layout.item_campaign_large, parent,
                                                        false);
                return new ExpandedCampaignViewHolder(bigLayout);
            default:
                View smallLayout = LayoutInflater.from(parent.getContext())
                                                 .inflate(R.layout.item_campaign, parent, false);
                return new CampaignViewHolder(smallLayout);

        }
    }

    @Override
    public void onBindViewHolder(final CampaignViewHolder holder, final int position)
    {
        final Campaign campaign = (Campaign) campaigns.get(position);
        if(getItemViewType(position) == VIEW_TYPE_CAMPAIGN)
        {
            holder.title.setText(campaign.title);
            holder.callToAction.setText(campaign.callToAction);
            Picasso.with(holder.imageView.getContext()).load(campaign.imagePath).into(holder.imageView);
            holder.root.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    selectedPosition = position;
                    campaignClickListener.onCampaignExpanded(position);
                    notifyItemChanged(position);
                }
            });
        }
        else if(getItemViewType(position) == VIEW_TYPE_CAMPAIGN_EXPANDED)
        {
            ExpandedCampaignViewHolder expandedCampaignViewHolder = (ExpandedCampaignViewHolder) holder;
            expandedCampaignViewHolder.title.setText(campaign.title);
            expandedCampaignViewHolder.callToAction.setText(campaign.callToAction);
            Picasso.with(expandedCampaignViewHolder.imageView.getContext()).load(campaign.imagePath).into(expandedCampaignViewHolder.imageView);
            expandedCampaignViewHolder.imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    selectedPosition = -1;
                    notifyItemChanged(position);
                }
            });
            expandedCampaignViewHolder.campaignDetails.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    campaignClickListener.onCampaignClicked(campaign.id);
                }
            });

            expandedCampaignViewHolder.problemFact.setText(campaign.problemFact);
        }


    }

    @Override
    public int getItemViewType(int position)
    {
        return position == selectedPosition ? VIEW_TYPE_CAMPAIGN_EXPANDED : VIEW_TYPE_CAMPAIGN;
    }

    @Override
    public int getItemCount()
    {
        return campaigns.size();
    }

    public static class CampaignViewHolder extends RecyclerView.ViewHolder
    {
        protected View      root;
        protected ImageView imageView;
        protected TextView  title;
        protected TextView  callToAction;

        public CampaignViewHolder(View itemView)
        {
            super(itemView);
            this.root = itemView;
            this.imageView = (ImageView) itemView.findViewById(R.id.image);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.callToAction = (TextView) itemView.findViewById(R.id.call_to_action);
        }
    }

    public static class ExpandedCampaignViewHolder extends CampaignViewHolder
    {
        private TextView problemFact;
        private View     campaignDetails;

        public ExpandedCampaignViewHolder(View itemView)
        {
            super(itemView);
            problemFact = (TextView) itemView.findViewById(R.id.problemFact);
            campaignDetails = itemView.findViewById(R.id.campaign_details);
        }
    }
}
