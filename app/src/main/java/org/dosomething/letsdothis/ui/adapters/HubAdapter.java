package org.dosomething.letsdothis.ui.adapters;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.User;

import java.util.ArrayList;
import java.util.List;

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
    public static final String CURRENTLY_DOING            = "currently doing";
    public static final String BEEN_THERE_DONE_GOOD       = "been there, done good";

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private ArrayList<Object> hubList = new ArrayList<>();

    public HubAdapter(User user)
    {
        super();
        this.hubList.add(user);
        hubList.add(CURRENTLY_DOING);
        hubList.add(BEEN_THERE_DONE_GOOD);
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
            profileViewHolder.fName.setText(user.first_name);
            profileViewHolder.lName.setText(user.last_name);
        }
        else if(getItemViewType(position) == VIEW_TYPE_CURRENT_CAMPAIGN)
        {
            Campaign campaign = (Campaign) hubList.get(position);
            CurrentCampaignViewHolder currentCampaignViewHolder = (CurrentCampaignViewHolder) holder;
            currentCampaignViewHolder.title.setText(campaign.title);
            currentCampaignViewHolder.callToAction.setText(campaign.callToAction);
        }
        else if(getItemViewType(position) == VIEW_TYPE_PAST_CAMPAIGN)
        {
            Campaign campaign = (Campaign) hubList.get(position);
            PastCampaignViewHolder pastCampaignViewHolder = (PastCampaignViewHolder) holder;
            //FIXME add image
            pastCampaignViewHolder.title.setText(campaign.title);
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
            return VIEW_TYPE_CURRENT_CAMPAIGN;
        }
        else if(currentObject instanceof Object)//FIXME detect the past campaign
        {
            return VIEW_TYPE_PAST_CAMPAIGN;
        }
        return 0;
    }

    public void addCurrentCampaign(List<Campaign> objects)
    {
        int i = hubList.indexOf(BEEN_THERE_DONE_GOOD);
        hubList.addAll(i, objects);
        notifyItemRangeInserted(hubList.size() - objects.size(), hubList.size() - 1);
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
        protected TextView  fName;
        protected TextView  lName;

        public ProfileViewHolder(View itemView)
        {
            super(itemView);
            this.userImage = (ImageView) itemView.findViewById(R.id.user_image);
            this.fName = (TextView) itemView.findViewById(R.id.user_fname);
            this.lName = (TextView) itemView.findViewById(R.id.user_lname);
        }
    }

    public static class SectionTitleViewHolder extends ProfileViewHolder
    {
        protected final TextView textView;

        public SectionTitleViewHolder(TextView itemView)
        {
            super(itemView);
            textView = itemView;
        }
    }

    public static class CurrentCampaignViewHolder extends RecyclerView.ViewHolder
    {
        protected final TextView title;
        protected final TextView callToAction;

        public CurrentCampaignViewHolder(View itemView)
        {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            callToAction = (TextView) itemView.findViewById(R.id.call_to_action);
        }
    }

    public static class PastCampaignViewHolder extends RecyclerView.ViewHolder
    {
        protected final ImageView campImage;
        protected final TextView  title;

        public PastCampaignViewHolder(View itemView)
        {
            super(itemView);
            campImage = (ImageView) itemView.findViewById(R.id.user_image);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

}
