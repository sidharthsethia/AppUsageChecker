package com.android.cyberdivetest.helpers

import com.android.cyberdivetest.data.AppInfo
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sidharth Sethia on 14/02/23.
 */
interface AppListFetcher {
    fun getInstalledApps() : Flow<List<AppInfo>>
}