package com.android.cyberdivetest.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Sidharth Sethia on 15/02/23.
 */
@Parcelize
data class AppTimeLimitItem(
    val packageName: String,
    val appName: String,
    val timeUsedInMin: Int,
    val timeLimitInMin: Int
):Parcelable
