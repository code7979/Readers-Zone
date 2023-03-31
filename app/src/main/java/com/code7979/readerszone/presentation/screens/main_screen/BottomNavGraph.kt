package com.code7979.readerszone.presentation.screens.main_screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.code7979.readerszone.presentation.screens.main_screen.screens.Books
import com.code7979.readerszone.presentation.screens.main_screen.screens.Library
import com.code7979.readerszone.presentation.screens.main_screen.screens.Search

@Composable
fun BottomNavGraph(
    mainNavController: NavHostController,
    mainScreenNavController: NavHostController
) {
    NavHost(
        navController = mainScreenNavController,
        startDestination = BottomBarScreen.Library.route,
    ) {
        composable(route = BottomBarScreen.Library.route) {
            Library(mainNavController = mainNavController)
        }
        composable(route = BottomBarScreen.Books.route) {
            Books(mainNavController = mainNavController)
        }
        composable(route = BottomBarScreen.Search.route) {
            Search(mainNavController = mainNavController)
        }
    }
}