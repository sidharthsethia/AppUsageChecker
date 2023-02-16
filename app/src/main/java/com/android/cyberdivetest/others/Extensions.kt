package com.android.cyberdivetest.others

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

/**
 * Created by Sidharth Sethia on 16/02/23.
 */

fun Context.createNotificationChannel(channelId: String, name: String, importance: Int) {
    val notificationManager = getSystemService(Application.NOTIFICATION_SERVICE) as NotificationManager
    val channel =
        NotificationChannel(channelId, name, importance)
    notificationManager.createNotificationChannel(channel)
}