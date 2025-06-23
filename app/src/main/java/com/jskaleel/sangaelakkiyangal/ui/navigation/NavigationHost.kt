package com.jskaleel.sangaelakkiyangal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jskaleel.sangaelakkiyangal.ui.screens.welcome.WelcomeScreenRoute
import com.jskaleel.sangaelakkiyangal.ui.screens.welcome.WelcomeViewModel

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.Welcome.name
    ) {
        navigation(
            startDestination = Screen.Welcome.AboutApp.route,
            route = Route.Welcome.name
        ) {
            composable(route = Screen.Welcome.AboutApp.route) {
                val viewModel: WelcomeViewModel = hiltViewModel()
                WelcomeScreenRoute(
                    onNext = {
                        navController.navigate(Route.Main.name) {
                            popUpTo(Route.Welcome.name) {
                                inclusive = true
                            }
                        }
                    },
                    viewModel = viewModel,
                )
            }
        }
        mainNavGraph(navController = navController)
    }
}