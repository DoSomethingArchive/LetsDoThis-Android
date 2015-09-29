package org.dosomething.letsdothis.ui.adapters;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.Kudos;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.ui.views.SlantedBackgroundDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by izzyoji :) on 4/30/15.
 */
public class CampaignDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final int VIEW_TYPE_CAMPAIGN        = 0;
    public static final int VIEW_TYPE_CAMPAIGN_FOOTER = 1;
    public static final int VIEW_TYPE_REPORT_BACK     = 2;

    private final int webOrange;
    private final int shadowColor;
    private final int slantHeight;
    private final int widthOvershoot;
    private final int heightShadowOvershoot;
    private final int drupalId;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private ArrayList<Object> dataSet = new ArrayList<>();
    private DetailsAdapterClickListener detailsAdapterClickListener;

    private Campaign currentCampaign;
    private int                    selectedPosition = - 1;
    private Random                 random           = new Random();
    private HashMap<Integer, Kudos> kudosedMap       = new HashMap<>();

    public CampaignDetailsAdapter(DetailsAdapterClickListener detailsAdapterClickListener, Resources resources, int drupalId)
    {
        super();
        this.detailsAdapterClickListener = detailsAdapterClickListener;
        this.drupalId = drupalId;
        webOrange = resources.getColor(R.color.web_orange);
        shadowColor = resources.getColor(R.color.black_10);
        slantHeight = resources.getDimensionPixelSize(R.dimen.height_xtiny);
        widthOvershoot = resources.getDimensionPixelSize(R.dimen.space_50);
        heightShadowOvershoot = resources.getDimensionPixelSize(R.dimen.padding_tiny);
    }

    public void updateCampaign(Campaign campaign)
    {
        if(dataSet.isEmpty())
        {
            dataSet.add(campaign);
            dataSet.add("footer item_placeholder");
            notifyItemInserted(0);
        }
        else
        {
            if(currentCampaign == null)
            {
                dataSet.add(0, campaign);
                dataSet.add("footer item_placeholder");
                notifyItemInserted(0);
            }
            else
            {
                dataSet.set(0, campaign);
                notifyItemChanged(0);
            }
        }
        currentCampaign = campaign;
    }

    public void addAll(List<ReportBack> reportBacks)
    {
        dataSet.addAll(reportBacks);
        notifyItemRangeInserted(dataSet.size() - reportBacks.size(), dataSet.size() - 1);
    }

    public Campaign getCampaign()
    {
        return currentCampaign;
    }

    public void processingUpload()
    {
        if(currentCampaign != null)
        {
            currentCampaign.showShare = Campaign.UploadShare.UPLOADING;
            dataSet.set(0, currentCampaign);
            notifyItemChanged(0);
        }
    }

    public interface DetailsAdapterClickListener
    {
        void onScrolledToBottom();

        void proveClicked();

        void shareClicked(Campaign campaign);

        void inviteClicked();

        void onUserClicked(String id);

        void onKudosClicked(ReportBack reportBack, Kudos kudos);
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
                        .inflate(R.layout.item_report_back_expanded, parent, false);
                return new ReportBackViewHolder(reportBackLayout);
            default:
                View campaignLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_campaign_details, parent, false);
                return new CampaignViewHolder(campaignLayout);

        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        if (position == dataSet.size() - 3) {
            detailsAdapterClickListener.onScrolledToBottom();
        }

        if(getItemViewType(position) == VIEW_TYPE_CAMPAIGN)
        {
            final Campaign campaign = (Campaign) dataSet.get(position);
            CampaignViewHolder campaignViewHolder = (CampaignViewHolder) holder;

            Resources res = campaignViewHolder.imageView.getContext().getResources();
            int height = res.getDimensionPixelSize(R.dimen.campaign_height_expanded);
            Picasso.with(campaignViewHolder.imageView.getContext()).load(campaign.imagePath)
                    .resize(0, height).into(campaignViewHolder.imageView);
            campaignViewHolder.title.setText(campaign.title);
            campaignViewHolder.callToAction.setText(campaign.callToAction);

            if (campaign.solutionCopy != null) {
                campaignViewHolder.solutionCopy.setText(campaign.solutionCopy.trim());
            }
            else {
                campaignViewHolder.solutionCopy.setVisibility(View.GONE);
            }

            if (campaign.solutionSupport != null) {
                campaignViewHolder.solutionSupport.setText(campaign.solutionSupport.trim());
            }
            else {
                campaignViewHolder.solutionSupport.setVisibility(View.GONE);
            }

            SlantedBackgroundDrawable background = new SlantedBackgroundDrawable(true, webOrange,
                                                                                 shadowColor,
                                                                                 slantHeight,
                                                                                 widthOvershoot,
                                                                                 heightShadowOvershoot);
            campaignViewHolder.solutionWrapper.setBackground(background);
            if(campaign.showShare == Campaign.UploadShare.SHARE)
            {
                campaignViewHolder.proveShare.setText(res.getString(R.string.share_photo));
            }
            else if(campaign.showShare == Campaign.UploadShare.UPLOADING)
            {
                campaignViewHolder.proveShare.setText(res.getString(R.string.uploading));
            }
            else if(campaign.showShare == Campaign.UploadShare.SHOW_OFF)
            {
                campaignViewHolder.proveShare.setText(res.getString(R.string.show_off));
            }

            campaignViewHolder.proveShare.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(campaign.showShare == Campaign.UploadShare.SHARE)
                    {
                        detailsAdapterClickListener.shareClicked(campaign);
                    }
                    else if(campaign.showShare == Campaign.UploadShare.SHOW_OFF)
                    {
                        detailsAdapterClickListener.proveClicked();
                    }
                }
            });
        }
        else if(getItemViewType(position) == VIEW_TYPE_REPORT_BACK)
        {
            final ReportBack reportBack = (ReportBack) dataSet.get(position);
            final ReportBackViewHolder reportBackViewHolder = (ReportBackViewHolder) holder;
            final Context context = reportBackViewHolder.itemView.getContext();

            // Report back photo
            Picasso.with(context).
                    load(reportBack.getImagePath()).into(reportBackViewHolder.imageView);

            // User name
            String reportbackName = reportBack.user.first_name;
            if (reportBack.user.last_name != null && !reportBack.user.last_name.isEmpty()) {
                reportbackName += " " + reportBack.user.last_name.charAt(0) + ".";
            }
            reportBackViewHolder.name.setText(reportbackName);

            // User country location
            if (reportBack.user.country != null && !reportBack.user.country.isEmpty()) {
                Locale locale = new Locale("", reportBack.user.country);
                reportBackViewHolder.location.setText(locale.getDisplayCountry());
            }

            // User profile photo
            if (reportBack.user.avatarPath != null && !reportBack.user.avatarPath.isEmpty()) {
                Picasso.with(context).load(reportBack.user.avatarPath)
                        .placeholder(R.drawable.default_profile_photo)
                        .resizeDimen(R.dimen.friend_avatar, R.dimen.friend_avatar)
                        .into(reportBackViewHolder.avatar);
            }

            // Report back campaign name and details
            reportBackViewHolder.title.setText(currentCampaign.title);
            reportBackViewHolder.caption.setText(reportBack.caption);

            String impactText = String.format("%s %s %s", String.valueOf(reportBack.reportback.quantity),
                    currentCampaign.noun, currentCampaign.verb);
            reportBackViewHolder.impact.setText(impactText);

            // Click listeners on the user name and user photo
            OnUserClickListener onUserClickListener = new OnUserClickListener(reportBack.user.id);
            reportBackViewHolder.avatar.setOnClickListener(onUserClickListener);
            reportBackViewHolder.name.setOnClickListener(onUserClickListener);
        }
        else if(getItemViewType(position) == VIEW_TYPE_CAMPAIGN_FOOTER)
        {
            SectionTitleViewHolder sectionTitleViewHolder = (SectionTitleViewHolder) holder;
            sectionTitleViewHolder.textView.setText(sectionTitleViewHolder.textView.getContext()
                                                                                   .getString(R.string.people_doing_it));
        }

    }

    @Override
    public int getItemViewType(int position)
    {
        Object currentObject = dataSet.get(position);
        if(currentObject instanceof Campaign)
        {
            return VIEW_TYPE_CAMPAIGN;

        }
        else if(currentObject instanceof ReportBack)
        {
            return VIEW_TYPE_REPORT_BACK;
        }
        else
        {
            return VIEW_TYPE_CAMPAIGN_FOOTER;
        }
    }


    @Override
    public int getItemCount()
    {
        return dataSet.size();
    }

    private class CampaignViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView  solutionSupport;
        protected TextView  solutionCopy;
        protected ImageView imageView;
        protected TextView  title;
        protected TextView  callToAction;
        protected Button    proveShare;
        public    View      solutionWrapper;

        public CampaignViewHolder(View itemView)
        {
            super(itemView);
            this.solutionWrapper = itemView.findViewById(R.id.solutionWrapper);
            this.imageView = (ImageView) itemView.findViewById(R.id.image);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.callToAction = (TextView) itemView.findViewById(R.id.call_to_action);
            this.solutionCopy = (TextView) itemView.findViewById(R.id.solutionCopy);
            this.solutionSupport = (TextView) itemView.findViewById(R.id.solutionSupport);
            this.proveShare = (Button) itemView.findViewById(R.id.prove_share);
        }
    }

    private class ReportBackViewHolder extends RecyclerView.ViewHolder
    {
        protected ImageView avatar;
        protected ImageView imageView;
        protected TextView  name;
        protected TextView  location;
        protected TextView  caption;
        protected TextView  impact;
        protected TextView  title;

        public ReportBackViewHolder(View view)
        {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.image);
            this.avatar = (ImageView) view.findViewById(R.id.avatar);
            this.name = (TextView) view.findViewById(R.id.name);
            this.location = (TextView) view.findViewById(R.id.location);
            this.caption = (TextView) view.findViewById(R.id.caption);
            this.impact = (TextView) view.findViewById(R.id.impact);
            this.title = (TextView) view.findViewById(R.id.title);
        }
    }

    /**
     * OnClickListener to open up a user's public profile screen.
     */
    private class OnUserClickListener implements View.OnClickListener {
        private String mUserId;

        public OnUserClickListener(String id) {
            mUserId = id;
        }

        @Override
        public void onClick(View v) {
            detailsAdapterClickListener.onUserClicked(mUserId);
        }
    }

}
