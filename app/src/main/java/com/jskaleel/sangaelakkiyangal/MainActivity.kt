package com.jskaleel.sangaelakkiyangal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.jskaleel.sangaelakkiyangal.ui.navigation.NavigationHost
import com.jskaleel.sangaelakkiyangal.ui.theme.AppTheme
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
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        NavigationHost(
            navController = navController,
        )
    }
}