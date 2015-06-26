package org.dosomething.letsdothis.ui.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.dosomething.letsdothis.R;
import org.dosomething.letsdothis.ui.MainActivity;

/**
 * Created by toidiu on 6/25/15.
 */
public class DrawerListAdapter extends ArrayAdapter<String>
{

    public DrawerListAdapter(Context context, String[] list)
    {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.drawer_list_item, parent, false);
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        switch(getItem(position))
        {
            case MainActivity.ACTIONS:
                icon.setImageResource(R.drawable.ic_actions);
                break;
            case MainActivity.HUB:
                icon.setImageResource(R.drawable.ic_hub);
                break;
            case MainActivity.INVITES:
                icon.setImageResource(R.drawable.ic_invites);
                break;
            case MainActivity.NOTIFICATIONS:
                icon.setImageResource(R.drawable.ic_notifications);
                break;
        }

        TextView text = (TextView) convertView.findViewById(R.id.text);
        text.setText(getItem(position));
        return convertView;
    }
}
