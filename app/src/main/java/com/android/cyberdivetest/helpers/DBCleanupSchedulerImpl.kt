package com.android.cyberdivetest.helpers

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.android.cyberdivetest.others.Constants
import com.android.cyberdivetest.workers.DBCleanupWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Created by Sidharth Sethia on 16/02/23.
 */
class DBCleanupSchedulerImpl(val context: Context): DBCleanupScheduler {

    override fun scheduleNightlyDBCleanup() {
        val periodicLaunchWork = PeriodicWorkRequest.Builder(
            DBCleanupWorker::class.java,
            Constants.PERIODIC_DB_CLEANER_INTERVAL_IN_HOURS.toLong(),
            TimeUnit.HOURS
        )
            .addTag(Constants.PERIODIC_DB_CLEANER_WORKER_TAG)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .setInitialDelay(getInitialDelay(), TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            Constants.PERIODIC_DB_CLEANER_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,  //Replace Existing Periodic Work policy
            periodicLaunchWork //work request
        )
    }

    private fun getInitialDelay(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        return calendar.timeInMillis - System.currentTimeMillis()
    }
}