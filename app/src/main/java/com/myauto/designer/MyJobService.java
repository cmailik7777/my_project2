package com.myauto.designer;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;

/**
 * Created by Designer2 on 24.07.2018.
 */

public class MyJobService extends com.firebase.jobdispatcher.JobService {

    private static final String TAG = "MyJobService";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Performing long running task in scheduled job");
        // TODO(developer): add long running task here.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

}