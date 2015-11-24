package org.dosomething.letsdothis.data;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by izzyoji :) on 6/29/15.
 */
public class CampaignActions
{

    @DatabaseField(id = true)
    public int campaignId;

    @DatabaseField
    public int signUpId;

    @DatabaseField
    public int reportBackId;

    public static CampaignActions queryForId(Context context, int campaignId) throws SQLException
    {
        return getDao(context).queryForId(campaignId);
    }

    private static Dao<CampaignActions, Integer> getDao(Context context) throws SQLException
    {
        return DatabaseHelper.getInstance(context).getDao(CampaignActions.class);
    }

    public static void save(Context context, CampaignActions campaignActions) throws SQLException
    {
        getDao(context).createOrUpdate(campaignActions);
    }

    public static void clear(Context context) {
        try {
            List<CampaignActions> actions = getDao(context).queryForAll();
            ArrayList<CampaignActions> arrActions = new ArrayList<>();
            arrActions.addAll(actions);
            for (int i = 0 ; i < arrActions.size(); i++) {
                getDao(context).deleteById(arrActions.get(i).campaignId);
            }
        }
        catch (Exception e) {
            Log.e("CampaignActionsDAO", "Failed to clear campaign actions. May see inconsistent campaign activity behavior.");
        }

    }
}
