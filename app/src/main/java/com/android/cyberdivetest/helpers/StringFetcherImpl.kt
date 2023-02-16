package com.android.cyberdivetest.helpers

import android.content.Context

/**
 * Created by Sidharth Sethia on 15/02/23.
 */
class StringFetcherImpl(private val context: Context): StringFetcher {

    override fun getString(id: Int): String {
        return context.getString(id)
    }

    override fun getString(id: Int, vararg args: Any): String {
        return context.getString(id, *args)
    }
}