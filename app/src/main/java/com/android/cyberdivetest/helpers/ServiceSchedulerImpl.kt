package com.android.cyberdivetest.helpers

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.android.cyberdivetest.others.Constants
import com.android.cyberdivetest.workers.ServiceLaunchWorker
import java.util.concurrent.TimeUnit


/**
 * Created by Sidharth Sethia on 16/02/23.
 */
class ServiceSchedulerImpl(private val context: Context): ServiceScheduler {

    override fun schedulePeriodicServiceLaunch() {
        val periodicLaunchWork = PeriodicWorkRequest.Builder(
                ServiceLaunchWorker::class.java,
                Constants.PERIODIC_SERVICE_LAUNCHER_INTERVAL_IN_MIN.toLong(),
                TimeUnit.MINUTES
            )
                .addTag(Constants.PERIODIC_SERVICE_LAUNCHER_WORKER_TAG)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            Constants.PERIODIC_SERVICE_LAUNCHER_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,  //Existing Periodic Work policy
            periodicLaunchWork //work request
        )
    }

    override fun scheduleServiceLaunch() {
        val workRequest = OneTimeWorkRequestBuilder<ServiceLaunchWorker>()
            .addTag(Constants.ONE_TIME_SERVICE_LAUNCHER_WORKER_TAG)
            .build()
        WorkManager.getInstance().enqueue(workRequest)
    }

    override fun cancelFutureLaunches() {
        WorkManager.getInstance().cancelAllWorkByTag(Constants.PERIODIC_SERVICE_LAUNCHER_WORKER_TAG)
        WorkManager.getInstance().cancelAllWorkByTag(Constants.ONE_TIME_SERVICE_LAUNCHER_WORKER_TAG)
    }
}