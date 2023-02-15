package com.android.cyberdivetest.helpers

import android.app.AppOpsManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Sidharth Sethia on 14/02/23.
 */
class UsageStatsPermissionCheckerImpl(
    private val context: Context
) : UsageStatsPermissionChecker {

    override fun hasPermission(): Boolean {
        val appOpsManager =
            context.getSystemService(AppCompatActivity.APP_OPS_SERVICE) as AppOpsManager
        // `AppOpsManager.checkOpNoThrow` is deprecated from Android Q
        val mode = appOpsManager.unsafeCheckOpNoThrow(
            "android:get_usage_stats",
            android.os.Process.myUid(), context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }
}