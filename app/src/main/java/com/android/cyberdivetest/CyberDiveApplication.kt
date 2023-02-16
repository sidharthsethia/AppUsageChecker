package com.android.cyberdivetest

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.android.cyberdivetest.others.Constants
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
            getString(R.string.app_monitoring_notification_channel_name)
        )
    }
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    private fun createNotificationChannel(channelId: String, name: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }
}