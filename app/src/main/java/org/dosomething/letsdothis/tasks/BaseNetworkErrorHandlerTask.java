package org.dosomething.letsdothis.tasks;
import android.content.Context;

import org.apache.http.HttpStatus;

import co.touchlab.android.threading.errorcontrol.NetworkException;
import co.touchlab.android.threading.tasks.Task;
import retrofit.RetrofitError;

/**
 * Created by toidiu on 4/16/15.
 */
public abstract class BaseNetworkErrorHandlerTask extends Task
{
    @Override
    protected boolean handleError(Context context, Throwable throwable)
    {
        if(((RetrofitError) throwable).getResponse().getStatus() == HttpStatus.SC_UNAUTHORIZED)
        {
            return true;
        }
        if(throwable
                .getCause() instanceof NetworkException || throwable instanceof NetworkException)
        {
            return true;
        }
        return false;
    }
}
