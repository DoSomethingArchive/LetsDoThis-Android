package org.dosomething.letsdothis.network.models;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.User;

import java.util.List;

/**
 * Created by toidiu.
 */
public class ResponseGroupList
{
    Wrapper data[];

    public static class Wrapper
    {
        ResponseUser.Wrapper users[];
    }

    public static List<Campaign> addUsers(List<Campaign> campaigns, ResponseGroupList response)
    {
        for(Wrapper r : response.data)
        {
            if(r.users.length > 0)
            {

                for(ResponseUser.Wrapper u : r.users)
                {
                    User user = ResponseUser.getUser(u);
                }
            }

        }
    }

}
