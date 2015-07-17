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
        int                  campaign_id;
        int                  signup_group;
        ResponseUser.Wrapper users[];
    }

    public static Map<Integer, Campaign> addUsers(Map<Integer, Campaign> campMap, ResponseGroupList response)
    {
        for(Wrapper r : response.data)
        {
            Campaign campaign = campMap.get(r.campaign_id);
            campaign.signupGroup = r.signup_group;

            if(r.users.length > 0)
            {
                for(ResponseUser.Wrapper u : r.users)
                {
                    User user = ResponseUser.getUser(u);


                    campaign.group = new ArrayList<>();
                    campaign.group.add(user);
                }
            }
        }

        return campMap;
    }

}
