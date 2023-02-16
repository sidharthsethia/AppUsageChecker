package com.android.cyberdivetest.workers

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.cyberdivetest.R
import com.android.cyberdivetest.data.AppTimeLimitItem
import com.android.cyberdivetest.helpers.AppUsageCheckerScheduler
import com.android.cyberdivetest.helpers.ForegroundAppChecker
import com.android.cyberdivetest.helpers.ScreenOnChecker
import com.android.cyberdivetest.helpers.StringFetcher
import com.android.cyberdivetest.others.Constants
import com.android.cyberdivetest.repo.AppUsageRepository
import com.android.cyberdivetest.services.AppUsageCheckService
import com.android.cyberdivetest.ui.views.activities.KillForegroundActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Created by Sidharth Sethia on 16/02/23.
 */
@HiltWorker
class AppUsageCheckerWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: AppUsageRepository,
    private val foregroundAppChecker: ForegroundAppChecker,
    private val screenOnChecker: ScreenOnChecker,
    private val stringFetcher: StringFetcher,
    private val scheduler: AppUsageCheckerScheduler
) : CoroutineWorker(context, workerParameters) {

    private var noOfOverLimitApps = 0
    private var noOfIgnoredApps = 0

    override suspend fun doWork(): Result {
        try {
            performTasks()
        } catch (e: Exception) {
            Timber.tag(Constants.APP_LOG_TAG).e(e)
        }
        return Result.success()
    }

    private suspend fun scheduleOneOfTaskIfNeeded() {
        noOfIgnoredApps = withContext(Dispatchers.IO) {
            repository.getIgnoredAppCount()
        }
        Timber.tag(Constants.APP_LOG_TAG).d("scheduleOneOfTaskIfNeeded - $noOfIgnoredApps, $noOfOverLimitApps")
        if (noOfOverLimitApps - noOfIgnoredApps > 0) {
            scheduler.scheduleServiceLaunch(Constants.ONE_TIME_APP_USAGE_CHECKER_WORKER_DELAY)
        }
    }

    private suspend fun performTasks() {
        withContext(Dispatchers.IO) {
            repository.fetchApps()
        }
        repository.getOverLimitApps().flowOn(Dispatchers.IO).collectLatest { list ->
            noOfOverLimitApps = list.size
            val currentForegroundAppSet = foregroundAppChecker.getCurrentForegroundApp()
            list.find { currentForegroundAppSet.contains(it.packageName) }
                ?.takeUnless { item ->
                    withContext(Dispatchers.IO) {
                        repository.isAppIgnored(item.packageName)
                    }
                }?.run {
                    showAlert(this)
                }
        }
        scheduleOneOfTaskIfNeeded()
    }

    private fun showAlert(item: AppTimeLimitItem) {
        if (screenOnChecker.isScreenOn()) {
            val notification = createAlertNotification(item)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(
                Constants.ALERT_NOTIFICATION_ID,
                notification
            )
        }
    }

    private fun createAlertNotification(item: AppTimeLimitItem): Notification {
        val activityIntent = KillForegroundActivity.newIntent(context)
        val activityPendingIntent = PendingIntent.getActivity(
            context, Constants.PENDING_INTENT_APP_USAGE_SERVICE,
            activityIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder =
            NotificationCompat.Builder(context, Constants.ALERT_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(stringFetcher.getString(R.string.app_overuse_alert_title, item.appName))
                .setContentText(stringFetcher.getString(R.string.app_overuse_alert_message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .addAction(
                    0,
                    stringFetcher.getString(R.string.app_ignore),
                    PendingIntent.getService(
                        context,
                        Constants.PENDING_INTENT_APP_USAGE_SERVICE,
                        AppUsageCheckService.newIgnoreAppIntent(context, item),
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                .addAction(
                    0,
                    stringFetcher.getString(R.string.app_leave_app),
                    activityPendingIntent
                )
        return notificationBuilder.build()
    }
}