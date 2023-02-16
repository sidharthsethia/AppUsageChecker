package com.android.cyberdivetest.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cyberdivetest.repo.AppListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Sidharth Sethia on 14/02/23.
 */

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val repository: AppListRepository
) : ViewModel() {

    val appListStateFlow = repository.getAppListItems()

    fun update() {
        viewModelScope.launch { repository.fetchApps() }
    }
}