package org.dosomething.letsdothis.network.models;
import org.dosomething.letsdothis.data.User;

/**
 * Created by toidiu on 4/16/15.
 */
public class ResponseUser
{

    public Wrapper data;

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
        user.email = response.data.email;
        user.mobile = response.data.mobile;
        user.first_name = response.data.first_name;
        user.last_name = response.data.last_name;
        user.id = response.data._id;
        user.birthdate = response.data.birthdate;
        return user;
    }

}