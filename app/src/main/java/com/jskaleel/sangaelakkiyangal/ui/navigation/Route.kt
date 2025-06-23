package com.jskaleel.sangaelakkiyangal.ui.navigation

sealed class Screen(val route: String) {
    object Download : Screen("download")
    object Home : Screen("home")
    object AboutApp : Screen("about_app")
}

sealed class Route(val name: String) {
    object AboutApp : Route("Route_AboutApp")
    object Main : Route("Route_Main")
}