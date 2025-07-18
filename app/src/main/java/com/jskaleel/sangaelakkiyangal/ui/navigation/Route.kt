package com.jskaleel.sangaelakkiyangal.ui.navigation

import android.os.Bundle

sealed class Screen(val route: String) {
    object Welcome {
        data object AboutApp : Screen("welcome_about_app")
    }

    data object BookList : Screen("book_list") {
        object Nav {
            private const val SUB_CATEGORY = "sub_category"
            val link = "$route/{$SUB_CATEGORY}"
            fun create(subCategory: String): String {
                return "$route/$subCategory"
            }

            fun get(bundle: Bundle?): String {
                return bundle?.getString(SUB_CATEGORY).orEmpty()
            }
        }
    }

    data object PdfReader : Screen("pdf_reader") {
        object Nav {
            private const val BOOK_ID = "book_id"
            val link = "$route/{$BOOK_ID}"

            fun create(bookId: String): String {
                return "$route/$bookId"
            }

            fun get(bundle: Bundle?): String {
                return bundle?.getString(BOOK_ID).orEmpty()
            }
        }
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
