package com.jskaleel.sangaelakkiyangal.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jskaleel.sangaelakkiyangal.ui.screens.main.about.AboutScreenRoute
import com.jskaleel.sangaelakkiyangal.ui.screens.main.booklist.BookListScreenRoute
import com.jskaleel.sangaelakkiyangal.ui.screens.main.books.BooksScreenRoute
import com.jskaleel.sangaelakkiyangal.ui.screens.main.books.BooksViewModel
import com.jskaleel.sangaelakkiyangal.ui.screens.main.downloads.DownloadScreenRoute
import com.jskaleel.sangaelakkiyangal.ui.screens.main.downloads.DownloadViewModel
import com.jskaleel.sangaelakkiyangal.ui.utils.InvokeOnce


fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.Main.Home.route,
        route = Route.Main.name
    ) {
        composable(route = Screen.Main.Home.route) {
            val viewModel: BooksViewModel = hiltViewModel()

            BooksScreenRoute(
                onNext = {
                    navController.navigate(
                        Screen.Main.BookList.Link.create(it)
                    )
                },
                viewModel = viewModel
            )
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

        composable(route = Screen.Main.BookList.Link.link) { entry ->
            val viewModel: BooksViewModel = hiltViewModel()
            InvokeOnce {
                val subCategory = Screen.Main.BookList.Link.get(entry.arguments)
                viewModel.setSubCategory(subCategory)
            }

            BookListScreenRoute(
                viewModel = viewModel,
                openBook = {
                }
            )
        }
    }
}
