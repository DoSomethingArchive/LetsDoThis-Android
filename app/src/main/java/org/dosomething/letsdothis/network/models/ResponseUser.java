package org.dosomething.letsdothis.network.models;
import org.dosomething.letsdothis.data.User;

/**
 * Created by toidiu on 4/16/15.
 */
public class ResponseUser
{

    String email;
    String mobile;
    String first_name;
    String last_name;
    String _id;
    String birthdate;

    public static User getUser(ResponseUser response)
    {
        User user = new User();
        user.email = response.email;
        user.mobile = response.mobile;
        user.first_name = response.first_name;
        user.last_name = response.last_name;
        user.id = response._id;
        user.birthdate = response.birthdate;
        return user;
    }

}
