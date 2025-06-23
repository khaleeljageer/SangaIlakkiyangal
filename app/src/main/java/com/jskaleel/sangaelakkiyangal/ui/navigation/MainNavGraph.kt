package com.jskaleel.sangaelakkiyangal.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation


fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.Home.route,
        route = Route.Main.name
    ) {
        composable(route = Screen.Home.route) {

        }
        composable(route = Screen.AboutApp.route) {

        }
        composable(route = Screen.Download.route) {

        }
    }
}
