package com.android.cyberdivetest.helpers

/**
 * Created by Sidharth Sethia on 15/02/23.
 */
interface StringFetcher {
    fun getString(id: Int): String
    fun getString(id: Int, vararg args: Any): String
}