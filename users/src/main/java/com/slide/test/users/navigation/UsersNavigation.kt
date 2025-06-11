package com.slide.test.users.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.slide.test.core_ui.navigation.animation.SlideFromLeftAnimation
import com.slide.test.users.create.UserCreateDialogRoute
import com.slide.test.users.delete.UserDeleteDialogRoute
import com.slide.test.users.details.UserDetailsRoute
import com.slide.test.users.listing.UsersRoute

/**
 * Created by Stefan Halus on 18 May 2022
 */

fun NavGraphBuilder.usersGraph(navController: NavHostController) {

    composable(route = UsersDestination.route) { backStackEntry ->
        SlideFromLeftAnimation {
            UsersRoute(
                userDeleteResult = backStackEntry.savedStateHandle.getLiveData(
                    UserDeleteDestination.Result.resultArg
                ),
                modifier = Modifier.fillMaxSize(),
                navigateToDelete = { userId: Long, userName: String ->
                    navController.navigate(UserDeleteDestination.createRoute(userId, userName))
                },
                navigateToCreate = {
                    navController.navigate(UserCreateDestination.route)
                },
                navigateToDetails = { userId: Long ->
                    navController.navigate(UserDetailsDestination.createRoute(userId))
                }
            )
        }
    }

    composable(
        route = UserDetailsDestination.route,
        arguments = UserDetailsDestination.arguments,
    ) { backStackEntry ->
        SlideFromLeftAnimation {
            UserDetailsRoute(
                modifier = Modifier.fillMaxSize(),
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }

    dialog(
        route = UserCreateDestination.route,
        dialogProperties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {
        UserCreateDialogRoute(
            modifier = Modifier.fillMaxSize(),
            onDismiss = { created ->
                val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
                savedStateHandle?.set(UserDeleteDestination.Result.resultArg, created)
                navController.popBackStack()
            })
    }


    dialog(
        route = UserDeleteDestination.route,
        arguments = UserDeleteDestination.arguments
    ) {
        UserDeleteDialogRoute(
            modifier = Modifier.fillMaxSize(),
            onDismiss = { deleted ->
                val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
                savedStateHandle?.set(UserDeleteDestination.Result.resultArg, deleted)
                navController.popBackStack()
            })
    }

}
