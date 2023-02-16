package com.android.cyberdivetest.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.cyberdivetest.data.MonitoredAppInfo
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sidharth Sethia on 14/02/23.
 */

@Dao
interface MonitoredAppInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(app: MonitoredAppInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<MonitoredAppInfo>)

    @Query("SELECT * FROM MonitoredAppInfo")
    fun getMonitoredApps(): Flow<List<MonitoredAppInfo>>

    @Query("SELECT * FROM MonitoredAppInfo WHERE packageName = :packageName")
    fun getMonitoredApp(packageName: String): Flow<MonitoredAppInfo?>

    @Delete
    fun delete(app: MonitoredAppInfo)

    @Query("DELETE FROM MonitoredAppInfo WHERE packageName = :packageName")
    fun delete(packageName: String)

    @Query("DELETE FROM MonitoredAppInfo")
    fun nukeTable()

    @Query("SELECT COUNT() FROM MonitoredAppInfo")
    fun getMonitoredAppCount(): Int
}