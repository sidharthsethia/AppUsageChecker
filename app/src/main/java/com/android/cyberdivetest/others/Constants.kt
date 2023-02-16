package com.android.cyberdivetest.others

/**
 * Created by Sidharth Sethia on 14/02/23.
 */
object Constants {
    const val APP_DB_NAME = "cyberdive_app_db"
    const val ONE_MINUTE_IN_MILLIS = 1000*60
    const val ONE_HOUR_IN_MILLIS = ONE_MINUTE_IN_MILLIS*60
    const val ONE_DAY_IN_MILLIS = ONE_HOUR_IN_MILLIS*24
    const val APP_LOG_TAG = "Cyberdive"

    // Service notification constants
    const val SERVICE_NOTIFICATION_ID = 1
    const val SERVICE_NOTIFICATION_CHANNEL = "cyberdive_service_channel"
    const val ALERT_NOTIFICATION_ID = 2
    const val ALERT_NOTIFICATION_CHANNEL = "cyberdive_alert_channel"

    // Service Launch - Periodic worker constants
    const val PERIODIC_APP_USAGE_CHECKER_INTERVAL = ONE_MINUTE_IN_MILLIS * 1
    const val PERIODIC_APP_USAGE_CHECKER_WORKER_TAG = "PeriodicAppUsageCheckerWorkerTag"
    const val PERIODIC_APP_USAGE_CHECKER_WORK_NAME = "PeriodicCheckOfAppUsage"
    const val ONE_TIME_APP_USAGE_CHECKER_WORKER_TAG = "OneTimeAppUsageCheckerWorkerTag"

    // DB Cleanup - Periodic worker constants
    const val PERIODIC_DB_CLEANER_INTERVAL_IN_HOURS = 24
    const val PERIODIC_DB_CLEANER_WORKER_TAG = "DBCleaner-PeriodicWorker"
    const val PERIODIC_DB_CLEANER_WORK_NAME = "PeriodicCleanupOfAppDB"

    // Pending intent request codes
    const val PENDING_INTENT_APP_USAGE_SERVICE = 102
}