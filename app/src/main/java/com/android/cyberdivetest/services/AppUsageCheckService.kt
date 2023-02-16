package com.android.cyberdivetest.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.android.cyberdivetest.R
import com.android.cyberdivetest.data.AppTimeLimitItem
import com.android.cyberdivetest.data.IgnoredAppInfo
import com.android.cyberdivetest.helpers.ForegroundAppChecker
import com.android.cyberdivetest.helpers.ScreenOnChecker
import com.android.cyberdivetest.others.Constants
import com.android.cyberdivetest.repo.AppUsageRepository
import com.android.cyberdivetest.ui.views.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Sidharth Sethia on 15/02/23.
 */
@AndroidEntryPoint
class AppUsageCheckService : LifecycleService() {

    companion object {
        private const val ACTION_STOP = "stop"
        private const val ACTION_IGNORE_APP = "ignore"
        private const val KEY_APP_ITEM = "bk_app"
    }

    @Inject
    lateinit var repository: AppUsageRepository

    @Inject
    lateinit var foregroundAppChecker: ForegroundAppChecker

    @Inject
    lateinit var screenOnChecker: ScreenOnChecker

    override fun onCreate() {
        super.onCreate()
        startForeground(Constants.SERVICE_NOTIFICATION_ID, createNotification())
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return when (intent.action) {
            ACTION_STOP -> {
                stopServiceInternal()
                START_NOT_STICKY
            }
            ACTION_IGNORE_APP -> {
                intent.getParcelableExtra<AppTimeLimitItem>(KEY_APP_ITEM)
                    ?.also { handleIgnoreIntent(it) }
                START_NOT_STICKY
            }
            else -> {
                performTasks()
                START_STICKY
            }
        }
    }

    private fun performTasks() {
        lifecycleScope.launch {
            repository.fetchApps()
            repository.getOverLimitApps().collectLatest { list ->
                val currentForegroundAppSet = foregroundAppChecker.getCurrentForegroundApp()
                list.find { currentForegroundAppSet.contains(it.packageName) }
                    ?.takeUnless {
                        repository.isAppIgnored(packageName)
                    }?.run {
                        showAlert(this)
                    } ?: kotlin.run {
                        stopServiceInternal()
                    }
            }
        }
    }

    private fun handleIgnoreIntent(item: AppTimeLimitItem) {
        lifecycleScope.launch {
            repository.addIgnoredApp(IgnoredAppInfo(item.packageName, System.currentTimeMillis()))
            stopServiceInternal()
        }
    }

    private fun showAlert(item: AppTimeLimitItem) {
        if (screenOnChecker.isScreenOn()) {
            val notification = createAlertNotification(item)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(
                Constants.SERVICE_NOTIFICATION_ID,
                notification
            )
        }
    }

    private fun createAlertNotification(item: AppTimeLimitItem): Notification {
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        val homePendingIntent = PendingIntent.getActivity(
            this, 0,
            homeIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder =
            NotificationCompat.Builder(this, Constants.SERVICE_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(getString(R.string.app_overuse_alert_title, item.appName))
                .setContentText(getString(R.string.app_overuse_alert_message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .addAction(
                    0,
                    getString(R.string.app_ignore),
                    PendingIntent.getService(
                        this,
                        0,
                        newIgnoreAppIntent(item),
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                .addAction(
                    0,
                    getString(R.string.app_leave_app),
                    homePendingIntent
                )
        return notificationBuilder.build()
    }

    private fun createNotification(): Notification {
        val pendingIntent = getPendingIntent()
        val builder = NotificationCompat.Builder(this, Constants.SERVICE_NOTIFICATION_CHANNEL)
        builder
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(getString(R.string.app_service_notification_title))
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .addAction(
                0,
                getString(R.string.app_stop_monitoring),
                PendingIntent.getService(
                    this,
                    0,
                    newStopIntent(),
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .setOnlyAlertOnce(false)
            .setDefaults(0)
            .setSound(null)
            .priority = NotificationCompat.PRIORITY_LOW
        // last 4 done to make this update as less noisy as possible
        return builder.build()
    }

    private fun newStopIntent(): Intent {
        return Intent(this, AppUsageCheckService::class.java).apply {
            action = ACTION_STOP
        }
    }

    private fun newIgnoreAppIntent(item: AppTimeLimitItem): Intent {
        return Intent(this, AppUsageCheckService::class.java).apply {
            action = ACTION_STOP
            putExtra(KEY_APP_ITEM, item)
        }
    }

    private fun getPendingIntent(): PendingIntent? {
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun stopServiceInternal() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}