package org.dosomething.letsdothis.ui.adapters;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.models.ResponseProfileCampaign;
import org.dosomething.letsdothis.network.models.ResponseProfileSignups;
import org.dosomething.letsdothis.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by toidiu on 4/17/15.
 */
public class HubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    private static final int    VIEW_TYPE_PROFILE          = 0;
    private static final int    VIEW_TYPE_SECTION_TITLE    = 1;
    private static final int    VIEW_TYPE_CURRENT_EMPTY    = 2;
    private static final int    VIEW_TYPE_PUBLIC_EMPTY     = 3;
    private static final int    VIEW_TYPE_CURRENT_SIGNUPS  = 4;
    private static final int    VIEW_TYPE_REPORTBACKS      = 5;

    private static final String CURRENT_SIGNUPS_LABEL_STUB = "PLACEHOLDER: CURRENT_SIGNUPS_LABEL_STUB";
    private static final String CURRENT_CAMPAIGNS_EMPTY_STUB = "PLACEHOLDER: CURRENT_CAMPAIGNS_EMPTY_STUB";
    private static final String REPORTBACKS_LABEL_STUB = "PLACEHOLDER: REPORTBACKS_LABEL_STUB";

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private ArrayList<Object> mHubList = new ArrayList<>();
    private HubAdapterClickListener mHubAdapterClickListener;
    private boolean mIsPublic = false;
    private User mUser;

    // Displayed action counts
    private int mActionsCount = 0;
    private int mPhotosCount = 0;

    public HubAdapter(Context context, HubAdapterClickListener hubAdapterClickListener, boolean isPublic) {
        super();

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
                        .inflate(R.layout.item_hub_signups, parent, false);
                return new SignupViewHolder(v, mHubAdapterClickListener);
            case VIEW_TYPE_REPORTBACKS:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_report_back_expanded, parent, false);
                return new ReportBackViewHolder(v, mHubAdapterClickListener);
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
                if (! mIsPublic) {
                    viewHolder.textView.setText(R.string.hub_current_label);
                }
                else {
                    viewHolder.textView.setText(R.string.hub_current_label_public);
                }
            }
            else if (s.equals(REPORTBACKS_LABEL_STUB)) {
                if (! mIsPublic) {
                    viewHolder.textView.setText(R.string.hub_done_label);
                }
                else {
                    viewHolder.textView.setText(R.string.hub_done_label_public);
                }
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

            profileViewHolder.userCountry.setText(formatUserLocation(user.country));
            profileViewHolder.userCountry.setAlpha(0.26f);

            profileViewHolder.actionsCount.setText(String.valueOf(mActionsCount));
            profileViewHolder.photosCount.setText(String.valueOf(mPhotosCount));
        }
        else if (getItemViewType(position) == VIEW_TYPE_CURRENT_SIGNUPS) {
            ResponseProfileCampaign campaign = (ResponseProfileCampaign) mHubList.get(position);
            SignupViewHolder viewHolder = (SignupViewHolder) holder;

            viewHolder.title.setText(campaign.title);
            viewHolder.tagline.setText(campaign.tagline);

            viewHolder.setCampaignDetails(Integer.parseInt(campaign.id), campaign.title,
                    campaign.reportback_info.noun, campaign.reportback_info.verb);

            if (mIsPublic) {
                viewHolder.proveIt.setVisibility(View.GONE);
            }
        }
        else if (getItemViewType(position) == VIEW_TYPE_REPORTBACKS) {
            final ResponseProfileSignups.Signup action = (ResponseProfileSignups.Signup) mHubList.get(position);
            final ReportBackViewHolder rbViewHolder = (ReportBackViewHolder) holder;
            final Context context = rbViewHolder.itemView.getContext();

            // Use previously received User info
            if (mUser != null) {
                String name = ViewUtils.formatUserDisplayName(context, mUser.first_name, mUser.last_initial);
                rbViewHolder.name.setText(name);
                rbViewHolder.location.setText(formatUserLocation(mUser.country));

                if (mUser.avatarPath != null) {
                    Picasso.with(context)
                            .load(mUser.avatarPath).placeholder(R.drawable.ic_action_user)
                            .resizeDimen(R.dimen.hub_avatar_height, R.dimen.hub_avatar_height)
                            .into(rbViewHolder.avatar);
                }
            }

            // Report back photo
            int lastImageIndex = action.reportback.reportback_items.total - 1;
            ReportBack rbItem = action.reportback.reportback_items.data[lastImageIndex];
            Picasso.with(context).load(rbItem.getImagePath()).into(rbViewHolder.image);
            rbViewHolder.caption.setText(rbItem.caption);

            // Report back campaign name and details
            if (action.reportback != null && action.campaign != null && action.campaign.reportback_info != null) {
                final int quantity = action.reportback.quantity;
                final String noun = action.campaign.reportback_info.noun;
                final String verb = action.campaign.reportback_info.verb;
                rbViewHolder.title.setText(action.campaign.title);

                String impactText = String.format("%d %s %s", quantity, noun, verb);
                rbViewHolder.impact.setText(impactText);
            }

            // Setting data on view holder for use if the Share button is clicked
            rbViewHolder.setAction(action);
            rbViewHolder.setReportbackItemIndex(lastImageIndex);

            if (mIsPublic) {
                rbViewHolder.share.setVisibility(View.GONE);
            }
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
        ArrayList<Object> reportbacks = new ArrayList<>();
        int rbLabelIndex = mHubList.indexOf(REPORTBACKS_LABEL_STUB);
        if (rbLabelIndex > 0) {
            for (int i = rbLabelIndex; i < mHubList.size(); i++) {
                reportbacks.add(mHubList.get(i));
            }
        }

        // Remove any current signups
        int suLabelIndex = mHubList.indexOf(CURRENT_SIGNUPS_LABEL_STUB);
        if (suLabelIndex >= 0) {
            for (int i = mHubList.size() - 1; i > suLabelIndex; i--) {
                mHubList.remove(i);
            }
        }

        if (! mIsPublic && (campaigns == null || campaigns.isEmpty())) {
            // Adds the empty view
            mHubList.add(CURRENT_CAMPAIGNS_EMPTY_STUB);
            mActionsCount = 0;
        }
        else {
            // Adds signups to the list displayed to the Hub
            mHubList.addAll(campaigns);
            mActionsCount = campaigns.size();
        }

        // And then add back any reportbacks, if any
        mHubList.addAll(reportbacks);
        notifyDataSetChanged();
    }

    /**
     * Sets the user's completed actions to display to the Hub.
     *
     * @param actions A signup with a corresponding reportback
     */
    public void setCompletedActions(ArrayList<ResponseProfileSignups.Signup> actions) {
        // Remove items in the current "actions done" list and replace it with the new ones
        int rbLabelIndex = mHubList.indexOf(REPORTBACKS_LABEL_STUB);
        if (rbLabelIndex >= 0) {
            for (int i = mHubList.size() - 1; i >= rbLabelIndex; i--) {
                mHubList.remove(i);
            }
        }

        if (actions != null && ! actions.isEmpty()) {
            // Add the "actions done" label
            mHubList.add(REPORTBACKS_LABEL_STUB);

            // Add the reportbacks
            mHubList.addAll(actions);

            mPhotosCount = actions.size();
        }
        else {
            mPhotosCount = 0;
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mHubList.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        protected ImageView userImage;
        protected TextView  userCountry;
        protected TextView  actionsCount;
        protected TextView  photosCount;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            this.userImage = (ImageView) itemView.findViewById(R.id.user_image);
            this.userCountry = (TextView) itemView.findViewById(R.id.user_country);
            this.actionsCount = (TextView) itemView.findViewById(R.id.actions_count);
            this.photosCount = (TextView) itemView.findViewById(R.id.photos_count);
        }
    }

    /**
     * ViewHolder for current signups without a reportback.
     */
    public static class SignupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected final TextView title;
        protected final TextView tagline;
        protected final Button proveIt;

        // Reference to listener for the Share button
        private HubAdapterClickListener clickHandler;

        // ID for the campaign displayed in this item
        private int campaignId;

        // And additional campaign info for the reportback flow
        private String campaignTitle;
        private String rbNoun;
        private String rbVerb;

        public SignupViewHolder(View itemView, HubAdapterClickListener listener) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            tagline = (TextView) itemView.findViewById(R.id.tagline);
            proveIt = (Button) itemView.findViewById(R.id.prove_it);

            clickHandler = listener;

            title.setOnClickListener(this);
            proveIt.setOnClickListener(this);
        }

        public void setCampaignDetails(int cId, String cTitle, String noun, String verb) {
            campaignId = cId;
            campaignTitle = cTitle;
            rbNoun = noun;
            rbVerb = verb;
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.title:
                    clickHandler.onCampaignClicked(campaignId);
                    break;
                case R.id.prove_it:
                    clickHandler.onProveClicked(campaignId, campaignTitle, rbNoun, rbVerb);
                    break;
            }
        }
    }

    /**
     * ViewHolder for user reportbacks.
     */
    public static class ReportBackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView avatar;
        protected TextView caption;
        protected ImageView image;
        protected TextView impact;
        protected TextView location;
        protected TextView name;
        protected Button share;
        protected TextView title;

        // Reference to listener for the Share button
        private HubAdapterClickListener clickHandler;

        // Data to pass through the listener
        private ResponseProfileSignups.Signup completedAction;
        private int rbItemIndex;

        public ReportBackViewHolder(View itemView, HubAdapterClickListener listener) {
            super(itemView);

            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            caption = (TextView) itemView.findViewById(R.id.caption);
            image = (ImageView) itemView.findViewById(R.id.image);
            impact = (TextView) itemView.findViewById(R.id.impact);
            location = (TextView) itemView.findViewById(R.id.location);
            name = (TextView) itemView.findViewById(R.id.name);
            share = (Button) itemView.findViewById(R.id.share);
            title = (TextView) itemView.findViewById(R.id.title);

            clickHandler = listener;

            share.setVisibility(View.VISIBLE);
            share.setOnClickListener(this);
            title.setOnClickListener(this);
        }

        public void setAction(ResponseProfileSignups.Signup action) {
            completedAction = action;
        }

        public void setReportbackItemIndex(int index) {
            rbItemIndex = index;
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.share:
                    clickHandler.onShareClicked(completedAction, rbItemIndex);
                    break;
                case R.id.title:
                    clickHandler.onCampaignClicked(Integer.parseInt(completedAction.campaign.id));
                    break;
            }
        }
    }

    /**
     * ViewHolder for the empty view shown for the logged in user.
     */
    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public Button actions;

        public EmptyViewHolder(View itemView) {
            super(itemView);

            actions = (Button) itemView.findViewById(R.id.actions);
        }
    }

    /**
     * ViewHolder for the empty view shown for other users.
     */
    public static class PublicEmptyViewHolder extends RecyclerView.ViewHolder {
        public PublicEmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface HubAdapterClickListener {
        /**
         * Handles what happens when a Share button is clicked.
         *
         * @param completedAction Reportback data being shared
         * @param rbItemIndex Index of reportback item being shared
         */
        void onShareClicked(ResponseProfileSignups.Signup completedAction, int rbItemIndex);

        /**
         * Handles click to open a campaign screen.
         *
         * @param campaignId The campaign id
         */
        void onCampaignClicked(int campaignId);

        /**
         * Handles click to start a reportback submission.
         *
         * @param campaignId The campaign id
         * @param campaignTitle The campaign title
         * @param noun The campaign's reportback noun
         * @param verb The campaign's reportback verb
         */
        void onProveClicked(int campaignId, String campaignTitle, String noun, String verb);

        void onActionsButtonClicked();
    }
}
