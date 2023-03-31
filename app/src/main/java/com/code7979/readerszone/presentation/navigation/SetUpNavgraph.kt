package com.code7979.readerszone.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.code7979.readerszone.presentation.screens.BookDetailsScreen
import com.code7979.readerszone.presentation.screens.ReadPdfScreen
import com.code7979.readerszone.presentation.screens.main_screen.MainScreen

@Composable
fun SetUpNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {

        composable(route = Screen.MainScreen.route) {
            MainScreen(mainNavController = navController)
        }

        composable(
            route = Screen.BookDetailsScreen.route,
            arguments = listOf(navArgument(BOOK_ID) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString(BOOK_ID) ?: ""
            BookDetailsScreen(
                bookId = bookId,
                navController = navController,
            )
        }

        composable(
            route = Screen.ReadPdfScreen.route,
            arguments = listOf(navArgument(BOOK_URL) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val bookUrl = backStackEntry.arguments?.getString(BOOK_URL) ?: ""
            ReadPdfScreen(bookUrl)
        }
    }

}


