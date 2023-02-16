package com.android.cyberdivetest.helpers

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.android.cyberdivetest.others.Constants
import com.android.cyberdivetest.workers.AppUsageCheckerWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by Sidharth Sethia on 16/02/23.
 */
class AppUsageCheckerSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
    ): AppUsageCheckerScheduler {

    override fun schedulePeriodicAppUsageChecker() {
        val periodicLaunchWork = PeriodicWorkRequest.Builder(
                AppUsageCheckerWorker::class.java,
                Constants.PERIODIC_APP_USAGE_CHECKER_INTERVAL.toLong(),
                TimeUnit.MINUTES
            )
                .addTag(Constants.PERIODIC_APP_USAGE_CHECKER_WORKER_TAG)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            Constants.PERIODIC_APP_USAGE_CHECKER_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,  //Existing Periodic Work policy
            periodicLaunchWork //work request
        )
    }

    override fun scheduleServiceLaunch(delayInMin: Int) {
        val workRequest = OneTimeWorkRequestBuilder<AppUsageCheckerWorker>()
            .addTag(Constants.ONE_TIME_APP_USAGE_CHECKER_WORKER_TAG)
            .setInitialDelay(delayInMin.toLong(), TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
        Timber.tag(Constants.APP_LOG_TAG).d("One Time Task - $delayInMin min")
    }

    override fun cancelFutureLaunches() {
        WorkManager.getInstance(context).cancelAllWorkByTag(Constants.PERIODIC_APP_USAGE_CHECKER_WORKER_TAG)
        WorkManager.getInstance(context).cancelAllWorkByTag(Constants.ONE_TIME_APP_USAGE_CHECKER_WORKER_TAG)
    }
}