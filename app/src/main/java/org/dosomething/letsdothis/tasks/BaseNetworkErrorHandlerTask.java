package org.dosomething.letsdothis.tasks;
import android.content.Context;

import java.net.HttpURLConnection;

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
        return ((RetrofitError) throwable).getResponse()
                                          .getStatus() == HttpURLConnection.HTTP_UNAUTHORIZED || throwable
                .getCause() instanceof NetworkException;
//        I don't think we need the last line, as it says it's always false
//        return ((RetrofitError) throwable).getResponse()
//                                          .getStatus() == HttpURLConnection.HTTP_UNAUTHORIZED || throwable
//                .getCause() instanceof NetworkException || throwable instanceof NetworkException;
    }
}
