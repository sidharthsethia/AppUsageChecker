package com.android.cyberdivetest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created by Sidharth Sethia on 14/02/23.
 */

@HiltAndroidApp
class CyberDiveApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}