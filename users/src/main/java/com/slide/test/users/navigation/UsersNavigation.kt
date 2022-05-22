package com.slide.test.users.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.slide.test.core_ui.navigation.animation.ExpandShrinkAnimation
import com.slide.test.core_ui.navigation.animation.SlideFromLeftAnimation
import com.slide.test.users.delete.UserDeleteDialogRoute
import com.slide.test.users.listing.UsersRoute

/**
 * Created by Stefan Halus on 18 May 2022
 */

fun NavGraphBuilder.usersGraph(navController: NavHostController) {

    composable(route = UsersDestination.route) { backStackEntry ->
        SlideFromLeftAnimation() {
            UsersRoute(
                userDeleteResult = backStackEntry?.savedStateHandle?.getLiveData(UserDeleteDestination.Result.resultArg),
                modifier = Modifier.fillMaxSize()
            ) { userId: Long, userName: String ->
                navController.navigate(UserDeleteDestination.createRoute(userId, userName))
            }
        }
    }

    dialog(
        route = UserDeleteDestination.route,
        arguments = UserDeleteDestination.arguments
    ) {
        ExpandShrinkAnimation() {
            UserDeleteDialogRoute(modifier = Modifier.fillMaxSize(),
                onDismiss = { deleted ->
                    val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
                    savedStateHandle?.set(UserDeleteDestination.Result.resultArg, deleted)
                    navController.popBackStack()
                })
        }
    }

}
