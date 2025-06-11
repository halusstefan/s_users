package com.slide.test.users.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.slide.test.core_ui.navigation.destination.NavDestination
import com.slide.test.users.navigation.UserDeleteDestination.Input
import com.slide.test.users.navigation.UserDeleteDestination.Input.userIdArg

/**
 * Created by Stefan Halus on 22 May 2022
 */
object UserDetailsDestination : NavDestination {

    private const val routeName: String = "user_details_route"

    override val route = "${routeName}/{${userIdArg}}"

    override val destination: String = "user_details_destination"

    val arguments = listOf(
        navArgument(Input.userIdArg) {
            type = NavType.LongType
        }
    )

    fun createRoute(userId: Long) : String {
        return "${routeName}/$userId"
    }

}