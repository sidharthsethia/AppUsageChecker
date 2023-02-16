package com.android.cyberdivetest.helpers

import android.content.Context
import android.os.PowerManager
import androidx.lifecycle.LifecycleService

/**
 * Created by Sidharth Sethia on 16/02/23.
 */
class ScreenOnCheckerImpl(private val context: Context) : ScreenOnChecker {
    override fun isScreenOn(): Boolean {
        return (context.getSystemService(LifecycleService.POWER_SERVICE) as PowerManager)
            .isInteractive
    }
}