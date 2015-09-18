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
    @DatabaseField
    public String avatarPath;
    @DatabaseField
    public String country;

    // Fields not saved in the database
    public String password;
    public String source;

    public User()
    {
    }

    public User(String email, String phone, String password)
    {
        this.email = email;
        this.mobile = phone;
        this.password = password;
    }

    public User(String password, String firstName)
    {
        this.password = password;
        this.first_name = firstName;
    }

    public static TypedInput getJsonTypedInput(User user) throws Throwable
    {
        JSONObject jsonObject = new JSONObject();
        if (user.first_name != null && !user.first_name.isEmpty()) {
            jsonObject.put("first_name", user.first_name);
        }
        if (user.last_name != null && !user.last_name.isEmpty()) {
            jsonObject.put("last_name", user.last_name);
        }
        if (user.email != null && !user.email.isEmpty()) {
            jsonObject.put("email", user.email);
        }
        if (user.mobile != null && !user.mobile.isEmpty()) {
            jsonObject.put("mobile", user.mobile);
        }
        if (user.password != null && !user.password.isEmpty()) {
            jsonObject.put("password", user.password);
        }
        if (user.birthdate != null && !user.birthdate.isEmpty()) {
            jsonObject.put("birthdate", user.birthdate);
        }
        if (user.country != null && !user.country.isEmpty()) {
            jsonObject.put("country", user.country);
        }

        String json = jsonObject.toString();
        String finalJson = json.replace("\\", "");
        return new TypedByteArray("application/json", finalJson.getBytes("UTF-8"));
    }
}
