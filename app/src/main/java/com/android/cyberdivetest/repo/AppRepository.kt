package com.android.cyberdivetest.repo

import com.android.cyberdivetest.data.AppListItem
import com.android.cyberdivetest.data.AppTimeLimitItem
import com.android.cyberdivetest.data.IgnoredAppInfo
import com.android.cyberdivetest.data.MonitoredAppInfo
import com.android.cyberdivetest.db.AppInfoDao
import com.android.cyberdivetest.db.IgnoredAppInfoDao
import com.android.cyberdivetest.db.MonitoredAppInfoDao
import com.android.cyberdivetest.helpers.AppListFetcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Sidharth Sethia on 14/02/23.
 */
class AppRepository @Inject constructor(
    private val fetcher: AppListFetcher,
    private val appInfoDao: AppInfoDao,
    private val monitoredAppInfoDao: MonitoredAppInfoDao,
    private val ignoredAppInfoDao: IgnoredAppInfoDao
) : AppListRepository, AppTimeLimitRepository, AppUsageRepository {

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

    override fun getAppTimeLimitItem(packageName: String): Flow<AppTimeLimitItem> {
        return appInfoDao.getApp(packageName)
            .combine(monitoredAppInfoDao.getMonitoredApp(packageName)) { appInfo, monitoredAppInfo ->
                AppTimeLimitItem(
                    packageName,
                    appInfo.appName,
                    appInfo.timeSpentInMin,
                    monitoredAppInfo?.timeLimitInMin ?: 0,
                )
            }
    }

    override suspend fun saveAppTimeLimit(packageName: String, timeLimitInMin: Int) {
        withContext(Dispatchers.IO) {
            monitoredAppInfoDao.insert(MonitoredAppInfo(packageName, timeLimitInMin))
        }
    }

    override suspend fun deleteAppTimeLimit(packageName: String) {
        withContext(Dispatchers.IO) {
            monitoredAppInfoDao.delete(packageName)
        }
    }

    private fun getMonitoredApps(): Flow<List<AppTimeLimitItem>> {
        return appInfoDao.getAppList()
            .combine(monitoredAppInfoDao.getMonitoredApps()) { appInfo, monitoredAppInfo ->
                val timeLimitMap =
                    monitoredAppInfo.associate { it.packageName to it.timeLimitInMin }
                appInfo
                    .filter { it.packageName in timeLimitMap.keys }
                    .map {
                        AppTimeLimitItem(
                            it.packageName,
                            it.appName,
                            it.timeSpentInMin,
                            timeLimitMap.getOrDefault(it.packageName, 0)
                        )
                    }
            }
    }

    override fun getOverLimitApps(): Flow<List<AppTimeLimitItem>> {
        return getMonitoredApps().map { list ->
            list.filter { it.timeUsedInMin > it.timeLimitInMin }
        }
    }

    override suspend fun deleteFromIgnoredAppList(packageName: String) {
        withContext(Dispatchers.IO) {
            ignoredAppInfoDao.delete(packageName)
        }
    }

    override suspend fun addIgnoredApp(appInfo: IgnoredAppInfo) {
        withContext(Dispatchers.IO) {
            ignoredAppInfoDao.insert(appInfo)
        }
    }

    override suspend fun isAppIgnored(packageName: String): Boolean {
        return withContext(Dispatchers.IO) {
            ignoredAppInfoDao.findApp(packageName) != null
        }
    }

    override suspend fun hasMonitoredApps(): Boolean {
        return withContext(Dispatchers.IO) {
            monitoredAppInfoDao.getMonitoredAppCount() > 0
        }
    }
}