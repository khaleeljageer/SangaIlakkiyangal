package com.jskaleel.sangaelakkiyangal.ui.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jskaleel.sangaelakkiyangal.ui.navigation.Screen

val topBarItems = listOf(
    Screen.Main.Home,
    Screen.Main.Download,
    Screen.BookList,
    Screen.Main.AboutApp
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentAwareTopAppBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    backStackEntry?.let { entry ->
        val viewModel: TopAppBarViewModel = viewModel(
            viewModelStoreOwner = entry,
            initializer = { TopAppBarViewModel() },
        )

        TopAppBar(
            title = viewModel.title,
            navigationIcon = viewModel.navigationIcon,
            scrollBehavior = scrollBehavior,
            modifier = modifier,
        )
    }
}

@Composable
fun ProvideAppBarTitle(title: @Composable () -> Unit) {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    (viewModelStoreOwner as? NavBackStackEntry)?.let { owner ->
        val viewModel: TopAppBarViewModel = viewModel(
            viewModelStoreOwner = owner,
            initializer = { TopAppBarViewModel() },
        )
        LaunchedEffect(title) {
            viewModel.title = title
        }
    }
}

@Composable
fun ProvideAppBarNavigationIcon(navigationIcon: @Composable () -> Unit) {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    (viewModelStoreOwner as? NavBackStackEntry)?.let { owner ->
        val viewModel: TopAppBarViewModel = viewModel(
            viewModelStoreOwner = owner,
            initializer = { TopAppBarViewModel() },
        )
        LaunchedEffect(navigationIcon) {
            viewModel.navigationIcon = navigationIcon
        }
    }
}

private class TopAppBarViewModel : ViewModel() {
    var title by mutableStateOf<@Composable () -> Unit>({ }, referentialEqualityPolicy())
    var navigationIcon by mutableStateOf<@Composable () -> Unit>({ }, referentialEqualityPolicy())
}
