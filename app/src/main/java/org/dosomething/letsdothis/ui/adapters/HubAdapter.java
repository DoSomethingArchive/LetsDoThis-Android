package org.dosomething.letsdothis.ui.adapters;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.data.UserReportBack;
import org.dosomething.letsdothis.network.models.ResponseProfileCampaign;
import org.dosomething.letsdothis.network.models.ResponseProfileSignups;
import org.dosomething.letsdothis.ui.CampaignDetailsActivity;
import org.dosomething.letsdothis.ui.ReportBackDetailsActivity;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by toidiu on 4/17/15.
 */
public class HubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    private static final int    VIEW_TYPE_PROFILE          = 0;
    private static final int    VIEW_TYPE_SECTION_TITLE    = 1;
    private static final int    VIEW_TYPE_CURRENT_CAMPAIGN = 2;
    private static final int    VIEW_TYPE_PAST_CAMPAIGN    = 3;
    private static final int    VIEW_TYPE_CURRENT_EMPTY    = 4;
    private static final int    VIEW_TYPE_PUBLIC_EMPTY     = 5;
    private static final int    VIEW_TYPE_CURRENT_SIGNUPS  = 6;
    private static final int    VIEW_TYPE_REPORTBACKS      = 7;

    private static final String CURRENT_SIGNUPS_LABEL_STUB = "PLACEHOLDER: CURRENT_SIGNUPS_LABEL_STUB";
    private static final String CURRENT_CAMPAIGNS_EMPTY_STUB = "PLACEHOLDER: CURRENT_CAMPAIGNS_EMPTY_STUB";
    private static final String REPORTBACKS_LABEL_STUB = "PLACEHOLDER: REPORTBACKS_LABEL_STUB";

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private ArrayList<Object> mHubList = new ArrayList<>();
    private HubAdapterClickListener mHubAdapterClickListener;
    private boolean mIsPublic = false;
    private Campaign mClickedCampaign;
    private Context mContext;
    private User mUser;

    public HubAdapter(Context context, HubAdapterClickListener hubAdapterClickListener, boolean isPublic) {
        super();

        this.mContext = context;
        this.mHubAdapterClickListener = hubAdapterClickListener;

        // First row is user profile info
        setUser(new User(null, ""));

        // Second row is the section label for current signups
        mHubList.add(CURRENT_SIGNUPS_LABEL_STUB);

        this.mIsPublic = isPublic;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case VIEW_TYPE_SECTION_TITLE:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_campaign_footer, parent, false);
                return new SectionTitleViewHolder((TextView) v);
            case VIEW_TYPE_PROFILE:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_hub_profile, parent, false);
                return new ProfileViewHolder(v);
            case VIEW_TYPE_CURRENT_CAMPAIGN:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_hub_current_campaign, parent, false);
                return new CurrentCampaignViewHolder(v);
            // @todo May not make sense anymore. Consider replacing it with something like a VIEW_TYPE_REPORTBACKS
            case VIEW_TYPE_PAST_CAMPAIGN:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_hub_past_campaign, parent, false);
                return new PastCampaignViewHolder(v);
            case VIEW_TYPE_CURRENT_EMPTY:
                v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_hub_current_empty, parent, false);
                return new EmptyViewHolder(v);
            case VIEW_TYPE_PUBLIC_EMPTY:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_hub_public_empty, parent, false);
                return new PublicEmptyViewHolder(v);
            case VIEW_TYPE_CURRENT_SIGNUPS:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_hub_current_campaign, parent, false);
                return new CurrentCampaignViewHolder(v);
            case VIEW_TYPE_REPORTBACKS:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_report_back_expanded, parent, false);
                return new ReportBackViewHolder(v);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == VIEW_TYPE_SECTION_TITLE) {
            SectionTitleViewHolder viewHolder = (SectionTitleViewHolder) holder;
            String s = (String) mHubList.get(position);
            if (s.equals(CURRENT_SIGNUPS_LABEL_STUB)) {
                viewHolder.textView.setText(R.string.hub_current_label);
            }
            else if (s.equals(REPORTBACKS_LABEL_STUB)) {
                viewHolder.textView.setText(R.string.hub_done_label);
            }
            else {
                viewHolder.textView.setText(s);
            }
        }
        else if (getItemViewType(position) == VIEW_TYPE_PROFILE) {
            User user = (User) mHubList.get(position);
            if (user == null) {
                return;
            }

            ProfileViewHolder profileViewHolder = (ProfileViewHolder) holder;

            if (user != null && user.avatarPath != null) {
                Picasso.with(((ProfileViewHolder) holder).userImage.getContext())
                        .load(user.avatarPath).placeholder(R.drawable.ic_action_user)
                        .resizeDimen(R.dimen.hub_avatar_height, R.dimen.hub_avatar_height)
                        .into(profileViewHolder.userImage);
            }

            profileViewHolder.name.setText(formatUserDisplayName(user.first_name, user.last_initial));
            profileViewHolder.userCountry.setText(formatUserLocation(user.country));
            profileViewHolder.userCountry.setAlpha(0.26f);
        }
        else if (getItemViewType(position) == VIEW_TYPE_CURRENT_CAMPAIGN) {
            final Campaign campaign = (Campaign) mHubList.get(position);
            CurrentCampaignViewHolder viewHolder = (CurrentCampaignViewHolder) holder;
            viewHolder.title.setText(campaign.title);
            viewHolder.taglineCaption.setText(campaign.callToAction);

            Resources res = viewHolder.title.getResources();

            if (!mIsPublic && campaign.showShare == Campaign.UploadShare.SHARE) {
                viewHolder.share.setText(res.getString(R.string.share_photo));

                // Clicking on the Share button should share the reportback
                viewHolder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (campaign.showShare == Campaign.UploadShare.SHARE) {
                            mHubAdapterClickListener.onShareClicked(campaign);
                            mClickedCampaign = campaign;
                        }
                    }
                });
            }
            else {
                viewHolder.share.setVisibility(View.GONE);
            }

            boolean hasReportBackImage = false;
            String tmpRbItemId = "";
            if (campaign.userReportBack != null) {
                String count = String.format("%d %s %s", campaign.userReportBack.quantity,
                        campaign.noun, campaign.verb);
                viewHolder.count.setText(count);

                ArrayList<UserReportBack.ReportBackItem> items = campaign.userReportBack.getItems();
                if (items != null && items.size() > 0) {
                    hasReportBackImage = true;

                    // We'll just default to showing the first image in the reportback list
                    String reportBackImage = items.get(0).getImagePath();
                    tmpRbItemId = items.get(0).getId();

                    int height = mContext.getResources().getDimensionPixelSize(R.dimen.campaign_height);
                    Picasso.with(mContext).load(reportBackImage).resize(0, height)
                            .into(viewHolder.reportbackImage);

                    // And use this image's caption
                    viewHolder.taglineCaption.setText(items.get(0).getCaption());
                }
            }

            if (hasReportBackImage) {
                final String rbItemId = tmpRbItemId;
                viewHolder.imageContainer.setVisibility(View.VISIBLE);
                viewHolder.addImage.setVisibility(View.GONE);
                viewHolder.reportbackImage.setVisibility(View.VISIBLE);

                // Clicking the reportback image should go to that reportback's detail screen
                viewHolder.reportbackImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = ReportBackDetailsActivity.getLaunchIntent(mContext,
                                Integer.parseInt(rbItemId), campaign.id);
                        mContext.startActivity(intent);
                    }
                });
            }
            else {
                // Use campaign's call-to-action if no reportback
                viewHolder.taglineCaption.setText(campaign.callToAction);

                if (!mIsPublic) {
                    viewHolder.imageContainer.setVisibility(View.VISIBLE);
                    viewHolder.addImage.setVisibility(View.VISIBLE);
                    viewHolder.reportbackImage.setVisibility(View.GONE);

                    // Clicking the "add image" button should start the reportback flow
                    viewHolder.addImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mHubAdapterClickListener.onProveClicked(campaign);
                            mClickedCampaign = campaign;
                        }
                    });
                }
                else {
                    // Hide the image container if it's a public profile with no reportback
                    viewHolder.imageContainer.setVisibility(View.GONE);
                }
            }

            // Clicking on campaign title should go to the Campaign Details screen
            viewHolder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(CampaignDetailsActivity.getLaunchIntent(mContext, campaign.id));
                }
            });
        }
        else if(getItemViewType(position) == VIEW_TYPE_PAST_CAMPAIGN) {
            Campaign campaign = (Campaign) mHubList.get(position);
            PastCampaignViewHolder pastCampaignViewHolder = (PastCampaignViewHolder) holder;
            Context context = pastCampaignViewHolder.image.getContext();
            int height = context.getResources().getDimensionPixelSize(R.dimen.campaign_height);
            Picasso.with(context).load(campaign.imagePath).resize(0, height)
                    .into(pastCampaignViewHolder.image);
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            pastCampaignViewHolder.image.setColorFilter(new ColorMatrixColorFilter(cm));

            pastCampaignViewHolder.title.setText(campaign.title);
        }
        else if (getItemViewType(position) == VIEW_TYPE_CURRENT_SIGNUPS) {
            ResponseProfileCampaign campaign = (ResponseProfileCampaign) mHubList.get(position);
            CurrentCampaignViewHolder viewHolder = (CurrentCampaignViewHolder) holder;

            viewHolder.title.setText(campaign.title);
        }
        else if (getItemViewType(position) == VIEW_TYPE_REPORTBACKS) {
            final ResponseProfileSignups.Signup action = (ResponseProfileSignups.Signup) mHubList.get(position);
            final ReportBackViewHolder rbViewHolder = (ReportBackViewHolder) holder;
            final Context context = rbViewHolder.itemView.getContext();

            // Use previously received User info
            if (mUser != null) {
                rbViewHolder.name.setText(formatUserDisplayName(mUser.first_name, mUser.last_initial));
                rbViewHolder.location.setText(formatUserLocation(mUser.country));

                if (mUser.avatarPath != null) {
                    Picasso.with(((ProfileViewHolder) holder).userImage.getContext())
                            .load(mUser.avatarPath).placeholder(R.drawable.ic_action_user)
                            .resizeDimen(R.dimen.hub_avatar_height, R.dimen.hub_avatar_height)
                            .into(rbViewHolder.avatar);
                }
            }

            // Report back photo
            int lastImageIndex = action.reportback.reportback_items.total - 1;
            ReportBack rbItem = action.reportback.reportback_items.data[lastImageIndex];
            Picasso.with(context).load(rbItem.getImagePath()).into(rbViewHolder.image);


            // Report back campaign name and details
            final int quantity = action.reportback.quantity;
            final String noun = action.campaign.reportback_info.noun;
            final String verb = action.campaign.reportback_info.verb;
            rbViewHolder.title.setText(action.campaign.title);
            rbViewHolder.caption.setText(rbItem.caption);

            String impactText = String.format("%d %s %s", quantity, noun, verb);
            rbViewHolder.impact.setText(impactText);
        }
        else if (getItemViewType(position) == VIEW_TYPE_CURRENT_EMPTY) {
            EmptyViewHolder viewHolder = (EmptyViewHolder) holder;

            viewHolder.actions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHubAdapterClickListener.onActionsButtonClicked();
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object currentObject = mHubList.get(position);

        if (currentObject instanceof String) {
            if (CURRENT_CAMPAIGNS_EMPTY_STUB.equals(currentObject)) {
                if (mIsPublic) {
                    return VIEW_TYPE_PUBLIC_EMPTY;
                }
                else {
                    return VIEW_TYPE_CURRENT_EMPTY;
                }
            }
            else {
                return VIEW_TYPE_SECTION_TITLE;
            }
        }
        else if (currentObject instanceof User) {
            return VIEW_TYPE_PROFILE;
        }
        else if (currentObject instanceof Campaign) {
            return VIEW_TYPE_CURRENT_CAMPAIGN;
        }
        else if (currentObject instanceof ResponseProfileCampaign) {
            return VIEW_TYPE_CURRENT_SIGNUPS;
        }
        else if (currentObject instanceof ResponseProfileSignups.Signup) {
            return VIEW_TYPE_REPORTBACKS;
        }

        return 0;
    }

    /**
     * Helper function to format the user's location.
     *
     * @param country User country location
     * @return String
     */
    private String formatUserLocation(String country) {
        if (country != null && ! country.isEmpty()) {
            Locale locale = new Locale("", country);
            return locale.getDisplayCountry();
        }

        return "";
    }

    /**
     * Helper function to format the displayed username.
     *
     * @param first User first name
     * @param lastInitial User last initial, if any
     * @return String
     */
    private String formatUserDisplayName(String first, String lastInitial) {
        String last = "";
        if (lastInitial != null && ! lastInitial.isEmpty()) {
            last = lastInitial + ".";
        }

        return String.format("%s %s", first, last).trim();
    }

    /**
     * Sets the user to display.
     *
     * @param user
     */
    public void setUser(User user) {
        mUser = user;

        if (! mHubList.isEmpty() && mHubList.get(0) instanceof User) {
            mHubList.set(0, user);
        }
        else {
            mHubList.add(0, user);
        }

        notifyDataSetChanged();
    }

    /**
     * Sets the current campaigns a user is signed up for to the Hub.
     *
     * @param campaigns Array of campaigns
     */
    public void setCurrentSignups(ArrayList<ResponseProfileCampaign> campaigns) {
        mHubList.addAll(campaigns);
        notifyDataSetChanged();
    }

    /**
     * Sets the user's completed actions to display to the Hub.
     *
     * @param actions A signup with a corresponding reportback
     */
    public void setCompletedActions(ArrayList<ResponseProfileSignups.Signup> actions) {
        // Remove items in the current "actions done" list and replace it with the new ones
        if (mHubList.indexOf(REPORTBACKS_LABEL_STUB) >= 0) {
            for (int i = mHubList.size() - 1; i > mHubList.indexOf(REPORTBACKS_LABEL_STUB); i--) {
                mHubList.remove(i);
            }
        }
        // Add the "actions done" label
        else {
            mHubList.add(REPORTBACKS_LABEL_STUB);
        }

        mHubList.addAll(actions);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mHubList.size();
    }

    public void processingUpload() {
        for (int i = 0; i < mHubList.size(); i++) {
            Object o = mHubList.get(i);
            if (o instanceof Campaign) {
                Campaign campaign = (Campaign) o;
                if (campaign.id == mClickedCampaign.id) {
                    campaign.showShare = Campaign.UploadShare.UPLOADING;
                    notifyItemChanged(i);
                }
            }
        }
    }

    public Campaign getClickedCampaign() {
        return mClickedCampaign;
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        protected ImageView userImage;
        protected TextView  name;
        protected TextView  userCountry;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            this.userImage = (ImageView) itemView.findViewById(R.id.user_image);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.userCountry = (TextView) itemView.findViewById(R.id.user_country);
        }
    }

    public static class CurrentCampaignViewHolder extends RecyclerView.ViewHolder {
        protected final TextView     title;
        protected final TextView     taglineCaption;
        protected final TextView     count;
        protected final View         imageContainer;
        protected final ImageView    addImage;
        protected final ImageView    reportbackImage;
        protected final Button       share;

        public CurrentCampaignViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            taglineCaption = (TextView) itemView.findViewById(R.id.tagline_caption);
            imageContainer = itemView.findViewById(R.id.image_container);
            addImage = (ImageView) itemView.findViewById(R.id.add_image);
            reportbackImage = (ImageView) itemView.findViewById(R.id.reportback_image);
            count = (TextView) itemView.findViewById(R.id.count);
            share = (Button) itemView.findViewById(R.id.prove_share);
        }
    }

    public static class ReportBackViewHolder extends RecyclerView.ViewHolder {
        protected ImageView avatar;
        protected TextView caption;
        protected ImageView image;
        protected TextView impact;
        protected TextView location;
        protected TextView name;
        protected TextView title;

        public ReportBackViewHolder(View itemView) {
            super(itemView);

            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            caption = (TextView) itemView.findViewById(R.id.caption);
            image = (ImageView) itemView.findViewById(R.id.image);
            impact = (TextView) itemView.findViewById(R.id.impact);
            location = (TextView) itemView.findViewById(R.id.location);
            name = (TextView) itemView.findViewById(R.id.name);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    public static class PastCampaignViewHolder extends RecyclerView.ViewHolder {
        protected final ImageView image;
        protected final TextView  title;

        public PastCampaignViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public Button actions;

        public EmptyViewHolder(View itemView) {
            super(itemView);

            actions = (Button) itemView.findViewById(R.id.actions);
        }
    }

    public static class PublicEmptyViewHolder extends RecyclerView.ViewHolder {
        public PublicEmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface HubAdapterClickListener {
        void onShareClicked(Campaign campaign);

        void onProveClicked(Campaign campaign);

        void onActionsButtonClicked();
    }
}
