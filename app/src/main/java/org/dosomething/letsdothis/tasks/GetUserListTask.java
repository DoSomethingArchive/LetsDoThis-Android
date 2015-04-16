package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.dosomething.letsdothis.data.User;
import org.dosomething.letsdothis.network.DataHelper;
import org.dosomething.letsdothis.network.NorthstarAPI;
import org.dosomething.letsdothis.network.models.UserResponse;

import java.util.ArrayList;
import java.util.List;

import co.touchlab.android.threading.eventbus.EventBusExt;
import co.touchlab.android.threading.tasks.Task;

/**
 * Created by toidiu on 4/16/15.
 */
public class GetUserListTask extends Task
{
    private static final int        RESULT_LIMIT = 100;
    public               List<User> userList     = new ArrayList<>();

    @Override
    protected void run(Context context) throws Throwable
    {
        boolean done = false;
        for(int page = 0; ! done; page++)
        {
            UserResponse response = DataHelper.makeRequestAdapter().create(NorthstarAPI.class)
                    .userList(page, RESULT_LIMIT);

            for(User user : response.data)
            {
                userList.add(user);
            }

            if(page%5==0)
            {
                EventBusExt.getDefault().post(this);
            }
            if(page == response.last_page)
            {
                done = true;
            }
        }
    }

    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        return false;
    }

    @Override
    protected void onComplete(Context context)
    {
        EventBusExt.getDefault().post(this);
        super.onComplete(context);
    }
}
