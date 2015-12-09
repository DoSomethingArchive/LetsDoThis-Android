package org.dosomething.letsdothis.tasks;
import android.content.Context;
import android.text.TextUtils;

import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.ReportBack;
import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseCampaign;
import org.dosomething.letsdothis.network.models.ResponseCampaignWrapper;
import org.dosomething.letsdothis.network.models.ResponseGroup;
import org.dosomething.letsdothis.network.models.ResponseReportBackList;
import org.dosomething.letsdothis.network.models.ResponseUser;
import org.dosomething.letsdothis.network.models.ResponseUserCampaign;
import org.dosomething.letsdothis.utils.AppPrefs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;

/**
 * Created by izzyoji :) on 7/16/15.
 */
public class GroupTask extends Task
{
    private final int groupId;

    public Campaign campaign;
    public List<User> users       = new ArrayList<>();
    public List<ReportBack> reportBacks = new ArrayList<>();

    public GroupTask(int groupId)
    {
        this.groupId = groupId;
    }

    @Override
    protected void onComplete(Context context)
    {
        super.onComplete(context);
        EventBusExt.getDefault().post(this);
    }

    @Override
    protected void run(Context context) throws Throwable
    {

        ResponseGroup responseGroup = NetworkHelper.getNorthstarAPIService().group(groupId);

        int campaignId = responseGroup.data.campaign_id;
        ResponseCampaignWrapper responseCampaignWrapper = NetworkHelper.getPhoenixAPIService()
                                                                       .campaign(campaignId);

        campaign = ResponseCampaign.getCampaign(responseCampaignWrapper.data);


        String currentUser = AppPrefs.getInstance(context).getCurrentUserId();

        List<ResponseUser.Wrapper> responseUserWrappers = Arrays.asList(responseGroup.data.users);
        for(ResponseUser.Wrapper wrapper : responseUserWrappers)
        {
            ResponseUserCampaign.Wrapper.ResponseReportBackData reportBackData = wrapper.campaigns[0].reportback_data;
            User user = ResponseUser.getUser(wrapper);
            boolean me = TextUtils.equals(user.id, currentUser);

            if(me)
            {
                if(reportBackData != null)
                {
                    campaign.showShare = Campaign.UploadShare.SHARE;
                    reportBacks.addAll(
                            ResponseReportBackList.getReportBacks(reportBackData.reportback_items));
                }
                else
                {
                    campaign.showShare = Campaign.UploadShare.SHOW_OFF;
                }
            }
            else
            {
                users.add(user);
                if(reportBackData != null)
                {
                    reportBacks.addAll(
                            ResponseReportBackList.getReportBacks(reportBackData.reportback_items));
                }

            }

        }



    }

    @Override
    protected boolean handleError(Context context, Throwable e)
    {
        return false;
    }
}
