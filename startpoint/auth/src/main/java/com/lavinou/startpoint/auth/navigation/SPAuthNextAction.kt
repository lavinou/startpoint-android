package com.lavinou.startpoint.auth.navigation

import androidx.navigation.NavController
import com.lavinou.startpoint.navigation.MainContent

sealed interface SPAuthNextAction {

    data class NavigateTo(
        val route: Any,
        val keepBackStack: Boolean = false
    ) : SPAuthNextAction

    data class DisplayMessage(
        val title: String,
        val message: String
    ) : SPAuthNextAction

    data class FieldMessage(
        val field: String,
        val message: String
    ) : SPAuthNextAction
}

fun NavController.nextActionNavigateTo(action: SPAuthNextAction.NavigateTo) {
    if(action.route is MainContent) {
        popBackStack(action.route, inclusive = false)
    } else {
        navigate(action.route) {
            if(action.keepBackStack.not()) {
                popUpTo(MainContent) { inclusive = false }
            }
        }
    }
}