package org.dosomething.letsdothis.ui.adapters;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by izzyoji :) on 4/17/15.
 */
public class CampaignAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final int    VIEW_TYPE_CAMPAIGN           = 0;
    public static final int    VIEW_TYPE_CAMPAIGN_EXPANDED  = 1;
    public static final int    V_TYPE_CAMP_EXPANDED_EXPIRED = 2;
    public static final int    VIEW_TYPE_CAMPAIGN_SMALL     = 3;
    public static final int    VIEW_TYPE_CAMPAIGN_FOOTER    = 4;
    public static final int    VIEW_TYPE_REPORT_BACK        = 5;
    public static final String PLACEHOLDER                  = "placeholder";

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
        void onCampaignClicked(int campaignId, boolean alreadySignedUp);

        void onCampaignExpanded(int position);

        void onReportBackClicked(int reportBackId);

        void onScrolledToBottom();

        void onNetworkCampaignRefresh();
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
    public int getItemViewType(int position)
    {
        Object currentObject = dataSet.get(position);
        if(currentObject instanceof Campaign)
        {
            if(position == selectedPosition)
            {
                Campaign camp = (Campaign) currentObject;
                if(TimeUtils.isCampaignExpired(camp))
                {
                    return V_TYPE_CAMP_EXPANDED_EXPIRED;
                }
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v;
        switch(viewType)
        {
            case VIEW_TYPE_CAMPAIGN_FOOTER:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_campaign_footer, parent, false);
                return new SectionTitleViewHolder((TextView) v);
            case VIEW_TYPE_REPORT_BACK:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_report_back_square, parent, false);
                return new ReportBackViewHolder((ReportBackImageView) v);
            case V_TYPE_CAMP_EXPANDED_EXPIRED:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_campaign_expired_large, parent, false);
                return new ExpandedExpireCampaignViewHolder(v);
            case VIEW_TYPE_CAMPAIGN_EXPANDED:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_campaign_large, parent, false);
                return new ExpandedCampaignViewHolder(v);
            case VIEW_TYPE_CAMPAIGN_SMALL:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_campaign_small, parent, false);
                return new CampaignViewHolder(v);
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_campaign, parent, false);
                return new CampaignViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        if (position == dataSet.size() - 3) {
            campaignAdapterClickListener.onScrolledToBottom();
        }

        Resources resources = holder.itemView.getResources();
        Context context = holder.itemView.getContext();

        if(getItemViewType(position) == VIEW_TYPE_CAMPAIGN)
        {
            final Campaign campaign = (Campaign) dataSet.get(position);
            CampaignViewHolder campaignViewHolder = (CampaignViewHolder) holder;
            campaignViewHolder.title.setText(campaign.title);

            int height = resources.getDimensionPixelSize(R.dimen.campaign_height);

            CharSequence tag = (CharSequence) campaignViewHolder.imageView.getTag();
            if(! TextUtils.equals(campaign.imagePath, tag))
            {
                Picasso.with(context).load(campaign.imagePath).resize(0, height)
                        .into(campaignViewHolder.imageView);
                campaignViewHolder.imageView.setTag(campaign.imagePath);
            }

            if (campaign.userIsSignedUp) {
                campaignViewHolder.signedupIndicator.setVisibility(View.VISIBLE);
            }
            else {
                campaignViewHolder.signedupIndicator.setVisibility(View.GONE);
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

            ColorMatrix cm = new ColorMatrix();
            if(TimeUtils.isCampaignExpired(campaign))
            {
                cm.setSaturation(0);
                campaignViewHolder.imageView.setColorFilter(new ColorMatrixColorFilter(cm));
            }
            else
            {
                cm.setSaturation(1);
                campaignViewHolder.imageView.setColorFilter(new ColorMatrixColorFilter(cm));
            }
        }
        if(getItemViewType(position) == VIEW_TYPE_CAMPAIGN_SMALL)
        {
            final Campaign campaign = (Campaign) dataSet.get(position);
            CampaignViewHolder smallCampaignViewHolder = (CampaignViewHolder) holder;
            smallCampaignViewHolder.title.setText(campaign.title);

            int height = resources.getDimensionPixelSize(R.dimen.campaign_small_height);

            CharSequence tag = (CharSequence) smallCampaignViewHolder.imageView.getTag();
            if(! TextUtils.equals(campaign.imagePath, tag))
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

            if (campaign.userIsSignedUp) {
                smallCampaignViewHolder.signedupIndicator.setVisibility(View.VISIBLE);
            }
            else {
                smallCampaignViewHolder.signedupIndicator.setVisibility(View.GONE);
            }


            ColorMatrix cm = new ColorMatrix();
            if(TimeUtils.isCampaignExpired(campaign))
            {
                cm.setSaturation(0);
                smallCampaignViewHolder.imageView.setColorFilter(new ColorMatrixColorFilter(cm));
            }
            else
            {
                cm.setSaturation(1);
                smallCampaignViewHolder.imageView.setColorFilter(new ColorMatrixColorFilter(cm));
            }
        }
        else if(getItemViewType(position) == V_TYPE_CAMP_EXPANDED_EXPIRED)
        {
            final Campaign campaign = (Campaign) dataSet.get(position);
            final ExpandedExpireCampaignViewHolder viewHolder = (ExpandedExpireCampaignViewHolder) holder;

            viewHolder.title.setText(campaign.title);
            viewHolder.callToAction.setText(campaign.callToAction);

            int height = resources.getDimensionPixelSize(R.dimen.campaign_height_expanded);

            CharSequence tag = (CharSequence) viewHolder.imageView.getTag();

            if(! TextUtils.equals(campaign.imagePath, tag))
            {
                Picasso.with(context).load(campaign.imagePath).resize(0, height)
                        .into(viewHolder.imageView);
                viewHolder.imageView.setTag(campaign.imagePath);
            }
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = -1;
                    notifyItemChanged(position);
                }
            });

            final SlantedBackgroundDrawable background = new SlantedBackgroundDrawable(false,
                                                                                       Color.WHITE,
                                                                                       shadowColor,
                                                                                       slantHeight,
                                                                                       widthOvershoot,
                                                                                       heightShadowOvershoot);
            viewHolder.slantedBg.setBackground(background);
            viewHolder.refreshCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    campaignAdapterClickListener.onNetworkCampaignRefresh();
                    //FIXME show progress bar
                }
            });

            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            viewHolder.imageView.setColorFilter(new ColorMatrixColorFilter(cm));
        }
        else if(getItemViewType(position) == VIEW_TYPE_CAMPAIGN_EXPANDED)
        {
            final Campaign campaign = (Campaign) dataSet.get(position);
            ExpandedCampaignViewHolder viewHolder = (ExpandedCampaignViewHolder) holder;

            viewHolder.title.setText(campaign.title);
            viewHolder.callToAction.setText(campaign.callToAction);

            int height = resources.getDimensionPixelSize(R.dimen.campaign_height_expanded);

            CharSequence tag = (CharSequence) viewHolder.imageView.getTag();

            if(! TextUtils.equals(campaign.imagePath, tag))
            {
                Picasso.with(context).load(campaign.imagePath).resize(0, height)
                        .into(viewHolder.imageView);
                viewHolder.imageView.setTag(campaign.imagePath);
            }
            viewHolder.imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    selectedPosition = - 1;
                    notifyItemChanged(position);
                }
            });

            if (campaign.userIsSignedUp) {
                viewHolder.signedupIndicator.setVisibility(View.VISIBLE);
                viewHolder.alreadySignedUp.setVisibility(View.VISIBLE);
                viewHolder.notSignedUp.setVisibility(View.GONE);
            }
            else {
                viewHolder.signedupIndicator.setVisibility(View.GONE);
                viewHolder.alreadySignedUp.setVisibility(View.GONE);
                viewHolder.notSignedUp.setVisibility(View.VISIBLE);
            }

            SlantedBackgroundDrawable background = new SlantedBackgroundDrawable(false, Color.WHITE,
                                                                                 shadowColor,
                                                                                 slantHeight,
                                                                                 widthOvershoot,
                                                                                 heightShadowOvershoot);
            viewHolder.slantedBg.setBackground(background);
            viewHolder.campaignDetailsWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    campaignAdapterClickListener.onCampaignClicked(campaign.id, campaign.userIsSignedUp);
                }
            });

            // If campaign.endTime isn't specified by the server, then don't show the expires label
            if (campaign.endTime == 0) {
                viewHolder.expire_label.setVisibility(View.GONE);
                viewHolder.daysWrapper.setVisibility(View.GONE);
            }
            else {
                viewHolder.expire_label.setVisibility(View.VISIBLE);
                viewHolder.daysWrapper.setVisibility(View.VISIBLE);

                List<String> campExpTime = TimeUtils.getTimeUntilExpiration(campaign.endTime);
                int dayInt = Integer.parseInt(campExpTime.get(0));
                viewHolder.daysLabel.setText(resources.getQuantityString(R.plurals.days, dayInt));
                viewHolder.days.setText(String.valueOf(dayInt));
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

    public void addAll(List<ReportBack> objects)
    {
        if(! dataSet.isEmpty() && objects != null)
        {
            dataSet.addAll(objects);
            notifyItemRangeInserted(dataSet.size() - objects.size(), dataSet.size() - 1);
        }

    }

    public void setCampaigns(List<Campaign> campaigns)
    {
        // Sort campaigns in alphabetical order
        Collections.sort(campaigns, new Comparator<Campaign>() {
            public int compare(Campaign c1, Campaign c2) {
                return c1.title.compareToIgnoreCase(c2.title);
            }
        });

        int indexOfPlaceHolder = dataSet.indexOf(PLACEHOLDER);
        if(indexOfPlaceHolder == - 1)
        {
            dataSet.addAll(campaigns);
            dataSet.add(PLACEHOLDER);
        }
        else
        {
            int size = dataSet.size();
            for(int i = 0; i < campaigns.size(); i++)
            {
                Campaign c = campaigns.get(i);
                if(size > i && dataSet.get(i) instanceof Campaign)
                {
                    dataSet.set(i, c);
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * Update the adapter's data set indicating a user is signed up for a given campaign. Then
     * refresh the view.
     *
     * @param id Campaign id the user is signed up for
     */
    public void userSignedUpForCampaign(int id) {
        for (int i = 0; i < dataSet.size(); i++) {
            if (dataSet.get(i) instanceof Campaign) {
                Campaign c = (Campaign)dataSet.get(i);
                if (c.id == id && !c.userIsSignedUp) {
                    c.userIsSignedUp = true;
                    notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return dataSet.size();
    }

    public void clear()
    {
        dataSet.clear();
        notifyDataSetChanged();
    }

    public static class CampaignViewHolder extends RecyclerView.ViewHolder
    {
        protected View      root;
        protected ImageView imageView;
        protected TextView  title;
        protected View      signedupIndicator;

        public CampaignViewHolder(View itemView)
        {
            super(itemView);
            this.root = itemView;
            this.imageView = (ImageView) itemView.findViewById(R.id.image);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.signedupIndicator = itemView.findViewById(R.id.signedup_indicator);
        }
    }

    public static class ExpandedCampaignViewHolder extends CampaignViewHolder
    {
        private         View     campaignDetailsWrapper;
        public          TextView callToAction;
        public          TextView expire_label;
        public          TextView days;
        public          TextView daysLabel;
        private final   View     slantedBg;
        private final   View     notSignedUp;
        private final   View     alreadySignedUp;
        protected final View     signedupIndicator;
        protected final View     daysWrapper;

        public ExpandedCampaignViewHolder(View itemView)
        {
            super(itemView);
            expire_label = (TextView) itemView.findViewById(R.id.expire_label);
            callToAction = (TextView) itemView.findViewById(R.id.call_to_action);
            daysWrapper = itemView.findViewById(R.id.days_wrapper);
            slantedBg = itemView.findViewById(R.id.slanted_bg);
            signedupIndicator = itemView.findViewById(R.id.signedup_indicator);
            campaignDetailsWrapper = itemView.findViewById(R.id.campaign_details_wrapper);
            notSignedUp = campaignDetailsWrapper.findViewById(R.id.not_signedup);
            alreadySignedUp = campaignDetailsWrapper.findViewById(R.id.signedup);
            days = (TextView) itemView.findViewById(R.id.days);
            daysLabel = (TextView) itemView.findViewById(R.id.days_label);
        }
    }

    public static class ExpandedExpireCampaignViewHolder extends CampaignViewHolder
    {
        private       View     refreshCopy;
        public        TextView callToAction;
        public        TextView expired;
        private final View     slantedBg;

        public ExpandedExpireCampaignViewHolder(View itemView)
        {
            super(itemView);
            expired = (TextView) itemView.findViewById(R.id.expired_already);
            callToAction = (TextView) itemView.findViewById(R.id.call_to_action);
            slantedBg = itemView.findViewById(R.id.slanted_bg);
            refreshCopy = itemView.findViewById(R.id.refresh_copy);
        }
    }
}
