package com.jskaleel.sangaelakkiyangal.ui.navigation

sealed class Screen(val route: String) {
    data object Download : Screen("download")
    data object Home : Screen("home")
    data object AboutApp : Screen("about_app")
}

sealed class Route(val name: String) {
    data object AboutApp : Route("Route_AboutApp")
    data object Main : Route("Route_Main")
}