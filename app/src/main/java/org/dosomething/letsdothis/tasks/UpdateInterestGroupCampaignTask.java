package org.dosomething.letsdothis.tasks;
import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.DatabaseHelper;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseCampaignList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.BaseTaskQueue;
import co.touchlab.android.threading.tasks.Task;

/**
 * Created by izzyoji :) on 6/23/15.
 */
public class UpdateInterestGroupCampaignTask extends BaseNetworkErrorHandlerTask
{
    // Interest group id
    public int interestGroupId;

    // Resulting campaigns
    public List<Campaign> campaigns;

    // True if an error was encountered during the task
    private boolean mHasError;

    public UpdateInterestGroupCampaignTask(int interestGroupId) {
        this.interestGroupId = interestGroupId;
        this.mHasError = false;
    }

    @Override
    protected void run(Context context) throws Throwable {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = df.format(new Date());
        ResponseCampaignList response = NetworkHelper.getPhoenixAPIService()
                .campaignList(interestGroupId, currentDate);
        campaigns = ResponseCampaignList.getCampaigns(response);

        Dao<Campaign, Integer> campDao = DatabaseHelper.getInstance(context).getCampDao();

        DeleteBuilder db = campDao.deleteBuilder();
        db.where().eq(Campaign.INTEREST_GROUP, interestGroupId);
        campDao.delete(db.prepare());

        for(Campaign c : campaigns) {
            c.interestGroup = interestGroupId;
            campDao.createOrUpdate(c);
        }
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable) {
        super.handleError(context, throwable);
        mHasError = true;

        return true;
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }

    public boolean hasError() {
        return mHasError;
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
