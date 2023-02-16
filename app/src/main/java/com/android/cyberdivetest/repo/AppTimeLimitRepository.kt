package com.android.cyberdivetest.repo

import com.android.cyberdivetest.data.AppTimeLimitItem
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sidharth Sethia on 15/02/23.
 */
interface AppTimeLimitRepository {
    fun getAppTimeLimitItem(packageName: String): Flow<AppTimeLimitItem>
    suspend fun saveAppTimeLimit(packageName: String, timeLimitInMin: Int)
    suspend fun deleteAppTimeLimit(packageName: String)
}