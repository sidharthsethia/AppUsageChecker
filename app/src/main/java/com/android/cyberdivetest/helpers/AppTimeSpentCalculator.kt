package com.android.cyberdivetest.helpers

/**
 * Created by Sidharth Sethia on 14/02/23.
 */
interface AppTimeSpentCalculator {
    fun calculateTimeSpentInApp(): Map<String, Int>
}