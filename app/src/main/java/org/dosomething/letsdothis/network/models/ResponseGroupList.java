package org.dosomething.letsdothis.network.models;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by toidiu.
 */
public class ResponseGroupList
{
    Wrapper data[];

    public static class Wrapper
    {
        String               campaign_id;
        ResponseUser.Wrapper users[];
    }

    public static Map<Integer, Campaign> addUsers(Map<Integer, Campaign> campMap, ResponseGroupList response)
    {
        for(Wrapper r : response.data)
        {
            if(r.users.length > 0)
            {
                for(ResponseUser.Wrapper u : r.users)
                {
                    User user = ResponseUser.getUser(u);
                    int id = Integer.parseInt(r.campaign_id);

                    Campaign campaign = campMap.get(id);
                    campaign.group = new ArrayList<>();
                    campaign.group.add(user);
                }
            }
        }

        return campMap;
    }

}
