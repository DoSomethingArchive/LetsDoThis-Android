package org.dosomething.letsdothis.ui.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.dosomething.letsdothis.R;

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
        switch(position)
        {
            case 0:
                icon.setImageResource(R.drawable.ic_actions);
                break;
            case 1:
                icon.setImageResource(R.drawable.ic_hub);
                break;
            case 2:
                icon.setImageResource(R.drawable.ic_invites);
                break;
            case 3:
                icon.setImageResource(R.drawable.ic_notifications);
                break;
        }

        TextView text = (TextView) convertView.findViewById(R.id.text);
        text.setText(getItem(position));
        return convertView;
    }
}
