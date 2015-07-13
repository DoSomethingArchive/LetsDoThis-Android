package org.dosomething.letsdothis.data;
import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

import java.sql.SQLException;

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
}
