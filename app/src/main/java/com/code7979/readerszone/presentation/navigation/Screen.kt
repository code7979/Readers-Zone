package com.code7979.readerszone.presentation.navigation

const val BOOK_ID = "book_Id"
const val BOOK_URL = "book_url"

sealed class Screen(var route: String) {
    object MainScreen : Screen("main_screen")

    object BookDetailsScreen : Screen("book_details_screen/{$BOOK_ID}") {
        fun passId(bookId: String): String {
            return this.route.replace(oldValue = "{$BOOK_ID}", newValue = bookId)
        }
    }

    object ReadPdfScreen : Screen("read_pdf_screen/{$BOOK_URL}") {
        fun passBookUrl(bookUrl: String): String {
            return this.route.replace(oldValue = "{$BOOK_URL}", newValue = bookUrl)
        }
    }
}
