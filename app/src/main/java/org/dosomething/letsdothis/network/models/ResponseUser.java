package org.dosomething.letsdothis.network.models;
import org.dosomething.letsdothis.data.User;

/**
 * Created by toidiu on 4/16/15.
 */
public class ResponseUser
{

    public Wrapper data[];

    public static class Wrapper
    {
        String email;
        String mobile;
        String first_name;
        String last_name;
        String _id;
        String birthdate;
    }

    public static User getUser(ResponseUser response)
    {
        User user = new User();
        user.email = response.data[0].email;
        user.mobile = response.data[0].mobile;
        user.first_name = response.data[0].first_name;
        user.last_name = response.data[0].last_name;
        user.id = response.data[0]._id;
        user.birthdate = response.data[0].birthdate;
        return user;
    }

}