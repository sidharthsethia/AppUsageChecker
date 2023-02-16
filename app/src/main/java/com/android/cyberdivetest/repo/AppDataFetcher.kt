package com.android.cyberdivetest.repo

/**
 * Created by Sidharth Sethia on 16/02/23.
 */
interface AppDataFetcher {
    suspend fun fetchApps()
}