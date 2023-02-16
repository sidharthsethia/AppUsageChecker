package com.android.cyberdivetest.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.cyberdivetest.R
import com.android.cyberdivetest.data.ActionState
import com.android.cyberdivetest.data.TimeDuration
import com.android.cyberdivetest.helpers.StringFetcher
import com.android.cyberdivetest.repo.AppTimeLimitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Sidharth Sethia on 15/02/23.
 */
@HiltViewModel
class AppTimeLimitViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val repository: AppTimeLimitRepository,
    private val stringFetcher: StringFetcher
): ViewModel() {

    val timeDurations = listOf(
        TimeDuration(1, stringFetcher.getString(R.string.app_time_in_mins, 1)),
        TimeDuration(5, stringFetcher.getString(R.string.app_time_in_mins, 5)),
        TimeDuration(15, stringFetcher.getString(R.string.app_time_in_mins, 15)),
        TimeDuration(30, stringFetcher.getString(R.string.app_time_in_mins, 30)),
        TimeDuration(60, stringFetcher.getString(R.string.app_time_in_hr, 1)),
        TimeDuration(120, stringFetcher.getString(R.string.app_time_in_hr, 2))
    )

    private val packageName = state.get<String>("id") ?: throw IllegalArgumentException("Missing id")
    private val _actionStateFlow = MutableStateFlow<ActionState>(ActionState.Idle)
    var selectedPosition = 0
    val actionStateFlow = _actionStateFlow.asStateFlow()
    val appTimeLimitItemFlow = repository.getAppTimeLimitItem(packageName).onEach { item ->
        selectedPosition = timeDurations
            .indexOfFirst { item.timeLimitInMin == it.durationInMin }
            .takeIf { it >= 0 }
            ?: 0
    }
    val deleteFlow = repository.getAppTimeLimitItem(packageName)
        .map { it.timeLimitInMin > 0 }

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch {
            _actionStateFlow.emit(ActionState.Failure(exception.message ?: ""))
        }
    }

    fun saveTimeLimit() {
         viewModelScope.launch(exceptionHandler) {
             _actionStateFlow.emit(ActionState.Loading)
             repository.saveAppTimeLimit(packageName, timeDurations[selectedPosition].durationInMin)
             _actionStateFlow.emit(ActionState.AppTimeLimitInsertSuccess)
         }
    }

    fun deleteTimeLimit() {
        viewModelScope.launch(exceptionHandler) {
            _actionStateFlow.emit(ActionState.Loading)
            repository.deleteAppTimeLimit(packageName)
            _actionStateFlow.emit(ActionState.AppTimeLimitInsertSuccess)
        }
    }
}