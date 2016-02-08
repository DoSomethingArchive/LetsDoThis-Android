package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseRegister;
import org.dosomething.letsdothis.utils.AppPrefs;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by toidiu on 4/15/15.
 */
public class RegisterTask extends BaseRegistrationTask {
    private final String SOURCE = "letsdothis_android";

    private final String mEmail;
    private final String mPhone;
    private final String mFirstName;

    public RegisterTask(String email, String phone, String password, String firstName) {
        super(email, password);

        mEmail = email;
        mPhone = phone;
        mFirstName = firstName;
    }

    @Override
    protected void attemptRegistration(Context context, String country) throws Throwable {
        User user = new User(mEmail, mPhone, mPassword);
        user.first_name = mFirstName;
        user.country = country;
        user.source = SOURCE;

        ResponseRegister response = NetworkHelper.getNorthstarAPIService().registerWithEmail(user);
        validateResponse(context, response, user);
    }

    private void validateResponse(Context context, ResponseRegister response, User user) throws Throwable {
        if (response != null) {
            if (response.data.key != null && response.data.user != null
                    && response.data.user.data != null && response.data.user.data.id != null) {
                user.id = response.data.user.data.id;
                user.drupalId = response.data.user.data.drupal_id;
                user.email = response.data.user.data.email;
                user.mobile = response.data.user.data.mobile;
                user.birthdate = response.data.user.data.birthday;
                user.first_name = response.data.user.data.first_name;
                user.last_name = response.data.user.data.last_name;

                AppPrefs appPrefs = AppPrefs.getInstance(context);
                appPrefs.setSessionToken(response.data.key);
                appPrefs.setCurrentEmail(response.data.user.data.email);
                loginUser(context, user);
            }
        }
    }

    @Override
    protected void onComplete(Context context) {
        super.onComplete(context);

        EventBusExt.getDefault().post(this);
    }
}
