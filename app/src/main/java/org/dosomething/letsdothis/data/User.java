package org.dosomething.letsdothis.data;
import com.j256.ormlite.field.DatabaseField;

import org.json.JSONObject;

import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

/**
 * Created by toidiu on 4/16/15.
 */
public class User
{
    @DatabaseField(id = true)
    public String id;
    @DatabaseField
    public Integer drupalId;
    @DatabaseField
    public String email;
    @DatabaseField
    public String mobile;
    @DatabaseField
    public String first_name;
    @DatabaseField
    public String last_name;
    @DatabaseField
    public String birthdate;

    //DON'T STORE PASSWORD IN DATABASE
    public String password;

    @DatabaseField
    public String avatarPath;

    public User()
    {
    }

    public User(String email, String phone, String password)
    {
        this.email = email;
        this.mobile = phone;
        this.password = password;
    }

    public User(String password, String firstName, String lastName, String birthday)
    {
        this.password = password;
        this.first_name = firstName;
        this.last_name = lastName;
        this.birthdate = birthday;
    }

    public static TypedInput getJsonTypedInput(User user) throws Throwable
    {
        JSONObject jsonObject = new JSONObject();
        if(user.first_name != null && ! user.first_name.isEmpty())
        {
            jsonObject.put("first_name", user.first_name);
        }
        if(user.last_name != null && ! user.last_name.isEmpty())
        {
            jsonObject.put("last_name", user.last_name);
        }
        if(user.email != null && ! user.email.isEmpty())
        {
            jsonObject.put("email", user.email);
        }
        if(user.mobile != null && ! user.mobile.isEmpty())
        {
            jsonObject.put("mobile", user.mobile);
        }
        if(user.password != null && ! user.password.isEmpty())
        {
            jsonObject.put("password", user.password);
        }
        if(user.birthdate != null && ! user.birthdate.isEmpty())
        {
            jsonObject.put("birthdate", user.birthdate);
        }

        String json = jsonObject.toString();
        String finalJson = json.replace("\\", "");
        return new TypedByteArray("application/json", finalJson.getBytes("UTF-8"));
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
