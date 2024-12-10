package com.example.todo.ui.navigation

import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination

sealed class Destination(
    val route: String,
    val args: Map<String, Any?> = mapOf()
) {
    data object MainScreen : Destination(Routes.MAIN_SCREEN)
    class AddTask(date: Long = System.currentTimeMillis()) :
        Destination(Routes.ADD_TASK, mapOf(ParamKey.DATE to date))
}


object Routes {
    const val MAIN_SCREEN = "main_screen"
    const val ADD_TASK = "add_task"
}

object ParamKey {
    const val DATE = "selected_date"
}

fun NavController.navigateTo(destination: Destination) {
    val routeLink = NavDeepLinkRequest.Builder
        .fromUri(NavDestination.createRoute(destination.route).toUri())
        .build()
    val args = bundleOf(*destination.args.toList().toTypedArray())

    val deepLinkMatch = graph.matchDeepLink(routeLink)
    if (deepLinkMatch != null) navigate(deepLinkMatch.destination.id, args)
    else navigate(destination.route)
}