package com.android.cyberdivetest.repo

import kotlinx.coroutines.flow.Flow

/**
 * Created by Sidharth Sethia on 16/02/23.
 */
interface MonitoredAppCountRepository {
    fun getMonitoredAppCount(): Flow<Int>
}