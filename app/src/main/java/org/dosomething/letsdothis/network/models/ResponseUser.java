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
        String avatar;
        int    drupal_id;
        public ResponseUserCampaign.Wrapper[] campaigns;
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
        user.drupalId = response.data[0].drupal_id;
        user.avatarPath = response.data[0].avatar;
        return user;
    }


    public static User getUser(Wrapper wrapper)
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