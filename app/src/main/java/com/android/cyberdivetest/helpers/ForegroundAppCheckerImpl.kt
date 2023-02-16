package com.android.cyberdivetest.helpers

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import com.android.cyberdivetest.others.Constants


/**
 * Created by Sidharth Sethia on 16/02/23.
 */

class ForegroundAppCheckerImpl(
    private val usageStatsManager: UsageStatsManager,
    private val usageStatsPermissionChecker: UsageStatsPermissionChecker
): ForegroundAppChecker {

    override fun getCurrentForegroundApp(): String {
        if (usageStatsPermissionChecker.hasPermission()) {
            val endTime = System.currentTimeMillis()
            val startTime = endTime - Constants.ONE_HOUR_IN_MILLIS
            val usageEvents = usageStatsManager.queryEvents(startTime, endTime)
            val event = UsageEvents.Event()
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event)
                if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                   return event.packageName
                }
            }
        }
        return ""
    }
}