package com.android.cyberdivetest.data

/**
 * Created by Sidharth Sethia on 15/02/23.
 */
sealed class ActionState {
    object Idle: ActionState()
    object Loading: ActionState()
    object AppTimeLimitInsertSuccess : ActionState()
    object AppTimeLimitDeleteSuccess : ActionState()
    class Failure(val error: String): ActionState()
}
