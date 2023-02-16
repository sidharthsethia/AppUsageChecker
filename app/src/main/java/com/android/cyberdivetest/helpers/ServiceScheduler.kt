package com.android.cyberdivetest.helpers

/**
 * Created by Sidharth Sethia on 16/02/23.
 */
interface ServiceScheduler {
    fun schedulePeriodicServiceLaunch()
    fun scheduleServiceLaunch()
    fun cancelFutureLaunches()
}