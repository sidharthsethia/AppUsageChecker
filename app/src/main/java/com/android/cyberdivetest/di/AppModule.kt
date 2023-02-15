package com.android.cyberdivetest.di

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.UserManager
import androidx.room.Room
import com.android.cyberdivetest.db.AppDB
import com.android.cyberdivetest.db.AppInfoDao
import com.android.cyberdivetest.helpers.AppListFetcher
import com.android.cyberdivetest.helpers.AppListFetcherImpl
import com.android.cyberdivetest.helpers.AppTimeSpentCalculator
import com.android.cyberdivetest.helpers.AppTimeSpentCalculatorImpl
import com.android.cyberdivetest.helpers.UsageStatsPermissionChecker
import com.android.cyberdivetest.helpers.UsageStatsPermissionCheckerImpl
import com.android.cyberdivetest.others.Constants
import com.android.cyberdivetest.repo.AppListRepository
import com.android.cyberdivetest.repo.AppListRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Sidharth Sethia on 14/02/23.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDB(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(
        context,
        AppDB::class.java,
        Constants.APP_DB_NAME
    ).build()

    @Singleton
    @Provides
    fun provideAppInfoDao(db: AppDB) = db.getAppInfoDao()

    @Singleton
    @Provides
    fun provideMonitoredAppInfoDao(db: AppDB) = db.getMonitoredAppInfoDao()

    @Provides
    fun providePackageManager(@ApplicationContext context: Context) = context.packageManager

    @Provides
    fun provideUserManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.USER_SERVICE) as UserManager

    @Provides
    fun provideUsageStatsManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    @Provides
    fun provideUsageStatsPermissionChecker(
        @ApplicationContext context: Context
    ): UsageStatsPermissionChecker = UsageStatsPermissionCheckerImpl(context)

    @Provides
    fun provideAppTimeSpentCalculator(
        usageStatsManager: UsageStatsManager,
        userManager: UserManager,
        permissionChecker: UsageStatsPermissionChecker
    ): AppTimeSpentCalculator = AppTimeSpentCalculatorImpl(
        usageStatsManager, permissionChecker, userManager
    )

    @Provides
    fun provideAppListFetcher(
        packageManager: PackageManager,
        timeSpentCalculator: AppTimeSpentCalculator
    ): AppListFetcher = AppListFetcherImpl(packageManager, timeSpentCalculator)

    @Provides
    fun provideAppListRepository(
        appListFetcher: AppListFetcher,
        appInfoDao: AppInfoDao
    ): AppListRepository = AppListRepositoryImpl(appListFetcher, appInfoDao)
}