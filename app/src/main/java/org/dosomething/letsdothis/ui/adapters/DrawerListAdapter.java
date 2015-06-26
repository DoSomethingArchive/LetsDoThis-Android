package org.dosomething.letsdothis.ui.adapters;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

        Resources resources = convertView.getResources();
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);

        String positionString = getItem(position);
        if(TextUtils.equals(positionString, resources.getString(R.string.actions)))
        {
            icon.setImageResource(R.drawable.ic_actions);
        }
        else if(TextUtils.equals(positionString, resources.getString(R.string.hub)))
        {
            icon.setImageResource(R.drawable.ic_hub);
        }
        else if(TextUtils.equals(positionString, resources.getString(R.string.invites)))
        {
            icon.setImageResource(R.drawable.ic_invites);
        }
        else if(TextUtils.equals(positionString, resources.getString(R.string.notifications)))
        {
            icon.setImageResource(R.drawable.ic_notifications);
        }

        TextView text = (TextView) convertView.findViewById(R.id.text);
        text.setText(positionString);
        return convertView;
    }
}
