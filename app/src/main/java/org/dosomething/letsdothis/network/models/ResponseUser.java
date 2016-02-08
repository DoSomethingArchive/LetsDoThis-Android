package org.dosomething.letsdothis.network.models;
import org.dosomething.letsdothis.data.User;

/**
 * @TODO After API changes, the data field here is no longer an array. We should just be able to
 *       consolidate ResponseUser and ResponseUserUpdate.
 * Created by toidiu on 4/16/15.
 */
public class ResponseUser {
    public Wrapper data;

    public static class Wrapper {
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
        return getUser(response.data);
    }

    public static User getUser(Wrapper wrapper) {
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