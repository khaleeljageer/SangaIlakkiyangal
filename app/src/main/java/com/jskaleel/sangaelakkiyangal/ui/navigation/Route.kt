package com.jskaleel.sangaelakkiyangal.ui.navigation

sealed class Screen(val route: String) {
    object Welcome {
        data object AboutApp : Screen("welcome_about_app")
    }

    object Main {
        data object Download : Screen("main_download")
        data object Home : Screen("main_home")
        data object AboutApp : Screen("main_about_app")
    }
}

sealed class Route(val name: String) {
    data object Welcome : Route("Route_Welcome")
    data object Main : Route("Route_Main")
}