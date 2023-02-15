package com.android.cyberdivetest.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.cyberdivetest.data.AppInfo
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sidharth Sethia on 14/02/23.
 */

@Dao
interface AppInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(app: AppInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<AppInfo>)

    @Query("SELECT * FROM AppInfo")
    fun getAppList(): Flow<List<AppInfo>>

    @Query("SELECT * FROM AppInfo WHERE packageName = :packageName")
    fun getApp(packageName: String): Flow<AppInfo>

    @Delete
    fun delete(app: AppInfo)

    @Query("DELETE FROM AppInfo WHERE packageName = :packageName")
    fun delete(packageName: String)

    @Query("DELETE FROM AppInfo")
    fun nukeTable()
}