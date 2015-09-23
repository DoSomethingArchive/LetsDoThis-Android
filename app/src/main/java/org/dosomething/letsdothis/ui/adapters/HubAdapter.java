package org.dosomething.letsdothis.ui.adapters;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by toidiu on 4/17/15.
 */
public class HubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final int    VIEW_TYPE_PROFILE          = 0;
    public static final int    VIEW_TYPE_SECTION_TITLE    = 1;
    public static final int    VIEW_TYPE_CURRENT_CAMPAIGN = 2;
    public static final int    VIEW_TYPE_PAST_CAMPAIGN    = 3;
    public static final int    VIEW_TYPE_EXPIRE           = 4;
    public static final String CURRENTLY_DOING            = "currently doing";
    public static final String BEEN_THERE_DONE_GOOD       = "been there, done good";

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private ArrayList<Object> hubList = new ArrayList<>();
    private User                    user;
    private HubAdapterClickListener hubAdapterClickListener;
    private boolean isPublic = false;
    private Campaign clickedCampaign;

    public HubAdapter(HubAdapterClickListener hubAdapterClickListener, boolean isPublic)
    {
        super();
        this.hubAdapterClickListener = hubAdapterClickListener;
        addUser(new User(null, ""));
        hubList.add(CURRENTLY_DOING);
        hubList.add(BEEN_THERE_DONE_GOOD);
        this.isPublic = isPublic;
    }

    public void addUser(User user)
    {
        this.user = user;
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
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        if(getItemViewType(position) == VIEW_TYPE_SECTION_TITLE)
        {
            String s = (String) hubList.get(position);
            SectionTitleViewHolder sectionTitleViewHolder = (SectionTitleViewHolder) holder;
            sectionTitleViewHolder.textView.setText(s);
        }
        else if(getItemViewType(position) == VIEW_TYPE_PROFILE)
        {
            User user = (User) hubList.get(position);
            ProfileViewHolder profileViewHolder = (ProfileViewHolder) holder;

            if(user != null && user.avatarPath != null)
            {
                Picasso.with(((ProfileViewHolder) holder).userImage.getContext())
                        .load(user.avatarPath).placeholder(R.drawable.ic_action_user)
                        .resizeDimen(R.dimen.hub_avatar_height, R.dimen.hub_avatar_height)
                        .into(profileViewHolder.userImage);
            }


            String first = user.first_name;
            String last = "";
            if(user.last_name != null && user.last_name.length() > 0)
            {
                last = " " + user.last_name.charAt(0) + ".";
            }

            String displayName = String.format("%s%s", first, last);
            profileViewHolder.name.setText(displayName);

            // Convert from country code to display name
            if (user.country != null) {
                Locale locale = new Locale("", user.country);
                profileViewHolder.userCountry.setText(locale.getDisplayCountry());
            }
        }
        else if (getItemViewType(position) == VIEW_TYPE_CURRENT_CAMPAIGN) {
            final Campaign campaign = (Campaign) hubList.get(position);
            CurrentCampaignViewHolder viewHolder = (CurrentCampaignViewHolder) holder;
            viewHolder.title.setText(campaign.title);
            viewHolder.callToAction.setText(campaign.callToAction);
            viewHolder.count.setText(campaign.count);

            Resources res = viewHolder.title.getResources();
            if (isPublic) {
                viewHolder.actionButtons.setVisibility(View.GONE);
            }

            if(campaign.showShare == Campaign.UploadShare.SHARE)
            {
                viewHolder.share.setText(res.getString(R.string.share_photo));
            }
            else {
                viewHolder.share.setVisibility(View.GONE);
            }

            viewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (campaign.showShare == Campaign.UploadShare.SHARE) {
                        hubAdapterClickListener.onShareClicked(campaign);
                        clickedCampaign = campaign;
                    }
                }
            });

            viewHolder.addImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "TODO: start reportback", Toast.LENGTH_LONG).show();
                    hubAdapterClickListener.onProveClicked(campaign);
                    clickedCampaign = campaign;
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
        else if(getItemViewType(position) == VIEW_TYPE_EXPIRE)
        {
            ExpireViewHolder viewHolder = (ExpireViewHolder) holder;

            // @todo Pretty sure I'm breaking whatever expired logic is going on here with the change
            // to using mobile_app.dates.end time for campaign end dates. Will want to fix this when
            // working on Hub stuff.
            Long expire = (Long) hubList.get(position);
            List<String> campExpTime = TimeUtils.getTimeUntilExpiration(expire);
            int dayInt = Integer.parseInt(campExpTime.get(0));

            Resources resources = viewHolder.itemView.getContext().getResources();
            viewHolder.daysLabel.setText(resources.getQuantityString(R.plurals.days, dayInt));

            viewHolder.expire_label.setVisibility(View.VISIBLE);
            viewHolder.expired.setVisibility(View.GONE);
            viewHolder.daysWrapper.setVisibility(View.GONE);
            if (dayInt > 0) {
                viewHolder.daysWrapper.setVisibility(View.VISIBLE);
                viewHolder.days.setText(String.valueOf(dayInt));
            } else {
                viewHolder.expire_label.setVisibility(View.GONE);
                viewHolder.expired.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        Object currentObject = hubList.get(position);
        if(currentObject instanceof String)
        {
            return VIEW_TYPE_SECTION_TITLE;
        }
        else if(currentObject instanceof User)
        {
            return VIEW_TYPE_PROFILE;
        }
        else if(currentObject instanceof Campaign)
        {
            int posOfPastHeader = hubList.indexOf(BEEN_THERE_DONE_GOOD);
            if(position < posOfPastHeader)
            {
                return VIEW_TYPE_CURRENT_CAMPAIGN;
            }
            else
            {
                return VIEW_TYPE_PAST_CAMPAIGN;
            }
        }
        else if(currentObject instanceof Long)
        {
            return VIEW_TYPE_EXPIRE;
        }
        return 0;
    }

    public void addCurrentCampaign(List<Campaign> objects)
    {
        setExpirationView();
        if(hubList.isEmpty())
        {
            setExpirationView();
            int i = hubList.indexOf(BEEN_THERE_DONE_GOOD);
            hubList.addAll(i, objects);
            notifyItemRangeInserted(hubList.size() - objects.size(), hubList.size() - 1);
        }
        else
        {
            for(int i = 0, j = 3; i < objects.size(); i++)
            {
                Object o = hubList.get(j);
                if(o instanceof Campaign)
                {
                    hubList.set(j, objects.get(i));
                    j++;
                }
                else
                {
                    hubList.add(j, objects.get(i));
                    j++;
                }
            }

            notifyItemRangeChanged(2, objects.size());
        }
    }

    private void setExpirationView()
    {
        if(! isPublic)
        {
            Long expire = TimeUtils.getStartOfNextMonth();
            int i = hubList.indexOf(CURRENTLY_DOING);
            hubList.add(i + 1, expire);
        }
    }

    public void addPastCampaign(List<Campaign> objects)
    {
        hubList.addAll(objects);
        notifyItemRangeInserted(hubList.size() - objects.size(), hubList.size() - 1);
    }

    @Override
    public int getItemCount()
    {
        return hubList.size();
    }

    public void processingUpload()
    {
        for(int i = 0; i < hubList.size(); i++)
        {
            Object o = hubList.get(i);
            if(o instanceof Campaign)
            {
                Campaign campaign = (Campaign) o;
                if(campaign.id == clickedCampaign.id)
                {
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

    public static class ProfileViewHolder extends RecyclerView.ViewHolder
    {
        protected ImageView userImage;
        protected TextView  name;
        protected TextView  userCountry;

        public ProfileViewHolder(View itemView)
        {
            super(itemView);
            this.userImage = (ImageView) itemView.findViewById(R.id.user_image);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.userCountry = (TextView) itemView.findViewById(R.id.user_country);
        }
    }

    public static class CurrentCampaignViewHolder extends RecyclerView.ViewHolder
    {
        protected final TextView     title;
        protected final TextView     callToAction;
        protected final TextView     count;
        protected final ImageView    addImage;
        protected final ImageView    reportbackImage;
        protected final Button       share;
        protected final View         actionButtons;

        public CurrentCampaignViewHolder(View itemView)
        {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            callToAction = (TextView) itemView.findViewById(R.id.call_to_action);
            addImage = (ImageView) itemView.findViewById(R.id.add_image);
            reportbackImage = (ImageView) itemView.findViewById(R.id.reportback_image);
            count = (TextView) itemView.findViewById(R.id.count);
            share = (Button) itemView.findViewById(R.id.prove_share);
            actionButtons = itemView.findViewById(R.id.action_buttons);
        }
    }

    public static class PastCampaignViewHolder extends RecyclerView.ViewHolder
    {
        protected final ImageView image;
        protected final TextView  title;

        public PastCampaignViewHolder(View itemView)
        {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    public static class ExpireViewHolder extends RecyclerView.ViewHolder
    {
        public          TextView expired;
        public          TextView expire_label;
        protected final TextView days;
        protected final TextView daysLabel;
        protected final View     daysWrapper;

        public ExpireViewHolder(View itemView)
        {
            super(itemView);
            expire_label = (TextView) itemView.findViewById(R.id.expire_label);
            expired = (TextView) itemView.findViewById(R.id.expired_already);
            daysWrapper = itemView.findViewById(R.id.days_wrapper);
            days = (TextView) itemView.findViewById(R.id.days);
            daysLabel = (TextView) itemView.findViewById(R.id.days_label);
        }
    }

    public interface HubAdapterClickListener
    {
        void friendClicked(String friendId);

        void groupClicked(int groupId);

        void onShareClicked(Campaign campaign);

        void onProveClicked(Campaign campaign);

        void onInviteClicked(String title, int signupGroup);
    }
}
