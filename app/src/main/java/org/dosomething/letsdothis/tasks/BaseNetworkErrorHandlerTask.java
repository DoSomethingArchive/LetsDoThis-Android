package org.dosomething.letsdothis.tasks;
import android.content.Context;
import android.widget.Toast;

import java.net.HttpURLConnection;

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
        if(throwable instanceof RetrofitError)
        {
            RetrofitError retrofitError = (RetrofitError) throwable;
            int status = retrofitError.getResponse().getStatus();
            if(status == HttpURLConnection.HTTP_UNAUTHORIZED || status == HttpURLConnection.HTTP_NOT_FOUND)
            {
                return true;
            }
        }

        Toast.makeText(context, "There was a connection error.", Toast.LENGTH_SHORT).show();
        return true;
        //        return throwable.getCause() instanceof NetworkException;
    }
}
