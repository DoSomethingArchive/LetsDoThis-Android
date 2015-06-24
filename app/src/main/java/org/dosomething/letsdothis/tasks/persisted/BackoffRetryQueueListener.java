package org.dosomething.letsdothis.tasks.persisted;
import android.os.Handler;

import co.touchlab.android.threading.tasks.BaseTaskQueue;
import co.touchlab.android.threading.tasks.Task;
import co.touchlab.android.threading.tasks.persisted.PersistedTaskQueue;

/**
 * Created by kgalligan on 6/19/15.
 */
public class BackoffRetryQueueListener implements BaseTaskQueue.QueueListener
{
    private int tries = 0;

    @Override
    public void queueStarted(BaseTaskQueue queue)
    {

    }

    @Override
    public void queueFinished(final BaseTaskQueue queue)
    {
        if(queue.countTasks() > 0)
        {
            if(tries > 2)
                return;

            Handler handler = new Handler();
            int delay = 5000;
            if(tries == 0)
            {
                delay = 5000;
            }
            else if(tries == 1)
            {
                delay = 30000;
            }
            else if(tries == 2)
            {
                delay = 120000;
            }

            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    ((PersistedTaskQueue) queue).restartQueue();
                }
            }, delay);

            tries++;
        }
        else
        {
            tries = 0;
        }
    }

    @Override
    public void taskStarted(BaseTaskQueue queue, Task task)
    {

    }

    @Override
    public void taskFinished(BaseTaskQueue queue, Task task)
    {

    }
}
