package com.android.cyberdivetest.helpers

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import androidx.collection.arraySetOf
import com.android.cyberdivetest.others.Constants
import timber.log.Timber


/**
 * Created by Sidharth Sethia on 16/02/23.
 */

class ForegroundAppCheckerImpl(
    private val usageStatsManager: UsageStatsManager,
    private val usageStatsPermissionChecker: UsageStatsPermissionChecker
): ForegroundAppChecker {

    override fun getCurrentForegroundApp(): Set<String> {
        val result = arraySetOf<String>()
        if (usageStatsPermissionChecker.hasPermission()) {
            val endTime = System.currentTimeMillis()
            val startTime = endTime - Constants.ONE_MINUTE_IN_MILLIS * 2
            val usageEvents = usageStatsManager.queryEvents(startTime, endTime)
            val event = UsageEvents.Event()

            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event)
                if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                    result.add(event.packageName)
                    Timber.tag(Constants.APP_LOG_TAG).d("Foreground - ${event.packageName}")
                } else {
                    Timber.tag(Constants.APP_LOG_TAG).d("Not Foreground - ${event.packageName}")
                }
            }
        }
        Timber.tag(Constants.APP_LOG_TAG).d("foreground result - $result")
        return result
    }
}