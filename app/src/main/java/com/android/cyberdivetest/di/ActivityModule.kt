package com.android.cyberdivetest.di

import com.android.cyberdivetest.helpers.AppUsageCheckerScheduler
import com.android.cyberdivetest.helpers.AppUsageCheckerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

/**
 * Created by Sidharth Sethia on 16/02/23.
 */
@Module
@InstallIn(ActivityComponent::class)
interface ActivityModule {

    @ActivityScoped
    @Binds
    fun bindsAppUsageCheckerScheduler(
        impl: AppUsageCheckerImpl
    ): AppUsageCheckerScheduler
}