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
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.data.UserReportBack;
import org.dosomething.letsdothis.ui.CampaignDetailsActivity;
import org.dosomething.letsdothis.ui.ReportBackDetailsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by toidiu on 4/17/15.
 */
public class HubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    private static final int    VIEW_TYPE_PROFILE          = 0;
    private static final int    VIEW_TYPE_SECTION_TITLE    = 1;
    private static final int    VIEW_TYPE_CURRENT_CAMPAIGN = 2;
    private static final int    VIEW_TYPE_PAST_CAMPAIGN    = 3;
    private static final int    VIEW_TYPE_EXPIRE           = 4;
    private static final int    VIEW_TYPE_CURRENT_EMPTY    = 5;
    private static final int    VIEW_TYPE_PUBLIC_EMPTY     = 6;

    private static final String BEEN_THERE_DONE_GOOD = "been there, done good";
    private static final String CURRENT_EXPIRES_LABEL_STUB = "PLACEHOLDER: CURRENT_EXPIRES_LABEL_STUB";
    private static final String CURRENT_CAMPAIGNS_EMPTY_STUB = "PLACEHOLDER: CURRENT_CAMPAIGNS_EMPTY_STUB";

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private ArrayList<Object> hubList = new ArrayList<>();
    private User                    mUser;
    private HubAdapterClickListener hubAdapterClickListener;
    private boolean isPublic = false;
    private Campaign clickedCampaign;
    private Context mContext;

    public HubAdapter(Context context, HubAdapterClickListener hubAdapterClickListener, boolean isPublic)
    {
        super();
        this.mContext = context;
        this.hubAdapterClickListener = hubAdapterClickListener;
        addUser(new User(null, ""));
        hubList.add(CURRENT_EXPIRES_LABEL_STUB);
        this.isPublic = isPublic;
    }

    public void addUser(User user)
    {
        this.mUser = user;
        if(! hubList.isEmpty() && hubList.get(0) instanceof User)
        {
            hubList.set(0, user);
        }
        else
        {
            hubList.add(0, user);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch(viewType)
        {
            case VIEW_TYPE_SECTION_TITLE:
                View sectionLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_campaign_footer, parent, false);
                return new SectionTitleViewHolder((TextView) sectionLayout);
            case VIEW_TYPE_PROFILE:
                View profileLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_hub_profile, parent, false);
                return new ProfileViewHolder(profileLayout);
            case VIEW_TYPE_CURRENT_CAMPAIGN:
                View currentLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_hub_current_campaign, parent, false);
                return new CurrentCampaignViewHolder(currentLayout);
            case VIEW_TYPE_PAST_CAMPAIGN:
                View pastLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_hub_past_campaign, parent, false);
                return new PastCampaignViewHolder(pastLayout);
            case VIEW_TYPE_EXPIRE:
                View expireLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_hub_expire, parent, false);
                return new ExpireViewHolder(expireLayout);
            case VIEW_TYPE_CURRENT_EMPTY:
                View emptyLayout = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_hub_current_empty, parent, false);
                return new EmptyViewHolder(emptyLayout);
            case VIEW_TYPE_PUBLIC_EMPTY:
                View publicEmptyLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_hub_public_empty, parent, false);
                return new PublicEmptyViewHolder(publicEmptyLayout);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == VIEW_TYPE_SECTION_TITLE) {
            String s = (String) hubList.get(position);
            SectionTitleViewHolder sectionTitleViewHolder = (SectionTitleViewHolder) holder;
            sectionTitleViewHolder.textView.setText(s);
        }
        else if (getItemViewType(position) == VIEW_TYPE_PROFILE) {
            ProfileViewHolder profileViewHolder = (ProfileViewHolder) holder;

            if (mUser != null && mUser.avatarPath != null) {
                Picasso.with(((ProfileViewHolder) holder).userImage.getContext())
                        .load(mUser.avatarPath).placeholder(R.drawable.ic_action_user)
                        .resizeDimen(R.dimen.hub_avatar_height, R.dimen.hub_avatar_height)
                        .into(profileViewHolder.userImage);
            }


            String first = mUser.first_name;
            String last = "";
            if (mUser.last_name != null && mUser.last_name.length() > 0) {
                last = " " + mUser.last_name.charAt(0) + ".";
            }

            String displayName = String.format("%s%s", first, last);
            profileViewHolder.name.setText(displayName);

            // Convert from country code to display name
            if (mUser.country != null) {
                Locale locale = new Locale("", mUser.country);
                profileViewHolder.userCountry.setText(locale.getDisplayCountry());
            }
        }
        else if (getItemViewType(position) == VIEW_TYPE_CURRENT_CAMPAIGN) {
            final Campaign campaign = (Campaign) hubList.get(position);
            CurrentCampaignViewHolder viewHolder = (CurrentCampaignViewHolder) holder;
            viewHolder.title.setText(campaign.title);
            viewHolder.taglineCaption.setText(campaign.callToAction);

            Resources res = viewHolder.title.getResources();

            if (!isPublic && campaign.showShare == Campaign.UploadShare.SHARE) {
                viewHolder.share.setText(res.getString(R.string.share_photo));

                // Clicking on the Share button should share the reportback
                viewHolder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (campaign.showShare == Campaign.UploadShare.SHARE) {
                            hubAdapterClickListener.onShareClicked(campaign);
                            clickedCampaign = campaign;
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

                if (!isPublic) {
                    viewHolder.imageContainer.setVisibility(View.VISIBLE);
                    viewHolder.addImage.setVisibility(View.VISIBLE);
                    viewHolder.reportbackImage.setVisibility(View.GONE);

                    // Clicking the "add image" button should start the reportback flow
                    viewHolder.addImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hubAdapterClickListener.onProveClicked(campaign);
                            clickedCampaign = campaign;
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
        else if(getItemViewType(position) == VIEW_TYPE_PAST_CAMPAIGN)
        {
            Campaign campaign = (Campaign) hubList.get(position);
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
        // This is the static label above the list of current campaigns
        else if(getItemViewType(position) == VIEW_TYPE_EXPIRE) {
            // @todo Nothing needs to happen here. Should clean this up.
        }
        else if (getItemViewType(position) == VIEW_TYPE_CURRENT_EMPTY) {
            EmptyViewHolder viewHolder = (EmptyViewHolder) holder;

            viewHolder.actions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hubAdapterClickListener.onActionsButtonClicked();
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object currentObject = hubList.get(position);

        if (currentObject instanceof String) {
            if (CURRENT_CAMPAIGNS_EMPTY_STUB.equals(currentObject)) {
                if (isPublic) {
                    return VIEW_TYPE_PUBLIC_EMPTY;
                }
                else {
                    return VIEW_TYPE_CURRENT_EMPTY;
                }
            }
            else if (CURRENT_EXPIRES_LABEL_STUB.equals(currentObject)) {
                return VIEW_TYPE_EXPIRE;
            }
            else {
                return VIEW_TYPE_SECTION_TITLE;
            }
        }
        else if (currentObject instanceof User) {
            return VIEW_TYPE_PROFILE;
        }
        else if (currentObject instanceof Campaign) {
            int posOfPastHeader = hubList.indexOf(BEEN_THERE_DONE_GOOD);

            if (posOfPastHeader != -1 && position >= posOfPastHeader) {
                return VIEW_TYPE_PAST_CAMPAIGN;
            }
            else {
                return VIEW_TYPE_CURRENT_CAMPAIGN;
            }
        }

        return 0;
    }

    /**
     * Set the campaigns a user is currently doing.
     *
     * @param objects Campaigns the user is doing. Could be empty or null.
     */
    public void setCurrentCampaign(List<Campaign> objects) {
        int indexCurrentLabel = hubList.indexOf(CURRENT_EXPIRES_LABEL_STUB);

        if (objects != null && objects.size() > 0) {
            // Remove empty stub if it's there
            hubList.remove(CURRENT_CAMPAIGNS_EMPTY_STUB);

            // Remove anything between the past campaigns label (or the end of the list if there are
            // no past campaigns) and the current campaigns label
            int indexRemoveTo = hubList.indexOf(BEEN_THERE_DONE_GOOD);
            if (indexRemoveTo < 0) {
                indexRemoveTo = hubList.size() - 1;
            }
            if (indexRemoveTo > indexCurrentLabel + 1) {
                for (int i = indexRemoveTo; i > indexCurrentLabel; i--) {
                    hubList.remove(i);
                }
            }

            // Add the campaigns. + 1 to start after the "expires" label
            int insertIndex = indexCurrentLabel + 1;
            for (int i = 0; i < objects.size(); i++) {
                hubList.add(insertIndex, objects.get(i));
                insertIndex++;
            }
        }
        else {
            // Add on the empty stub if it's not already there
            if (hubList.indexOf(CURRENT_CAMPAIGNS_EMPTY_STUB) == -1) {
                // + 1 to add after the expires label
                hubList.add(indexCurrentLabel + 1, CURRENT_CAMPAIGNS_EMPTY_STUB);
            }
        }

        notifyDataSetChanged();
    }

    public void addPastCampaign(List<Campaign> objects) {
        if (hubList.indexOf(BEEN_THERE_DONE_GOOD) == -1) {
            hubList.add(BEEN_THERE_DONE_GOOD);
        }

        hubList.addAll(objects);
        notifyItemRangeInserted(hubList.size() - objects.size() - 1, hubList.size() - 1);
    }

    @Override
    public int getItemCount()
    {
        return hubList.size();
    }

    public void processingUpload() {
        for (int i = 0; i < hubList.size(); i++) {
            Object o = hubList.get(i);
            if (o instanceof Campaign) {
                Campaign campaign = (Campaign) o;
                if (campaign.id == clickedCampaign.id) {
                    campaign.showShare = Campaign.UploadShare.UPLOADING;
                    notifyItemChanged(i);
                }
            }
        }
    }

    public Campaign getClickedCampaign()
    {
        return clickedCampaign;
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

    public static class PastCampaignViewHolder extends RecyclerView.ViewHolder {
        protected final ImageView image;
        protected final TextView  title;

        public PastCampaignViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    public static class ExpireViewHolder extends RecyclerView.ViewHolder {
        public ExpireViewHolder(View itemView) {
            super(itemView);
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
