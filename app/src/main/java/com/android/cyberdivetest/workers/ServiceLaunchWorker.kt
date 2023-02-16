package com.android.cyberdivetest.workers

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.cyberdivetest.services.AppUsageCheckService

/**
 * Created by Sidharth Sethia on 16/02/23.
 */
class ServiceLaunchWorker(val context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {
    override fun doWork(): Result {
        val intent = Intent(context, AppUsageCheckService::class.java)
        ContextCompat.startForegroundService(context, intent)
        return Result.success()
    }
}