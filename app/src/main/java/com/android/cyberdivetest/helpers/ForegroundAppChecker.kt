package com.android.cyberdivetest.helpers

/**
 * Created by Sidharth Sethia on 16/02/23.
 */
interface ForegroundAppChecker {
    fun getCurrentForegroundApp(): Set<String>
}