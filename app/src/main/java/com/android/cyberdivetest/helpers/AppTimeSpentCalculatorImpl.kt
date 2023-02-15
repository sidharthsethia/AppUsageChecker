package com.android.cyberdivetest.helpers

import android.app.usage.UsageStatsManager
import android.os.Build
import android.os.UserManager
import androidx.collection.arrayMapOf
import com.android.cyberdivetest.others.Constants

/**
 * Created by Sidharth Sethia on 14/02/23.
 */
class AppTimeSpentCalculatorImpl(
        private val usageStatsManager: UsageStatsManager,
        private val permissionChecker: UsageStatsPermissionChecker,
        private val userManager: UserManager
    ): AppTimeSpentCalculator {

    private fun isSafeToQuery() = permissionChecker.hasPermission()
            && (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || userManager.isUserUnlocked)

    override fun calculateTimeSpentInApp(): Map<String, Int> {
        val result = arrayMapOf<String, Int>()
        val endTime = System.currentTimeMillis()
        val startTime = endTime - Constants.ONE_DAY_IN_MILLIS
        val usageStats = usageStatsManager
            .takeIf { isSafeToQuery() }
            ?.queryUsageStats(UsageStatsManager.INTERVAL_BEST, startTime, endTime)
        usageStats?.filterNotNull()?.forEach {
            result[it.packageName] = it.totalTimeInForeground.div(1000*60).toInt()
        }
        return result
    }
}