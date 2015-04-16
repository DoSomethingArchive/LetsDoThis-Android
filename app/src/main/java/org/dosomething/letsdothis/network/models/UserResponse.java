package org.dosomething.letsdothis.network.models;
import org.dosomething.letsdothis.data.User;

/**
 * Created by toidiu on 4/16/15.
 */
public class UserResponse
{

    String   email;
    String   first_name;
    String   last_name;
    String   _id;

    public static User getUser(UserResponse response)
    {
        User user = new User();
        user.email=response.email;
        user.first_name=response.first_name;
        user.last_name=response.last_name;
        user.id=response._id;
        return user;
    }
    //    {
    //        email: "test@dosomething.org",
    //        first_name: First,
    //        last_name: Last,
    //        drupal_id: 123456,
    //        _id: some sort of hash value,
    //        campaigns: [
    //            {
    //                nid: 123,
    //                        rbid: 100,
    //                    sid: 100
    //            },
    //            {
    //                nid: 456,
    //                        sid: 101
    //            }
    //            ]
    //    }
}
