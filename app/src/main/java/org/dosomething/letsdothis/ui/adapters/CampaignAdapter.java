package org.dosomething.letsdothis.ui.adapters;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.ui.views.ReportBackImageView;
import org.dosomething.letsdothis.ui.views.SlantedBackgroundDrawable;
import org.dosomething.letsdothis.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by izzyoji :) on 4/17/15.
 */
public class CampaignAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final int    VIEW_TYPE_CAMPAIGN          = 0;
    public static final int    VIEW_TYPE_CAMPAIGN_EXPANDED = 1;
    public static final int    VIEW_TYPE_CAMPAIGN_SMALL    = 2;
    public static final int    VIEW_TYPE_CAMPAIGN_FOOTER   = 3;
    public static final int    VIEW_TYPE_REPORT_BACK       = 4;
    public static final String PLACEHOLDER                 = "placeholder";

    private final int shadowColor;
    private final int slantHeight;
    private final int widthOvershoot;
    private final int heightShadowOvershoot;

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

    public CampaignAdapter(CampaignAdapterClickListener campaignAdapterClickListener, Resources resources)
    {
        super();
        this.campaignAdapterClickListener = campaignAdapterClickListener;
        shadowColor = resources.getColor(R.color.black_10);
        slantHeight = resources.getDimensionPixelSize(R.dimen.height_xxtiny);
        widthOvershoot = resources.getDimensionPixelSize(R.dimen.space_50);
        heightShadowOvershoot = resources.getDimensionPixelSize(R.dimen.padding_tiny);
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
                                                      .inflate(R.layout.item_report_back_square,
                                                               parent, false);
                return new ReportBackViewHolder((ReportBackImageView) reportBackLayout);
            case VIEW_TYPE_CAMPAIGN_EXPANDED:
                View bigLayout = LayoutInflater.from(parent.getContext())
                                               .inflate(R.layout.item_campaign_large, parent,
                                                        false);
                return new ExpandedCampaignViewHolder(bigLayout);
            case VIEW_TYPE_CAMPAIGN_SMALL:
                View smallLayout = LayoutInflater.from(parent.getContext())
                                                 .inflate(R.layout.item_campaign_small, parent,
                                                          false);
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

        Resources resources = holder.itemView.getResources();
        Context context = holder.itemView.getContext();

        if(getItemViewType(position) == VIEW_TYPE_CAMPAIGN)
        {
            final Campaign campaign = (Campaign) dataSet.get(position);
            CampaignViewHolder campaignViewHolder = (CampaignViewHolder) holder;
            campaignViewHolder.title.setText(campaign.title);
            campaignViewHolder.callToAction.setText(campaign.callToAction);

            int height = resources.getDimensionPixelSize(R.dimen.campaign_height);

            CharSequence tag = (CharSequence) campaignViewHolder.imageView.getTag();
            if(!TextUtils.equals(campaign.imagePath, tag))
            {
                Picasso.with(context).load(campaign.imagePath).resize(0, height)
                       .into(campaignViewHolder.imageView);
                campaignViewHolder.imageView.setTag(campaign.imagePath);
            }

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
            CampaignViewHolder smallCampaignViewHolder = (CampaignViewHolder) holder;
            smallCampaignViewHolder.title.setText(campaign.title);
            smallCampaignViewHolder.callToAction.setText(campaign.callToAction);

            int height = resources.getDimensionPixelSize(R.dimen.campaign_small_height);

            CharSequence tag = (CharSequence) smallCampaignViewHolder.imageView.getTag();
            if(!TextUtils.equals(campaign.imagePath, tag))
            {
                Picasso.with(context).load(campaign.imagePath).resize(0, height)
                       .into(smallCampaignViewHolder.imageView);
                smallCampaignViewHolder.imageView.setTag(campaign.imagePath);
            }
            smallCampaignViewHolder.root.setOnClickListener(new View.OnClickListener()
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

            int height = resources.getDimensionPixelSize(R.dimen.campaign_height_expanded);

            CharSequence tag = (CharSequence) expandedCampaignViewHolder.imageView.getTag();

            if(!TextUtils.equals(campaign.imagePath, tag))
            {
                Picasso.with(context).load(campaign.imagePath).resize(0, height)
                       .into(expandedCampaignViewHolder.imageView);
                expandedCampaignViewHolder.imageView.setTag(campaign.imagePath);
            }
            expandedCampaignViewHolder.imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    selectedPosition = - 1;
                    notifyItemChanged(position);
                }
            });

            SlantedBackgroundDrawable background = new SlantedBackgroundDrawable(false, Color.WHITE,
                                                                                 shadowColor,
                                                                                 slantHeight,
                                                                                 widthOvershoot,
                                                                                 heightShadowOvershoot);
            expandedCampaignViewHolder.slantedBg.setBackground(background);
            expandedCampaignViewHolder.campaignDetailsWrapper
                    .setOnClickListener(new View.OnClickListener()
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

            expandedCampaignViewHolder.daysLabel
                    .setText(resources.getQuantityString(R.plurals.days, Integer.parseInt(days)));
            expandedCampaignViewHolder.hoursLabel
                    .setText(resources.getQuantityString(R.plurals.hours, Integer.parseInt(hours)));
            expandedCampaignViewHolder.minutesLabel.setText(
                    resources.getQuantityString(R.plurals.minutes, Integer.parseInt(minutes)));

            int dayInt = Integer.parseInt(days);
            int hourInt = Integer.parseInt(hours);
            expandedCampaignViewHolder.daysWrapper.setVisibility(View.GONE);
            expandedCampaignViewHolder.hoursrWrapper.setVisibility(View.GONE);
            expandedCampaignViewHolder.minWrapper.setVisibility(View.GONE);
            if(dayInt > 0)
            {
                expandedCampaignViewHolder.daysWrapper.setVisibility(View.VISIBLE);
            }
            else if(hourInt > 0)
            {
                expandedCampaignViewHolder.hoursrWrapper.setVisibility(View.VISIBLE);
            }
            else
            {
                expandedCampaignViewHolder.minWrapper.setVisibility(View.VISIBLE);
            }
        }
        else if(getItemViewType(position) == VIEW_TYPE_REPORT_BACK)
        {
            final ReportBack reportBack = (ReportBack) dataSet.get(position);
            ReportBackViewHolder reportBackViewHolder = (ReportBackViewHolder) holder;

            Picasso.with(context).load(reportBack.getImagePath()).into(reportBackViewHolder.root);

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
            sectionTitleViewHolder.textView
                    .setText(resources.getString(R.string.people_doing_stuff));
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

    public void addAll(List<ReportBack> objects)
    {
        dataSet.addAll(objects);
        notifyItemRangeInserted(dataSet.size() - objects.size(), dataSet.size() - 1);
    }

    public void setCampaigns(List<Campaign> campaigns)
    {
        int indexOfPlaceHolder = dataSet.indexOf(PLACEHOLDER);
        if(indexOfPlaceHolder == - 1)
        {
            dataSet.addAll(campaigns);
            dataSet.add(PLACEHOLDER);
        }
        else
        {
            for(int i = 0, length = Math.min(dataSet.size(), indexOfPlaceHolder); i < length; i++)
            {
                dataSet.remove(i);
            }
            dataSet.addAll(0, campaigns);
        }
        notifyDataSetChanged();
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
        private         TextView problemFact;
        private         View     campaignDetailsWrapper;
        public          TextView days;
        public          TextView hours;
        public          TextView minutes;
        public          TextView daysLabel;
        public          TextView hoursLabel;
        public          TextView minutesLabel;
        private final   View     slantedBg;
        protected final View     daysWrapper;
        protected final View     hoursrWrapper;
        protected final View     minWrapper;

        public ExpandedCampaignViewHolder(View itemView)
        {
            super(itemView);
            daysWrapper = itemView.findViewById(R.id.days_wrapper);
            hoursrWrapper = itemView.findViewById(R.id.hours_wrapper);
            minWrapper = itemView.findViewById(R.id.min_wrapper);
            problemFact = (TextView) itemView.findViewById(R.id.problemFact);
            slantedBg = itemView.findViewById(R.id.slanted_bg);
            campaignDetailsWrapper = itemView.findViewById(R.id.campaign_details_wrapper);
            days = (TextView) itemView.findViewById(R.id.days);
            hours = (TextView) itemView.findViewById(R.id.hours);
            minutes = (TextView) itemView.findViewById(R.id.min);
            daysLabel = (TextView) itemView.findViewById(R.id.days_label);
            hoursLabel = (TextView) itemView.findViewById(R.id.hours_label);
            minutesLabel = (TextView) itemView.findViewById(R.id.minutes_label);
        }
    }

}
