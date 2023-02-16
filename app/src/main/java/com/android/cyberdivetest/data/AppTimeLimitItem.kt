package com.android.cyberdivetest.data

/**
 * Created by Sidharth Sethia on 15/02/23.
 */
data class AppTimeLimitItem(
    val packageName: String,
    val appName: String,
    val timeUsedInMin: Int,
    val timeLimitInMin: Int
)
