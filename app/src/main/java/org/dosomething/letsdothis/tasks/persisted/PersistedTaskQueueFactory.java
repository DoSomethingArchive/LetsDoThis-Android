package org.dosomething.letsdothis.tasks.persisted;
import android.app.Application;
import android.content.Context;

import co.touchlab.android.threading.tasks.persisted.ConfigException;
import co.touchlab.android.threading.tasks.persisted.PersistedTaskQueue;
import co.touchlab.android.threading.tasks.persisted.PersistedTaskQueueConfig;

/**
 * Created by kgalligan on 6/19/15.
 */
public class PersistedTaskQueueFactory
{
    private static PersistedTaskQueue INSTANCE;

    public static synchronized PersistedTaskQueue getInstance(Context context)
    {
        if(INSTANCE == null)
        {
            PersistedTaskQueueConfig build;
            try
            {
                PersistedTaskQueueConfig.Builder builder = new PersistedTaskQueueConfig.Builder();
                build = builder.addQueueListener(new BackoffRetryQueueListener()).build(context);

            }
            catch(ConfigException e)
            {
                throw new RuntimeException(e);
            }
            INSTANCE = new PersistedTaskQueue((Application) context.getApplicationContext(), build);
        }

        return INSTANCE;
    }
}
