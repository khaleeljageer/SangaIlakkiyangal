package com.jskaleel.sangaelakkiyangal.ui.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jskaleel.sangaelakkiyangal.R
import com.jskaleel.sangaelakkiyangal.ui.model.BottomBarItem
import com.jskaleel.sangaelakkiyangal.ui.navigation.Route
import com.jskaleel.sangaelakkiyangal.ui.navigation.Screen

val bottomBarItems = listOf(
    BottomBarItem(
        title = "Home",
        icon = R.drawable.rounded_dashboard_24,
        route = Screen.Main.Home.route
    ),
    BottomBarItem(
        title = "Download",
        icon = R.drawable.rounded_downloads_24,
        route = Screen.Main.Download.route
    ),
    BottomBarItem(
        title = "About",
        icon = R.drawable.rounded_info_24,
        route = Screen.Main.AboutApp.route
    )
)

val bottomBarMenuRoutes = bottomBarItems.map { it.route }

@Composable
fun BottomNavigationBar(
    items: List<BottomBarItem>,
    navController: NavController,
    currentRoute: String?
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp),
        tonalElevation = 6.dp,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.title,
                    )
                },
                label = { },
                selected = currentRoute == item.route,
                onClick = {
                    if (item.route == Route.Main.name) {
                        navController.popBackStack(Route.Main.name, inclusive = false)
                    }
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(Route.Main.name) {
                                saveState = true
                            }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.background,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            )
        }
    }
}
