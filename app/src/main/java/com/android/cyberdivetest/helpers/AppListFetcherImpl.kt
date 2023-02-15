package com.android.cyberdivetest.helpers

import android.content.Intent
import android.content.pm.PackageManager
import com.android.cyberdivetest.data.AppInfo
import com.android.cyberdivetest.others.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

/**
 * Created by Sidharth Sethia on 14/02/23.
 */
class AppListFetcherImpl(
    private val packageManager: PackageManager,
    private val timeSpentCalculator: AppTimeSpentCalculator
    ) : AppListFetcher {

    override fun getInstalledApps(): Flow<List<AppInfo>> = flow {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val packages = packageManager.queryIntentActivities(intent, 0)
        val timeSpentMap = timeSpentCalculator.calculateTimeSpentInApp()
        emit(
            packages
                .filterNotNull()
                .map {
                    val packageName = it.activityInfo.applicationInfo.packageName
                    val appName = it.loadLabel(packageManager)?.toString() ?: ""
                    Timber.tag(Constants.APP_LOG_TAG).d("App - $appName" +
                            "${packageName}, ${timeSpentMap.getOrDefault(packageName, -1)}")
                    AppInfo(
                        packageName,
                        appName,
                        timeSpentMap.getOrDefault(packageName, 0)
                    )
                }
        )
    }

}