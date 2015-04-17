package org.dosomething.letsdothis.data;
import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by toidiu on 4/16/15.
 */
public class User
{
    @DatabaseField(id = true)
    public String id;
    @DatabaseField
    public String email;
    @DatabaseField
    public String mobile;
    @DatabaseField
    public String first_name;
    @DatabaseField
    public String last_name;




    public String password;

    public User()
    {
    }

    public User(String email, String phone, String password)
    {
        this.email = email;
        this.mobile = phone;
        this.password = password;
    }

    public static String getJson(User user)
    {
        return new Gson().toJson(user, User.class);
    }


  /* Optional */
    //    birthdate: Date,
    //    addr_street1 : String
    //    addr_street2 : String
    //    addr_city : String
    //    addr_state : String
    //    addr_zip : String
    //    country : String - two character country code
    //    agg_id: Int
    //    cgg_id: Int
    //    drupal_id: Int
    //    race: String
    //    religion: String
    //    college_name: String
    //    degree_type: String
    //    major_name: String
    //    hs_gradyear: String
    //    hs_name: String
    //    sat_math: Int
    //    sat_verbal: Int
    //    sat_writing: Int
}
