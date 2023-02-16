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
import com.android.cyberdivetest.others.Constants
import com.android.cyberdivetest.repo.AppUsageRepository
import com.android.cyberdivetest.ui.views.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint
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
        private const val ACTION_LEAVE_APP = "leave_app"
        private const val KEY_APP_ITEM = "bk_app"

        private fun newStopIntent(context: Context): Intent {
            return Intent(context, AppUsageCheckService::class.java).apply {
                action = ACTION_STOP
            }
        }

        fun newLeaveAppIntent(context: Context): Intent {
            return Intent(context, AppUsageCheckService::class.java).apply {
                action = ACTION_LEAVE_APP
            }
        }

        fun newIgnoreAppIntent(context: Context, item: AppTimeLimitItem): Intent {
            return Intent(context, AppUsageCheckService::class.java).apply {
                action = ACTION_IGNORE_APP
                putExtra(KEY_APP_ITEM, item)
            }
        }
    }

    @Inject
    lateinit var repository: AppUsageRepository

    override fun onCreate() {
        super.onCreate()
        startForeground(Constants.SERVICE_NOTIFICATION_ID, createNotification())
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent.action) {
            ACTION_IGNORE_APP -> {
                intent.getParcelableExtra<AppTimeLimitItem>(KEY_APP_ITEM)
                    ?.also { handleIgnoreIntent(it) }
            }
            ACTION_LEAVE_APP -> {
                stopAlertNotification()
                stopServiceInternal()
            }
            else -> {
                stopServiceInternal()
            }
        }
        return START_NOT_STICKY
    }

    private fun handleIgnoreIntent(item: AppTimeLimitItem) {
        lifecycleScope.launch {
            stopAlertNotification()
            repository.addIgnoredApp(IgnoredAppInfo(item.packageName, System.currentTimeMillis()))
            stopServiceInternal()
        }
    }

    private fun stopAlertNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(Constants.ALERT_NOTIFICATION_ID)
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
                    Constants.PENDING_INTENT_APP_USAGE_SERVICE,
                    newStopIntent(this),
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
}