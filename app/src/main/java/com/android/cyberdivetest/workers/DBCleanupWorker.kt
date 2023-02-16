package com.android.cyberdivetest.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.cyberdivetest.db.IgnoredAppInfoDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Sidharth Sethia on 16/02/23.
 */
@HiltWorker
class DBCleanupWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val ignoredAppInfoDao: IgnoredAppInfoDao
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            ignoredAppInfoDao.nukeTable()
        }
        return Result.success()
    }
}