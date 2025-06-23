package com.jskaleel.sangaelakkiyangal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.jskaleel.sangaelakkiyangal.ui.screens.aboutapp.AboutAppScreenRoute
import com.jskaleel.sangaelakkiyangal.ui.screens.aboutapp.AboutAppViewModel

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.AboutApp.name
    ) {
        navigation(
            startDestination = Screen.AboutApp.route,
            route = Route.AboutApp.name
        ) {
            composable(route = Screen.AboutApp.route) {
                val viewModel: AboutAppViewModel = hiltViewModel()
                AboutAppScreenRoute(
                    onNext = {},
                    viewModel = viewModel,
                )
            }
        }
//        mainNavGraph(navController = navController)
    }
}