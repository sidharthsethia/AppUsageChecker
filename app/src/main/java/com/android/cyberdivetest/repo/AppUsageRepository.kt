package com.android.cyberdivetest.repo

import com.android.cyberdivetest.data.AppTimeLimitItem
import com.android.cyberdivetest.data.IgnoredAppInfo
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sidharth Sethia on 16/02/23.
 */
interface AppUsageRepository: AppDataFetcher {
    fun getOverLimitApps(): Flow<List<AppTimeLimitItem>>
    suspend fun addIgnoredApp(appInfo: IgnoredAppInfo)
    suspend fun isAppIgnored(packageName: String): Boolean

    suspend fun getIgnoredAppCount(): Int
}