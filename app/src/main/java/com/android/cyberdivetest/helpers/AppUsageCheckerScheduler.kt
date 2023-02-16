package com.android.cyberdivetest.helpers

/**
 * Created by Sidharth Sethia on 16/02/23.
 */
interface AppUsageCheckerScheduler {
    fun schedulePeriodicAppUsageChecker()
    fun scheduleServiceLaunch(delayInMin: Int = 0)
    fun cancelFutureLaunches()
}