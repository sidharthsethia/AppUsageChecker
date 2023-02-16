package com.android.cyberdivetest.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * Created by Sidharth Sethia on 15/02/23.
 */
@Parcelize
@Entity
data class IgnoredAppInfo(
    @PrimaryKey val packageName: String,
    val timestamp: Long,
): Parcelable