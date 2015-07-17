package org.dosomething.letsdothis.tasks;
import android.content.Context;

import com.j256.ormlite.dao.Dao;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.DatabaseHelper;

import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;

/**
 * Created by izzyoji :) on 6/23/15.
 */
public class DbInterestGroupCampaignListTask extends Task
{
    public List<Campaign> campList;
    public int            interestGroupId;

    public DbInterestGroupCampaignListTask(int interestGroupId)
    {
        this.interestGroupId = interestGroupId;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        Dao<Campaign, String> dao = DatabaseHelper.getInstance(context).getCampDao();
        campList = dao.queryBuilder().where().eq(Campaign.INTEREST_GROUP, interestGroupId).query();
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        return false;
    }

    @Override
    protected void onComplete(Context context)
    {
        super.onComplete(context);
        EventBusExt.getDefault().post(this);
    }

}
