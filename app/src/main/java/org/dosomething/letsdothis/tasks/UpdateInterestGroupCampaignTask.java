package org.dosomething.letsdothis.tasks;
import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseCampaignList;
import org.dosomething.letsdothis.utils.TimeUtils;

import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.BaseTaskQueue;
import co.touchlab.android.threading.tasks.Task;

/**
 * Created by izzyoji :) on 6/23/15.
 */
public class UpdateInterestGroupCampaignTask extends BaseNetworkErrorHandlerTask
{
    public int            interestGroupId;
    public List<Campaign> campaigns;

    public UpdateInterestGroupCampaignTask(int interestGroupId)
    {
        this.interestGroupId = interestGroupId;
    }

    @Override
    protected void run(Context context) throws Throwable
    {
        ResponseCampaignList response = NetworkHelper.getDoSomethingAPIService()
                .campaignList(interestGroupId);
        campaigns = ResponseCampaignList.getCampaigns(response);

        Dao<Campaign, String> campDao = DatabaseHelper.getInstance(context).getCampDao();

        DeleteBuilder db = campDao.deleteBuilder();
        db.where().eq(Campaign.INTEREST_GROUP, interestGroupId);
        campDao.delete(db.prepare());

        for(Campaign c : campaigns)
        {
            c.interestGroup = interestGroupId;
            campDao.createOrUpdate(c);
        }
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        super.handleError(context, throwable);
        return false;
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }

    public static class IdQuery implements BaseTaskQueue.QueueQuery
    {
        final  int     gropuId;
        public boolean found;

        public IdQuery(int gropuId)
        {
            this.gropuId = gropuId;
        }


        @Override
        public void query(BaseTaskQueue queue, Task task)
        {
            if(task instanceof UpdateInterestGroupCampaignTask)
            {
                UpdateInterestGroupCampaignTask groupCampaignTask = (UpdateInterestGroupCampaignTask) task;
                if(groupCampaignTask.interestGroupId == gropuId) found = true;
            }
        }
    }

}
