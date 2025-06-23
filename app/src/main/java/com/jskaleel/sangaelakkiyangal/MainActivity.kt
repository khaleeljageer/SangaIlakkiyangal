package com.jskaleel.sangaelakkiyangal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jskaleel.sangaelakkiyangal.ui.model.BottomBarItem
import com.jskaleel.sangaelakkiyangal.ui.navigation.NavigationHost
import com.jskaleel.sangaelakkiyangal.ui.navigation.Screen
import com.jskaleel.sangaelakkiyangal.ui.theme.AppTheme
import com.jskaleel.sangaelakkiyangal.ui.utils.BottomNavigationBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                MainNavigation()
            }
        }
    }
}

@Composable
private fun MainNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val bottomBarItems = listOf(
        BottomBarItem(
            title = "Home",
            icon = painterResource(id = R.drawable.rounded_dashboard_24),
            route = Screen.Main.Home.route
        ),
        BottomBarItem(
            title = "Download",
            icon = painterResource(id = R.drawable.rounded_downloads_24),
            route = Screen.Main.Download.route
        ),
        BottomBarItem(
            title = "About",
            icon = painterResource(id = R.drawable.rounded_info_24),
            route = Screen.Main.AboutApp.route
        )
    )
    // Show bottom bar only on home screen
    val showBottomBar = bottomBarItems.any { it.route in currentRoute.orEmpty() }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    items = bottomBarItems,
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (showBottomBar) innerPadding else PaddingValues(all = 0.dp))
        ) {
            NavigationHost(
                navController = navController,
            )
        }
    }
}