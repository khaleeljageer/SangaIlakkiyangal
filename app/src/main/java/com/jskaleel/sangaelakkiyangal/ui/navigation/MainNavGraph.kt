package com.jskaleel.sangaelakkiyangal.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.jskaleel.sangaelakkiyangal.ui.screens.booklist.BookListScreenRoute
import com.jskaleel.sangaelakkiyangal.ui.screens.booklist.BookListViewModel
import com.jskaleel.sangaelakkiyangal.ui.screens.main.about.AboutScreenRoute
import com.jskaleel.sangaelakkiyangal.ui.screens.main.books.BooksScreenRoute
import com.jskaleel.sangaelakkiyangal.ui.screens.main.books.BooksViewModel
import com.jskaleel.sangaelakkiyangal.ui.screens.main.downloads.DownloadScreenRoute
import com.jskaleel.sangaelakkiyangal.ui.screens.main.downloads.DownloadViewModel
import com.jskaleel.sangaelakkiyangal.ui.screens.reader.PdfReaderScreenRoute
import com.jskaleel.sangaelakkiyangal.ui.screens.reader.PdfReaderViewModel
import com.jskaleel.sangaelakkiyangal.ui.utils.InvokeOnce

fun NavGraphBuilder.mainNavGraph(navController: NavController) {
    navigation(
        startDestination = Screen.Main.Home.route,
        route = Route.Main.name
    ) {
        animatedComposable(route = Screen.Main.Home.route) {
            val viewModel: BooksViewModel = hiltViewModel()

            BooksScreenRoute(
                onNext = {
                    navController.navigate(
                        Screen.BookList.Link.create(it)
                    )
                },
                viewModel = viewModel
            )
        }

        animatedComposable(route = Screen.Main.Download.route) {
            val viewModel: DownloadViewModel = hiltViewModel()

            DownloadScreenRoute(
                openBook = { id ->
                    navController.navigate(
                        Screen.PdfReader.Link.create(bookId = id)
                    )
                },
                viewModel = viewModel
            )
        }

        animatedComposable(route = Screen.Main.AboutApp.route) {
            AboutScreenRoute()
        }

        animatedComposable(route = Screen.BookList.Link.link) { entry ->
            val viewModel: BookListViewModel = hiltViewModel()
            InvokeOnce {
                val subCategory = Screen.BookList.Link.get(entry.arguments)
                viewModel.setup(subCategory)
            }
            BookListScreenRoute(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                openBook = { id ->
                    navController.navigate(
                        Screen.PdfReader.Link.create(bookId = id)
                    )
                }
            )
        }
        animatedComposable(route = Screen.PdfReader.Link.link) { entry ->
            val viewModel: PdfReaderViewModel = hiltViewModel()
            InvokeOnce {
                val bookId = Screen.PdfReader.Link.get(entry.arguments)
                viewModel.setup(bookId)
            }
            PdfReaderScreenRoute(
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }
}
