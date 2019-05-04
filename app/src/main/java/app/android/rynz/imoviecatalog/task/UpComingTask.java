package app.android.rynz.imoviecatalog.task;

import android.content.Context;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

import app.android.rynz.imoviecatalog.service.UpComingTaskService;

public class UpComingTask
{
    private GcmNetworkManager gcmNetworkManager;

    public UpComingTask(Context context)
    {
        gcmNetworkManager = GcmNetworkManager.getInstance(context);
    }

    public void createPeriodicTask()
    {
        Task periodicTask = new PeriodicTask.Builder()
                .setService(UpComingTaskService.class)
                .setPeriod(UpComingTaskService.DEFAULT_PERIOD)
                .setFlex(UpComingTaskService.DEFAULT_FLEX)
                .setTag(UpComingTaskService.TAG_UPCOMING_MOVIE_LOG)
                .setPersisted(true)
                .build();
        gcmNetworkManager.schedule(periodicTask);
    }

    public void cancelPeriodicTask()
    {
        if (gcmNetworkManager != null)
        {
            gcmNetworkManager.cancelTask(UpComingTaskService.TAG_UPCOMING_MOVIE_LOG, UpComingTaskService.class);
        }
    }

}
