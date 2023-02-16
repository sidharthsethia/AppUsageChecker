package com.android.cyberdivetest.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.cyberdivetest.data.IgnoredAppInfo
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sidharth Sethia on 15/02/23.
 */
@Dao
interface IgnoredAppInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(app: IgnoredAppInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<IgnoredAppInfo>)

    @Query("SELECT * FROM IgnoredAppInfo")
    fun getIgnoredApps(): Flow<List<IgnoredAppInfo>>

    @Query("SELECT * FROM IgnoredAppInfo WHERE packageName = :packageName")
    suspend fun findApp(packageName: String): IgnoredAppInfo?

    @Delete
    fun delete(app: IgnoredAppInfo)

    @Query("DELETE FROM IgnoredAppInfo WHERE packageName = :packageName")
    fun delete(packageName: String)

    @Query("DELETE FROM IgnoredAppInfo")
    fun nukeTable()

    @Query("SELECT COUNT() FROM MonitoredAppInfo")
    fun getIgnoredAppCount(): Int
}