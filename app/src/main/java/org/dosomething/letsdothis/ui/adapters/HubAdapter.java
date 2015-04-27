package org.dosomething.letsdothis.ui.adapters;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.data.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by izzyoji :) on 4/17/15.
 */
public class HubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final int VIEW_TYPE_PROFILE          = 0;
    public static final int VIEW_TYPE_SECTION_TITLE    = 1;
    public static final int VIEW_TYPE_CURRENT_CAMPAIGN = 2;
    public static final int VIEW_TYPE_PAST_CAMPAIGN    = 3;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private ArrayList<Object> hubList          = new ArrayList<>();
    private int               selectedPosition = - 1;

    public HubAdapter(List<Object> campaigns)
    {
        super();
        this.hubList.addAll(campaigns);
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
            default:
                View smallLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_campaign, parent, false);
                return new ProfileViewHolder(smallLayout);
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

        return VIEW_TYPE_SECTION_TITLE;
    }

    public void addItem(Object o)
    {
        hubList.add(o);
        notifyItemInserted(hubList.size() - 1);
    }

    public void addAll(List<ReportBack> objects)
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
        protected ImageView root;

        public CurrentCampaignViewHolder(ImageView itemView)
        {
            super(itemView);
            this.root = itemView;
        }
    }

    public static class PastCampaignViewHolder extends RecyclerView.ViewHolder
    {
        public PastCampaignViewHolder(TextView itemView)
        {
            super(itemView);
        }
    }

}
