package com.android.cyberdivetest.repo

import com.android.cyberdivetest.data.AppListItem
import com.android.cyberdivetest.db.AppInfoDao
import com.android.cyberdivetest.helpers.AppListFetcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Sidharth Sethia on 14/02/23.
 */
class AppListRepositoryImpl @Inject constructor(
    private val fetcher: AppListFetcher,
    private val appInfoDao: AppInfoDao
) : AppListRepository {

    override suspend fun fetchApps() {
        fetcher.getInstalledApps().collectLatest {
            appInfoDao.insertAll(it)
        }
    }

    override fun getAppListItems(): Flow<List<AppListItem>> {
        return appInfoDao.getAppList().map {
            it.map { appInfo ->
                AppListItem(
                    appInfo.packageName,
                    appInfo.appName,
                    appInfo.timeSpentInMin
                )
            }
        }
    }
}