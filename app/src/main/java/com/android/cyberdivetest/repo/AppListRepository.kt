package com.android.cyberdivetest.repo

import com.android.cyberdivetest.data.AppListItem
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sidharth Sethia on 14/02/23.
 */
interface AppListRepository {
    suspend fun fetchApps()
    fun getAppListItems(): Flow<List<AppListItem>>
}