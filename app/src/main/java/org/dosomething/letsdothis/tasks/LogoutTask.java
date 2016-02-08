package org.dosomething.letsdothis.tasks;
import android.content.Context;

import com.facebook.login.LoginManager;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.CampaignActions;
import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.util.ArrayList;
import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;

/**
 * Created by izzyoji :) on 4/30/15.
 */
public class LogoutTask extends BaseNetworkErrorHandlerTask {

    @Override
    protected void onComplete(Context context) {
        super.onComplete(context);

        EventBusExt.getDefault().post(this);
    }

    @Override
    protected void run(Context context) throws Throwable {
        // Facebook logout
        LoginManager.getInstance().logOut();

        AppPrefs prefs = AppPrefs.getInstance(context);
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);

        // Clear user data from the database
        dbHelper.getUserDao().deleteById(prefs.getCurrentUserId());

        // Clear campaign data from the DB
        List<Campaign> listCampaigns = dbHelper.getCampDao().queryForAll();
        ArrayList<Campaign> arrCampaigns = new ArrayList<>();
        arrCampaigns.addAll(listCampaigns);
        for (int i = 0; i < arrCampaigns.size(); i++) {
            Campaign campaign = arrCampaigns.get(i);
            dbHelper.getCampDao().deleteById(Integer.valueOf(campaign.id));
        }

        // Clear campaign actions from the database
        CampaignActions.clear(context);

        // Get current token to logout with
        String sessionToken = prefs.getSessionToken();

        // Clear SharedPreferences cache of the token and user data
        prefs.logout();

        // Logout from API
        NetworkHelper.getNorthstarAPIService().logout(sessionToken, "");
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable) {
        return true;
    }
}
