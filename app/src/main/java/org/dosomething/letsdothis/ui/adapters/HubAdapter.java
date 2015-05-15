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

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toidiu on 4/17/15.
 */
public class HubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final int    VIEW_TYPE_PLACEHOLDER      = - 1;
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

    public HubAdapter(User user, HubAdapterClickListener hubAdapterClickListener)
    {
        super();
        this.user = user;
        this.hubAdapterClickListener = hubAdapterClickListener;
        this.hubList.add(user);
        hubList.add(CURRENTLY_DOING);
        hubList.add(BEEN_THERE_DONE_GOOD);
        this.hubList.add(0, new PlaceHolder());
        this.isPublic = isPublic;
    }

    public interface HubAdapterClickListener
    {
        void groupClicked(int campaignId, String userId);

        void onProveShareClicked(Campaign campaign);

        void onInviteClicked(Campaign campaign);
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
            case VIEW_TYPE_PLACEHOLDER:
                View placeholderLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_placeholder, parent, false);
                return new PlaceholderViewHolder(placeholderLayout);
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

            profileViewHolder.name
                    .setText(String.format("%s %s.", user.first_name, user.last_name.charAt(0)));
        }
        else if(getItemViewType(position) == VIEW_TYPE_CURRENT_CAMPAIGN)
        {
            final Campaign campaign = (Campaign) hubList.get(position);
            CurrentCampaignViewHolder currentCampaignViewHolder = (CurrentCampaignViewHolder) holder;
            currentCampaignViewHolder.title.setText(campaign.title);
            currentCampaignViewHolder.callToAction.setText(campaign.callToAction);
            currentCampaignViewHolder.count.setText(campaign.count);

            if(isPublic)
            {
                currentCampaignViewHolder.actionButtons.setVisibility(View.GONE);
            }

            Context context = currentCampaignViewHolder.image.getContext();
            int height = context.getResources().getDimensionPixelSize(R.dimen.campaign_height);
            Picasso.with(context).load(campaign.imagePath).resize(0, height)
                    .into(currentCampaignViewHolder.image);

            int size = campaign.group.size();

            currentCampaignViewHolder.proveShare.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    hubAdapterClickListener.onProveShareClicked(campaign);
                }
            });

            currentCampaignViewHolder.invite.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    hubAdapterClickListener.onInviteClicked(campaign);
                }
            });

            if(size > 0)
            {
                int friendSize = context.getResources()
                        .getDimensionPixelSize(R.dimen.friend_avatar);
                currentCampaignViewHolder.friends.setVisibility(View.VISIBLE);
                currentCampaignViewHolder.friendsCount.setText(Integer.toString(size));
                currentCampaignViewHolder.friendsCount.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        hubAdapterClickListener.groupClicked(campaign.id, user.id);
                    }
                });

                for(int i = 0; i < 4; i++)
                {
                    FrameLayout childAt = (FrameLayout) currentCampaignViewHolder.friendsContainer
                            .getChildAt(i);

                    ImageView imageView = (ImageView) childAt.getChildAt(0);

                    if(campaign.group.size() > i)
                    {
                        User friend = campaign.group.get(i);
                        Picasso.with(context).load(friend.avatarPath)
                                .resize(friendSize, 0).into(imageView);
                        childAt.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                hubAdapterClickListener.friendClicked(user.id);
                            }
                        });
                    }
                    else
                    {
                        imageView.setImageDrawable(null);
                    }
                }
            }
            else
            {
                currentCampaignViewHolder.friends.setVisibility(View.GONE);
            }
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

            pastCampaignViewHolder.title.setText(campaign.title);
            pastCampaignViewHolder.image.setColorFilter(new ColorMatrixColorFilter(cm));
        }
        else if(getItemViewType(position) == VIEW_TYPE_EXPIRE)
        {
            ExpireViewHolder expireViewHolder = (ExpireViewHolder) holder;

            Long expire = (Long) hubList.get(position);
            List<String> timeUntilExpiration = TimeUtils.getTimeUntilExpiration(expire);
            String days = timeUntilExpiration.get(0);
            expireViewHolder.days.setText(days);
            String hours = timeUntilExpiration.get(1);
            expireViewHolder.hours.setText(hours);
            String minutes = timeUntilExpiration.get(2);
            expireViewHolder.minutes.setText(minutes);
            Resources resources = expireViewHolder.itemView.getContext().getResources();
            expireViewHolder.daysLabel
                    .setText(resources.getQuantityString(R.plurals.days, Integer.parseInt(days)));
            expireViewHolder.hoursLabel
                    .setText(resources.getQuantityString(R.plurals.hours, Integer.parseInt(hours)));
            expireViewHolder.minutesLabel.setText(
                    resources.getQuantityString(R.plurals.minutes, Integer.parseInt(minutes)));
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
            //FIXME properly get if campaign is past
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
        else if(currentObject instanceof PlaceHolder)
        {
            return VIEW_TYPE_PLACEHOLDER;
        }
        return 0;
    }

    public void addCurrentCampaign(List<Campaign> objects)
    {
        Campaign campaign = objects.get(0);
        setExpirationView(campaign);
        int i = hubList.indexOf(BEEN_THERE_DONE_GOOD);
        hubList.addAll(i, objects);
        notifyItemRangeInserted(hubList.size() - objects.size(), hubList.size() - 1);
    }

    private void setExpirationView(Campaign campaign)
    {
        //        campaign.startTime  //FIXME get real expiration time

        if(!isPublic)
        {
            Long l = TimeUtils.getSampleExpirationTime();
            int i = hubList.indexOf(CURRENTLY_DOING);
            hubList.add(i + 1, l);
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

    public static class ProfileViewHolder extends RecyclerView.ViewHolder
    {
        protected ImageView userImage;
        protected TextView  name;

        public ProfileViewHolder(View itemView)
        {
            super(itemView);
            this.userImage = (ImageView) itemView.findViewById(R.id.user_image);
            this.name = (TextView) itemView.findViewById(R.id.name);
        }
    }

    public static class CurrentCampaignViewHolder extends RecyclerView.ViewHolder
    {
        protected final TextView     title;
        protected final TextView     callToAction;
        protected final TextView     count;
        protected final ImageView    image;
        protected final View         friends;
        protected final TextView     friendsCount;
        protected final LinearLayout friendsContainer;
        protected final Button       proveShare;
        protected final Button       invite;
        protected final View         actionButtons;

        public CurrentCampaignViewHolder(View itemView)
        {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            callToAction = (TextView) itemView.findViewById(R.id.call_to_action);
            image = (ImageView) itemView.findViewById(R.id.image);
            count = (TextView) itemView.findViewById(R.id.count);
            friends = itemView.findViewById(R.id.friends);
            friendsCount = (TextView) itemView.findViewById(R.id.friends_count);
            friendsContainer = (LinearLayout) itemView.findViewById(R.id.friends_container);
            proveShare = (Button) itemView.findViewById(R.id.prove_share);
            invite = (Button) itemView.findViewById(R.id.invite);
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
        protected final TextView days;
        protected final TextView hours;
        protected final TextView minutes;
        protected final TextView daysLabel;
        protected final TextView hoursLabel;
        protected final TextView minutesLabel;

        public ExpireViewHolder(View itemView)
        {
            super(itemView);
            days = (TextView) itemView.findViewById(R.id.days);
            hours = (TextView) itemView.findViewById(R.id.hours);
            minutes = (TextView) itemView.findViewById(R.id.min);
            daysLabel = (TextView) itemView.findViewById(R.id.days_label);
            hoursLabel = (TextView) itemView.findViewById(R.id.hours_label);
            minutesLabel = (TextView) itemView.findViewById(R.id.minutes_label);
        }
    }


    public static class PlaceholderViewHolder extends RecyclerView.ViewHolder
    {
        public PlaceholderViewHolder(View itemView)
        {
            super(itemView);
        }
    }


    public static class PlaceHolder
    {
    }

    public interface HubAdapterClickListener
    {
        void friendClicked(String friendId);

        void groupClicked(int campaignId, String userId);

        void onProveShareClicked(Campaign campaign);
    }
}
