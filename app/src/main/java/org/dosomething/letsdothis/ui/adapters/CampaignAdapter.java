package org.dosomething.letsdothis.ui.adapters;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by izzyoji :) on 4/17/15.
 */
public class CampaignAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final int VIEW_TYPE_CAMPAIGN          = 0;
    public static final int VIEW_TYPE_CAMPAIGN_EXPANDED = 1;
    public static final int VIEW_TYPE_CAMPAIGN_SMALL    = 2;
    public static final int VIEW_TYPE_CAMPAIGN_FOOTER   = 3;
    public static final int VIEW_TYPE_REPORT_BACK       = 4;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private ArrayList<Object> dataSet = new ArrayList<>();
    private CampaignAdapterClickListener campaignAdapterClickListener;
    private int selectedPosition = - 1;

    public interface CampaignAdapterClickListener
    {
        void onCampaignClicked(int campaignId);

        void onCampaignExpanded(int position);

        void onReportBackClicked(int reportBackId);

        void onScrolledToBottom();
    }

    public CampaignAdapter(List<Campaign> campaigns, CampaignAdapterClickListener campaignAdapterClickListener)
    {
        super();
        this.dataSet.addAll(campaigns);
        this.campaignAdapterClickListener = campaignAdapterClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch(viewType)
        {
            case VIEW_TYPE_CAMPAIGN_FOOTER:
                View footerLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_campaign_footer, parent, false);
                return new SectionTitleViewHolder((TextView) footerLayout);
            case VIEW_TYPE_REPORT_BACK:
                View reportBackLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_report_back_square, parent, false);
                return new ReportBackViewHolder((ImageView) reportBackLayout);
            case VIEW_TYPE_CAMPAIGN_EXPANDED:
                View bigLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_campaign_large, parent, false);
                return new ExpandedCampaignViewHolder(bigLayout);
            case VIEW_TYPE_CAMPAIGN_SMALL:
                View smallLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_campaign_small, parent, false);
                return new CampaignViewHolder(smallLayout);
            default:
                View normalLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_campaign, parent, false);
                return new CampaignViewHolder(normalLayout);

        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        if(dataSet.size() >= 24 && position == dataSet.size() - 3)
        {
            campaignAdapterClickListener.onScrolledToBottom();
        }

        if(getItemViewType(position) == VIEW_TYPE_CAMPAIGN)
        {
            final Campaign campaign = (Campaign) dataSet.get(position);
            CampaignViewHolder campaignViewHolder = (CampaignViewHolder) holder;
            campaignViewHolder.title.setText(campaign.title);
            campaignViewHolder.callToAction.setText(campaign.callToAction);
            Picasso.with(campaignViewHolder.imageView.getContext()).load(campaign.imagePath)
                    .into(campaignViewHolder.imageView);
            campaignViewHolder.root.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    selectedPosition = position;
                    campaignAdapterClickListener.onCampaignExpanded(position);
                    notifyItemChanged(position);
                }
            });
        }
        if(getItemViewType(position) == VIEW_TYPE_CAMPAIGN_SMALL)
        {
            final Campaign campaign = (Campaign) dataSet.get(position);
            CampaignViewHolder campaignViewHolder = (CampaignViewHolder) holder;
            campaignViewHolder.title.setText(campaign.title);
            campaignViewHolder.callToAction.setText(campaign.callToAction);
            Picasso.with(campaignViewHolder.imageView.getContext()).load(campaign.imagePath)
                    .into(campaignViewHolder.imageView);
            campaignViewHolder.root.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    selectedPosition = position;
                    campaignAdapterClickListener.onCampaignExpanded(position);
                    notifyItemChanged(position);
                }
            });
        }
        else if(getItemViewType(position) == VIEW_TYPE_CAMPAIGN_EXPANDED)
        {
            final Campaign campaign = (Campaign) dataSet.get(position);
            ExpandedCampaignViewHolder expandedCampaignViewHolder = (ExpandedCampaignViewHolder) holder;
            expandedCampaignViewHolder.title.setText(campaign.title);
            expandedCampaignViewHolder.callToAction.setText(campaign.callToAction);
            Picasso.with(expandedCampaignViewHolder.imageView.getContext()).load(campaign.imagePath)
                    .into(expandedCampaignViewHolder.imageView);
            expandedCampaignViewHolder.imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    selectedPosition = - 1;
                    notifyItemChanged(position);
                }
            });
            expandedCampaignViewHolder.campaignDetails.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    campaignAdapterClickListener.onCampaignClicked(campaign.id);
                }
            });

            expandedCampaignViewHolder.problemFact.setText(campaign.problemFact);

            List<String> timeUntilExpiration = TimeUtils.getTimeUntilExpiration(campaign.endTime);
            String days = timeUntilExpiration.get(0);
            expandedCampaignViewHolder.days.setText(days);
            String hours = timeUntilExpiration.get(1);
            expandedCampaignViewHolder.hours.setText(hours);
            String minutes = timeUntilExpiration.get(2);
            expandedCampaignViewHolder.minutes.setText(minutes);
            Resources resources = expandedCampaignViewHolder.itemView.getContext().getResources();
            expandedCampaignViewHolder.daysLabel
                    .setText(resources.getQuantityString(R.plurals.days, Integer.parseInt(days)));
            expandedCampaignViewHolder.hoursLabel
                    .setText(resources.getQuantityString(R.plurals.hours, Integer.parseInt(hours)));
            expandedCampaignViewHolder.minutesLabel.setText(
                    resources.getQuantityString(R.plurals.minutes, Integer.parseInt(minutes)));
        }
        else if(getItemViewType(position) == VIEW_TYPE_REPORT_BACK)
        {
            final ReportBack reportBack = (ReportBack) dataSet.get(position);
            ReportBackViewHolder reportBackViewHolder = (ReportBackViewHolder) holder;

            Picasso.with(reportBackViewHolder.root.getContext()).load(reportBack.getImagePath())
                    .into(reportBackViewHolder.root);

            reportBackViewHolder.root.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    campaignAdapterClickListener.onReportBackClicked(reportBack.id);
                }
            });
        }
        else if(getItemViewType(position) == VIEW_TYPE_CAMPAIGN_FOOTER)
        {
            SectionTitleViewHolder sectionTitleViewHolder = (SectionTitleViewHolder) holder;
            sectionTitleViewHolder.textView.setText(sectionTitleViewHolder.textView.getContext()
                                                            .getString(
                                                                    R.string.people_doing_stuff));
        }


    }

    @Override
    public int getItemViewType(int position)
    {
        Object currentObject = dataSet.get(position);
        if(currentObject instanceof Campaign)
        {
            if(position == selectedPosition)
            {
                return VIEW_TYPE_CAMPAIGN_EXPANDED;
            }
            else if(selectedPosition != - 1)
            {
                return VIEW_TYPE_CAMPAIGN_SMALL;
            }
            else
            {
                return VIEW_TYPE_CAMPAIGN;
            }
        }
        else if(currentObject instanceof String)
        {
            return VIEW_TYPE_CAMPAIGN_FOOTER;
        }
        else
        {
            return VIEW_TYPE_REPORT_BACK;
        }
    }

    public void addItem(Object o)
    {
        dataSet.add(o);
        notifyItemInserted(dataSet.size() - 1);
    }

    public void addAll(List<ReportBack> objects)
    {
        dataSet.addAll(objects);
        notifyItemRangeInserted(dataSet.size() - objects.size(), dataSet.size() - 1);
    }

    @Override
    public int getItemCount()
    {
        return dataSet.size();
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
        public  TextView days;
        public  TextView hours;
        public  TextView minutes;
        public  TextView daysLabel;
        public  TextView hoursLabel;
        public  TextView minutesLabel;

        public ExpandedCampaignViewHolder(View itemView)
        {
            super(itemView);
            problemFact = (TextView) itemView.findViewById(R.id.problemFact);
            campaignDetails = itemView.findViewById(R.id.campaign_details);
            days = (TextView) itemView.findViewById(R.id.days);
            hours = (TextView) itemView.findViewById(R.id.hours);
            minutes = (TextView) itemView.findViewById(R.id.min);
            daysLabel = (TextView) itemView.findViewById(R.id.days_label);
            hoursLabel = (TextView) itemView.findViewById(R.id.hours_label);
            minutesLabel = (TextView) itemView.findViewById(R.id.minutes_label);
        }
    }

    public static class ReportBackViewHolder extends RecyclerView.ViewHolder
    {
        protected ImageView root;

        public ReportBackViewHolder(ImageView itemView)
        {
            super(itemView);
            this.root = itemView;
        }
    }

}
