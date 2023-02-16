package com.android.cyberdivetest.data

/**
 * Created by Sidharth Sethia on 15/02/23.
 */
data class TimeDuration(val durationInMin: Int, val name: String) {
    override fun toString(): String {
        return name
    }
}
