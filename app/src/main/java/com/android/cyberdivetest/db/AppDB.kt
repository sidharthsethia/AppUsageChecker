package com.android.cyberdivetest.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.cyberdivetest.data.AppInfo
import com.android.cyberdivetest.data.MonitoredAppInfo

/**
 * Created by Sidharth Sethia on 14/02/23.
 */

@Database(
    entities = [AppInfo::class, MonitoredAppInfo::class],
    version = 1
)
abstract class AppDB: RoomDatabase() {
    abstract fun getAppInfoDao(): AppInfoDao
    abstract fun getMonitoredAppInfoDao(): MonitoredAppInfoDao
}