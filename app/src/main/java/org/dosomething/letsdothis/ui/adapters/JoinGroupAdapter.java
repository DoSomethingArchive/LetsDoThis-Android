package org.dosomething.letsdothis.ui.adapters;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.data.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by izzyoji :) on 7/10/15.
 */

public class JoinGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final int    VIEW_TYPE_FRIEND      = 0;
    public static final int    VIEW_TYPE_CAMPAIGN    = 1;
    public static final int    VIEW_TYPE_JOIN_BUTTON = 2;
    public static final int    VIEW_TYPE_REPORT_BACK = 3;
    public static final String JOIN_PLACEHOLDER      = "join";

    //~=~=~=~=~=~=~=~=~=~=~=~=Members
    private List<Object> data = new ArrayList<>();
    private JoinGroupAdapterListener joinGroupAdapterListener;

    public interface JoinGroupAdapterListener
    {
        void onFriendClicked(String id);

        void joinClicked();

        void closeClicked();

        void onReportBackClicked(int id);
    }

    public JoinGroupAdapter(List<Object> data, JoinGroupAdapterListener joinGroupAdapterListener)
    {
        this.data = data;
        this.joinGroupAdapterListener = joinGroupAdapterListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch(viewType)
        {
            case VIEW_TYPE_FRIEND:
                View friendLayout = LayoutInflater.from(parent.getContext())
                                                  .inflate(R.layout.item_friend, parent, false);
                return new FriendViewHolder((ImageView) friendLayout);
            case VIEW_TYPE_JOIN_BUTTON:
                View primaryButton = LayoutInflater.from(parent.getContext())
                                                   .inflate(R.layout.item_primary_button, parent,
                                                            false);
                return new ButtonViewHolder(primaryButton);
            case VIEW_TYPE_REPORT_BACK:
                View reportBackLayout = LayoutInflater.from(parent.getContext())
                                                      .inflate(R.layout.item_report_back_square,
                                                               parent, false);
                return new ReportBackViewHolder((ImageView) reportBackLayout);
            default:
                View campaignLayout = LayoutInflater.from(parent.getContext())
                                                    .inflate(R.layout.item_group_campaign, parent,
                                                             false);
                return new CampaignViewHolder(campaignLayout);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

        Context context = holder.itemView.getContext();
        if(getItemViewType(position) == VIEW_TYPE_CAMPAIGN)
        {
            final Campaign campaign = (Campaign) data.get(position);
            CampaignViewHolder campaignViewHolder = (CampaignViewHolder) holder;
            campaignViewHolder.title.setText(campaign.title);
            campaignViewHolder.callToAction.setText(campaign.callToAction);

            int height = context.getResources().getDimensionPixelSize(R.dimen.campaign_height);
            Picasso.with(context).load(campaign.imagePath).resize(0, height)
                   .into(campaignViewHolder.imageView);

            campaignViewHolder.close.setVisibility(View.VISIBLE);
            campaignViewHolder.close.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    joinGroupAdapterListener.closeClicked();
                }
            });

        }
        else if(getItemViewType(position) == VIEW_TYPE_JOIN_BUTTON)
        {
            ButtonViewHolder actionButtonsViewHolder = (ButtonViewHolder) holder;

            actionButtonsViewHolder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    joinGroupAdapterListener.joinClicked();
                }
            });
        }
        else if(getItemViewType(position) == VIEW_TYPE_FRIEND)
        {
            final User user = (User) data.get(position);
            FriendViewHolder friendViewHolder = (FriendViewHolder) holder;

            if(! TextUtils.isEmpty(user.avatarPath))
            {
                Picasso.with(friendViewHolder.itemView.getContext()).load(user.avatarPath)
                       .into((ImageView) friendViewHolder.itemView);
            }

            friendViewHolder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    joinGroupAdapterListener.onFriendClicked(user.id);
                }
            });
        }
        else if(getItemViewType(position) == VIEW_TYPE_REPORT_BACK)
        {
            final ReportBack reportBack = (ReportBack) data.get(position);
            ReportBackViewHolder reportBackViewHolder = (ReportBackViewHolder) holder;

            Picasso.with(context).load(reportBack.getImagePath()).into(reportBackViewHolder.root);

            reportBackViewHolder.root.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    joinGroupAdapterListener.onReportBackClicked(reportBack.id);
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        Object currentObject = data.get(position);
        if(currentObject instanceof Campaign)
        {
            return VIEW_TYPE_CAMPAIGN;
        }
        else if(currentObject instanceof String)
        {
            return VIEW_TYPE_JOIN_BUTTON;
        }
        else if(currentObject instanceof ReportBack)
        {
            return VIEW_TYPE_REPORT_BACK;
        }
        else
        {
            return VIEW_TYPE_FRIEND;
        }
    }

    public static class ButtonViewHolder extends RecyclerView.ViewHolder
    {
        public ButtonViewHolder(View primaryButton)
        {
            super(primaryButton);
        }
    }
}
