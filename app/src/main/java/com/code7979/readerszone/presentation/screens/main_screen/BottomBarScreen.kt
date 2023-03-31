package com.code7979.readerszone.presentation.screens.main_screen

import com.code7979.readerszone.R

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val drawableResource: Int
){
    object Library:BottomBarScreen(
        route = "Library",
        title = "Library",
        drawableResource = R.drawable.ic_library_se
    )
    object Books:BottomBarScreen(
        route = "my_books",
        title = " My Books",
        drawableResource = R.drawable.book_se
    )
    object Search:BottomBarScreen(
        route = "search",
        title = "Search",
        drawableResource = R.drawable.ic_search
    )
}
