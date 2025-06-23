package com.jskaleel.sangaelakkiyangal.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jskaleel.sangaelakkiyangal.ui.screens.main.about.AboutScreenRoute
import com.jskaleel.sangaelakkiyangal.ui.screens.main.downloads.DownloadScreenRoute
import com.jskaleel.sangaelakkiyangal.ui.screens.main.downloads.DownloadViewModel


fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.Main.Home.route,
        route = Route.Main.name
    ) {
        composable(route = Screen.Main.Home.route) {

        }
        composable(route = Screen.Main.Download.route) {
            val viewModel: DownloadViewModel = hiltViewModel()

            DownloadScreenRoute(
                openBook = {},
                viewModel = viewModel
            )
        }
        composable(route = Screen.Main.AboutApp.route) {
            AboutScreenRoute()
        }
    }
}
