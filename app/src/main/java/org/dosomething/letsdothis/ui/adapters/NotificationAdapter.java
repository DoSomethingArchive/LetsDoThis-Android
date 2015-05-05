package org.dosomething.letsdothis.ui.adapters;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.data.Notification;
import org.dosomething.letsdothis.utils.TimeUtils;

import java.util.List;

/**
 * Created by toidiu on 4/30/15.
 */
public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    //~=~=~=~=~=~=~=~=~=~=~=~=Constants
    public static final int VIEW_TYPE_NOTIFICAITON = 0;

    //~=~=~=~=~=~=~=~=~=~=~=~=Fields
    private List<Object> notifications;

    public NotificationAdapter(List<Object> notifications)
    {
        super();
        this.notifications = notifications;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch(viewType)
        {
            case VIEW_TYPE_NOTIFICAITON:
                View notificationLayout = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_notification, parent, false);
                return new NotificationViewHolder(notificationLayout);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
    {
        if(getItemViewType(position) == VIEW_TYPE_NOTIFICAITON)
        {
            NotificationViewHolder notificationViewHolder = (NotificationViewHolder) holder;
            Notification notification = (Notification) notifications.get(position);

            Picasso.with(notificationViewHolder.imageView.getContext()).load(notification.imagePath)
                    .placeholder(R.drawable.user_image).into(notificationViewHolder.imageView);
            notificationViewHolder.title.setText(notification.title);
            notificationViewHolder.details.setText(notification.details);
            notificationViewHolder.timestamp.setText(TimeUtils.getTimeSince(
                    notificationViewHolder.timestamp.getContext(), notification.timeStamp));
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        Object currentObject = notifications.get(position);
        if(currentObject instanceof Notification)
        {
            return VIEW_TYPE_NOTIFICAITON;
        }
        return 0;
    }

    @Override
    public int getItemCount()
    {
        return notifications.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder
    {
        protected ImageView imageView;
        protected TextView  title;
        protected TextView  details;
        protected TextView  timestamp;

        public NotificationViewHolder(View itemView)
        {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.image);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.details = (TextView) itemView.findViewById(R.id.details);
            this.timestamp = (TextView) itemView.findViewById(R.id.timestamp);
        }
    }

}
