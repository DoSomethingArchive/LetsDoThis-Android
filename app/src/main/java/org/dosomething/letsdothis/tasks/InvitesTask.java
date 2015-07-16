package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.dosomething.letsdothis.data.Campaign;
import org.dosomething.letsdothis.data.Invite;
import org.dosomething.letsdothis.network.NetworkHelper;
import org.dosomething.letsdothis.network.models.ResponseCampaignList;
import org.dosomething.letsdothis.network.models.ResponseUserCampaign;
import org.dosomething.letsdothis.utils.AppPrefs;
import org.dosomething.letsdothis.utils.Hashery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;

/**
 * Created by izzyoji :) on 7/14/15.
 */
public class InvitesTask extends Task
{
    public ArrayList<Invite> invites = new ArrayList<>();

    @Override
    protected void run(Context context) throws Throwable
    {
        HashMap<Integer, Invite> inviteHashMap = new HashMap<>();

        String currentUserId = AppPrefs.getInstance(context).getCurrentUserId();
        ResponseUserCampaign userCampaigns = NetworkHelper.getNorthstarAPIService()
                                                          .getUserCampaigns(currentUserId);

        for(ResponseUserCampaign.Wrapper campaignData : userCampaigns.data)
        {
            Invite invite = new Invite();
            invite.code = Hashery.getInstance(context).encode(campaignData.signup_group == null
                                                                      ? campaignData.signup_id
                                                                      : campaignData.signup_group);
            inviteHashMap.put(campaignData.drupal_id, invite);
        }

        String campaignIds = StringUtils.join(inviteHashMap.keySet(), ",");
        ResponseCampaignList responseCampaignList = NetworkHelper.getDoSomethingAPIService()
                                                                 .campaignListByIds(campaignIds);
        List<Campaign> campaigns = ResponseCampaignList.getCampaigns(responseCampaignList);

        for(Campaign campaign : campaigns)
        {
            Invite invite = inviteHashMap.get(campaign.id);
            invite.title = campaign.title;
            invite.details = campaign.callToAction;
        }

        invites.addAll(inviteHashMap.values());
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }

    @Override
    protected boolean handleError(Context context, Throwable e)
    {
        return false;
    }
}
