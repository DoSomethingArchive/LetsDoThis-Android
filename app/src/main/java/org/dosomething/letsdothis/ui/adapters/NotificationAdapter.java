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
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>
{
    private List<Notification> notifications;

    public NotificationAdapter(List<Notification> notifications)
    {
        super();
        this.notifications = notifications;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                                          .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position)
    {
        Notification notification = notifications.get(position);

        Picasso.with(holder.imageView.getContext()).load(notification.imagePath).into(holder.imageView);
        holder.title.setText(notification.title);
        holder.details.setText(notification.details);
        holder.timestamp.setText(TimeUtils.getTimeSince(holder.timestamp.getContext(),notification.timeStamp));
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
