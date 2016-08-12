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
import org.dosomething.letsdothis.data.CampaignActionGuide;
import org.dosomething.letsdothis.data.CampaignAttachment;
import org.dosomething.letsdothis.data.Kudos;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Adapter to display and interface with data on the campaign details screen.
 *
 * Created by izzyoji :) on 4/30/15.
 */
public class CampaignDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final int VIEW_TYPE_CAMPAIGN        = 0;
    public static final int VIEW_TYPE_CAMPAIGN_FOOTER = 1;
    public static final int VIEW_TYPE_REPORT_BACK     = 2;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private ArrayList<Object> dataSet = new ArrayList<>();
    private DetailsAdapterClickListener detailsAdapterClickListener;

    private Campaign currentCampaign;

    // Flag indicating if the current user is signed up for this campaign
    private boolean mUserIsSignedUp;

    // Flag indicating a signup is in progress
    private boolean mSignupInProgress;

    public CampaignDetailsAdapter(DetailsAdapterClickListener detailsAdapterClickListener,
                                  Resources resources, boolean isSignedUp) {
        super();
        this.detailsAdapterClickListener = detailsAdapterClickListener;
        mUserIsSignedUp = isSignedUp;
        mSignupInProgress = false;
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

    /**
     * Update the view of the adapter when the
     */
    public void refreshOnSignup() {
        mUserIsSignedUp = true;
        mSignupInProgress = false;

        notifyDataSetChanged();
    }

    public interface DetailsAdapterClickListener
    {
        void onScrolledToBottom();

        void proveClicked();

        void shareClicked(Campaign campaign);

        void onUserClicked(String id);

        void onKudosClicked(ReportBack reportBack, Kudos kudos);

        void onSignupClicked(int campaignId);

        void showError(int resourceId);

		void showAttachment(String title, String uri);

		void showActionGuides(ArrayList<CampaignActionGuide> actionGuides);
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
        if (position >= dataSet.size() - 3) {
            detailsAdapterClickListener.onScrolledToBottom();
        }

        if (getItemViewType(position) == VIEW_TYPE_CAMPAIGN) {
            final Campaign campaign = (Campaign) dataSet.get(position);
            CampaignViewHolder campaignViewHolder = (CampaignViewHolder) holder;

            Resources res = campaignViewHolder.imageView.getContext().getResources();
            int height = res.getDimensionPixelSize(R.dimen.campaign_height_expanded);
            if (campaign.imagePath == null || campaign.imagePath.isEmpty()) {
                Picasso.with(campaignViewHolder.imageView.getContext())
                        .load(R.drawable.image_error)
                        .resize(0, height)
                        .into(campaignViewHolder.imageView);
            } else {
                Picasso.with(campaignViewHolder.imageView.getContext())
                        .load(campaign.imagePath)
                        .placeholder(R.drawable.image_loading)
                        .resize(0, height).into(campaignViewHolder.imageView);
            }

            campaignViewHolder.title.setText(campaign.title);
            campaignViewHolder.callToAction.setText(campaign.callToAction);

            // Signup progress bar
            if (mSignupInProgress) {
                campaignViewHolder.signupProgress.setVisibility(View.VISIBLE);
            }
            else {
                campaignViewHolder.signupProgress.setVisibility(View.GONE);
            }

            // Sponsor section
            if (campaign.sponsorLogo != null) {
                campaignViewHolder.sponsor.setVisibility(View.VISIBLE);
                Picasso.with(campaignViewHolder.sponsorLogo.getContext())
                        .load(campaign.sponsorLogo)
                        .into(campaignViewHolder.sponsorLogo);
            }
            else {
                campaignViewHolder.sponsor.setVisibility(View.GONE);
            }

            // Solution section
            if (!mUserIsSignedUp) {
                campaignViewHolder.solutionWrapper.setVisibility(View.GONE);
            }
            else {
                campaignViewHolder.solutionWrapper.setVisibility(View.VISIBLE);

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
            }

			// Resources section
			if (!campaign.hasAnyAttachments() && !campaign.hasAnyActionGuides()) {
				// Hide attachments
				campaignViewHolder.resourcesGroup.setVisibility(View.GONE);
			} else {
				// Populate attachments
				for (CampaignAttachment attachment : campaign.getAttachments()) {
					// Add the current attachment
					ViewGroup rowGroup = (ViewGroup) LayoutInflater.from(campaignViewHolder.resourcesGroup.getContext()).
							inflate(R.layout.item_campaign_attachment, campaignViewHolder.resourcesGroup, false);
					((TextView) rowGroup.findViewById(R.id.attachmentTitle)).setText(attachment.getTitle());
					rowGroup.setTag(attachment.getUri());
					rowGroup.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// Show this attachment
							String title = ((TextView) v.findViewById(R.id.attachmentTitle)).getText().toString();
							String uri = (String) v.getTag();
							detailsAdapterClickListener.showAttachment(title, uri);
						}
					});
					campaignViewHolder.resourcesGroup.addView(rowGroup);
				}
				if (campaign.hasAnyActionGuides()) {
					// Setup row to offer the Action Guides
					final ArrayList<CampaignActionGuide> actionGuides = new ArrayList<CampaignActionGuide>();
					for(CampaignActionGuide guide : campaign.getActionGuides()) {
						actionGuides.add(guide);
					}

					// Show the Action Guides row
					ViewGroup rowGroup = (ViewGroup) LayoutInflater.from(campaignViewHolder.resourcesGroup.getContext()).
							inflate(R.layout.item_campaign_attachment, campaignViewHolder.resourcesGroup, false);
					((TextView) rowGroup.findViewById(R.id.attachmentTitle)).setText(
							campaignViewHolder.resourcesGroup.getContext().getString(R.string.action_guides));
					rowGroup.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// Show the action guides
							detailsAdapterClickListener.showActionGuides(actionGuides);
						}
					});
					campaignViewHolder.resourcesGroup.addView(rowGroup);
				}
			}

            // Action button
            campaignViewHolder.actionButton.setVisibility(View.VISIBLE);
            if (! mUserIsSignedUp) {
                campaignViewHolder.actionButton.setText(res.getString(R.string.stop_being_bored));
            }
            else if (campaign.showShare == Campaign.UploadShare.SHARE) {
                campaignViewHolder.actionButton.setText(R.string.cta_photo_in_hub);
            }
            else if (campaign.showShare == Campaign.UploadShare.UPLOADING) {
                campaignViewHolder.actionButton.setText(res.getString(R.string.uploading));
            }
            else if (campaign.showShare == Campaign.UploadShare.SHOW_OFF) {
                campaignViewHolder.actionButton.setText(res.getString(R.string.show_off));
            }

            campaignViewHolder.actionButton.setOnClickListener(new OnActionClickListener(campaign));
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
            String name = ViewUtils.formatUserDisplayName(context, reportBack.user.first_name,
                    reportBack.user.last_initial);
            reportBackViewHolder.name.setText(name);

            // User country location
            if (reportBack.user.country != null && !reportBack.user.country.isEmpty()) {
                Locale locale = new Locale("", reportBack.user.country);
                reportBackViewHolder.location.setText(locale.getDisplayCountry());
            }

            // User profile photo
            if (reportBack.user.photo != null && !reportBack.user.photo.isEmpty()) {
                Picasso.with(context).load(reportBack.user.photo)
                        .placeholder(R.drawable.default_profile_photo)
                        .resizeDimen(R.dimen.friend_avatar, R.dimen.friend_avatar)
                        .into(reportBackViewHolder.avatar);
            }
            else {
                Picasso.with(context).load(R.drawable.default_profile_photo)
                        .resizeDimen(R.dimen.friend_avatar, R.dimen.friend_avatar)
                        .into(reportBackViewHolder.avatar);
            }

            // Report back campaign name and details

            final String title = currentCampaign != null ? currentCampaign.title : "";
            final String noun = currentCampaign != null ? currentCampaign.noun: "";
            final String verb = currentCampaign != null ? currentCampaign.verb : "";
            reportBackViewHolder.title.setText(title);
            reportBackViewHolder.caption.setText(reportBack.caption);

            String impactText = String.format("%s %s %s", String.valueOf(reportBack.reportback.quantity),
                    noun, verb);
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
        protected Button    actionButton;
        protected View      signupProgress;
        public    View      solutionWrapper;
        protected View      sponsor;
        protected ImageView sponsorLogo;
		protected ViewGroup resourcesGroup;

        public CampaignViewHolder(View itemView)
        {
            super(itemView);
            this.solutionWrapper = itemView.findViewById(R.id.solutionWrapper);
            this.imageView = (ImageView) itemView.findViewById(R.id.image);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.callToAction = (TextView) itemView.findViewById(R.id.call_to_action);
            this.solutionCopy = (TextView) itemView.findViewById(R.id.solutionCopy);
            this.solutionSupport = (TextView) itemView.findViewById(R.id.solutionSupport);
            this.actionButton = (Button) itemView.findViewById(R.id.action_button);
            this.signupProgress = itemView.findViewById(R.id.progress);
            this.sponsor = itemView.findViewById(R.id.sponsor);
            this.sponsorLogo = (ImageView) itemView.findViewById(R.id.sponsor_logo);
			this.resourcesGroup = (ViewGroup) itemView.findViewById(R.id.campaignResources);
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

    private class OnActionClickListener implements View.OnClickListener {

        private Campaign campaign;

        public OnActionClickListener(Campaign campaign) {
            this.campaign = campaign;
        }

        @Override
        public void onClick(View v) {
            boolean isSmsGame = campaign.type != null ? campaign.type.equals("sms_game") : false;
            boolean isClosed = campaign.status != null ? campaign.status.equals("closed") : false;

            if (isSmsGame) {
                detailsAdapterClickListener.showError(R.string.error_action_sms_game);
            }
            else if (isClosed && campaign.showShare != Campaign.UploadShare.SHARE) {
                detailsAdapterClickListener.showError(R.string.error_action_closed_campaign);
            }
            else if (!mUserIsSignedUp) {
                mSignupInProgress = true;
                notifyDataSetChanged();

                detailsAdapterClickListener.onSignupClicked(campaign.id);
            }
            else if (campaign.showShare == Campaign.UploadShare.SHARE) {
                detailsAdapterClickListener.shareClicked(campaign);
            }
            else if (campaign.showShare == Campaign.UploadShare.SHOW_OFF) {
                detailsAdapterClickListener.proveClicked();
            }
        }
    }
}
