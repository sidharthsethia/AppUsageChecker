package com.android.cyberdivetest

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.android.cyberdivetest.others.Constants
import com.android.cyberdivetest.others.createNotificationChannel
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by Sidharth Sethia on 14/02/23.
 */

@HiltAndroidApp
class CyberDiveApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        createNotificationChannel(
            Constants.SERVICE_NOTIFICATION_CHANNEL,
            getString(R.string.app_monitoring_notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        createNotificationChannel(
            Constants.ALERT_NOTIFICATION_CHANNEL,
            getString(R.string.app_alert_notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )
    }
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}