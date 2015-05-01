package org.dosomething.letsdothis.ui.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.dosomething.letsdothis.R;

/**
 * Created by toidiu on 4/30/15.
 */
public class NotificationListAdapter extends ArrayAdapter
{
    public NotificationListAdapter(Context context, int resource)
    {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        return inflater.inflate(R.layout.item_notification, parent, false);
    }
}
