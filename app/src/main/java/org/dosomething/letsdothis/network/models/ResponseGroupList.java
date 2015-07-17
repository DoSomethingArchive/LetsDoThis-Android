package org.dosomething.letsdothis.network.models;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.User;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by toidiu.
 */
public class ResponseGroupList
{
    Wrapper data[];

    public static class Wrapper
    {
        String    campaign_id;
        GroupUser users[];
    }

    public static class GroupUser
    {
        String email;
        String mobile;
        String first_name;
        String last_name;
        String _id;
        String birthdate;
        String avatar;
        int    drupal_id;
    }

    public static Map<Integer, Campaign> addUsers(Map<Integer, Campaign> campMap, ResponseGroupList response)
    {
        for(Wrapper r : response.data)
        {
            if(r.users.length > 0)
            {
                for(GroupUser u : r.users)
                {
                    User user = getUser(u);
                    int id = Integer.parseInt(r.campaign_id);

                    Campaign campaign = campMap.get(id);
                    campaign.group = new ArrayList<>();
                    campaign.group.add(user);
                }
            }
        }

        return campMap;
    }

    public static User getUser(GroupUser wrapper)
    {
        User user = new User();
        user.email = wrapper.email;
        user.mobile = wrapper.mobile;
        user.first_name = wrapper.first_name;
        user.last_name = wrapper.last_name;
        user.id = wrapper._id;
        user.birthdate = wrapper.birthdate;
        user.drupalId = wrapper.drupal_id;
        user.avatarPath = wrapper.avatar;
        return user;
    }

}
