package com.android.cyberdivetest.helpers

import android.app.ActivityManager
import android.content.Context
import androidx.collection.arraySetOf
import com.android.cyberdivetest.others.Constants
import timber.log.Timber


/**
 * Created by Sidharth Sethia on 16/02/23.
 */

class ForegroundAppCheckerImpl(
    private val context: Context
): ForegroundAppChecker {

    override fun getCurrentForegroundApp(): Set<String> {
        val result = arraySetOf<String>()
        val mActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        mActivityManager.runningAppProcesses.forEachIndexed { index, processInfo ->
            Timber.tag(Constants.APP_LOG_TAG).d("Foreground app $index from service - ${processInfo.processName}")
            result.add(processInfo.processName)
        }
        return result
    }
}