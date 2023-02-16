package com.android.cyberdivetest.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cyberdivetest.helpers.DBCleanupScheduler
import com.android.cyberdivetest.repo.AppListRepository
import com.android.cyberdivetest.repo.MonitoredAppCountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Sidharth Sethia on 14/02/23.
 */

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val repository: AppListRepository,
    countRepository: MonitoredAppCountRepository,
    dbCleanupScheduler: DBCleanupScheduler
) : ViewModel() {

    init {
        dbCleanupScheduler.scheduleNightlyDBCleanup()
    }

    val appListStateFlow = repository.getAppListItems()
    val launchServiceStateFlow = countRepository.getMonitoredAppCount().map {
        it > 0
    }

    fun update() {
        viewModelScope.launch {
            repository.fetchApps()
        }
    }
}